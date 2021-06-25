# IoT-data-showCase


**Use cases**

- Hundreds of thousands of vehicles driving across the globe, 24 hours a day, 7 days a week.
- Need to access aggregated real-time location, driving, behavior data, temperature sensors and more.

**Solution**

- Scalable, resiliency Kubernetes platform based on Tanzu
- Cloud Native Microservices based on Spring
- Reliable messaging deliverable with RabbitMQ
- Low latency data read/write operations with GemFire


![img.png](docs/images/overview.png)

# Project Modules


Applications                                                                        |    Notes
-------------------------------------------------------------------------           |    ----------------------
[geotabgroup-tracking-source](applications/geotabgroup-tracking-source)             |    Any car positioning based Geotab's fleet management streaming source microservice for vehicle details in JSON format
[IoT-connected-vehicle-dashboard](applications/IoT-connected-vehicle-dashboard)     |    GUI interface to views vehicle information
[IoT-connected-vehicles-sink](applications/IoT-connected-vehicles-sink)             |    Microservice streaming sink for storing Vehicle data in GemFire
[randmcnally-triplanning-source](applications/randmcnally-triplanning-source)       |    Trip planninhg details based on Rand McNally fleet management streaming source for vehicle data in XML format
[volvo-safecar-source](applications/volvo-safecar-source)                           |    Safe car Volvo Fleet Management management streaming source for vehicle data in XML format


# Docker

Use the following command to build docker images

```shell script
gradle :applications:vehicle-generator-app:bootBuildImage
gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
gradle :applications:vehicles-geode-sink:bootBuildImage

```

TODO: What does the prod look like (source providers or vehicle)


# K8 

Locally

```shell script
kind load docker-image vehicle-generator-app:0.0.1-SNAPSHOT
kind load docker-image iot-connected-vehicle-dashboard:0.0.1-SNAPSHOT
kind load docker-image iot-connected-vehicles-sink:0.0.1-SNAPSHOT
```

```shell script
kubectl apply -f cloud/k8/secrets
```

vehicle-secrets

```shell script
kubectl apply -f cloud/k8
```



# Accessing K8 Services

```shell
kubectl port-forward iot-connected-vehicle-dashboard 7070:7070
```


## RabbitMQ Access 
Get the RabbitMQ user/password
```shell script
kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"

export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo ""
echo "USER:" $ruser
echo "PASWORD:" $rpwd
```


Add new users

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user <user> <password>

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / <user> ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags <user> administrator

```


```shell script
kubectl port-forward rabbitmq-server-0 15672:15672
```

Scale RabbitMQ to 3 nodes

```shell script
kubectl apply -f cloud/k8/data-services/rabbitmq/rabbitmq-cluster-node3.yml
```

Scale GemFire to 2 locator and 3 datanodes

```shell script
kubectl apply -f cloud/k8/data-services/gemfire/gf-cluster-locators-2-datanodes-3.yml
```


k port-forward iot-connected-vehicle-dashboard 7000:7000


k apply -f config-maps.yml
k apply -f secrets/


### GKE

k apply -f cloud/GKE/k8/iot-connected-vehicle-dashboard.yml

k apply -f cloud/GKE/k8/iot-dashboard-service.yml



# WaveFront

```shell script
helm repo add wavefront https://wavefronthq.github.io/helm/
helm repo update
```

To deploy the Wavefront Collector and Wavefront Proxy:

Using helm 2:

helm install wavefront/wavefront --name wavefront --set wavefront.url=https://YOUR_CLUSTER.wavefront.com --set wavefront.token=YOUR_API_TOKEN --set clusterName=<YOUR_CLUSTER_NAME> --namespace wavefront

Using helm 3:

kubectl create namespace wavefront

```shell script

```
helm install wavefront wavefront/wavefront --set wavefront.url=https://YOUR_CLUSTER.wavefront.com --set wavefront.token=YOUR_API_TOKEN --set clusterName=<YOUR_CLUSTER_NAME> --namespace wavefront


[Local WaveFront](https://vmware.wavefront.com/dashboards/integration-kubernetes-clusters#_v01(g:(d:7200,ls:!t,s:1617894218,w:'2h'),p:(cluster_name:(v:gregoryg-cluster))))

Look for gregoryg-cluster



Return generate 

k delete pod vehicle-generator-app

## Streaming


mvn -Dmaven.test.skip=true install

git remote -v
origin	https://github.com/rabbitmq/rabbitmq-stream-java-client.git (fetch)
origin	https://github.com/rabbitmq/rabbitmq-stream-java-client.git (push)


## Lessons Learned

**K8 Issues**
- First signs of performance issues based on 
  ```Error from server: etcdserver: leader changed```
  ```error: error upgrading connection: etcdserver: request timed out```

- In order to use higher level k8 objects  like deployments, then grant the *default* service account the role cluster-admin  
  ```shell script 
  kubectl create clusterrolebinding default-cluster-admin --clusterrole=cluster-admin   --serviceaccount=default:default
  ```

- Do not set resource limits, or set them to higher numbers

-   Normal   Started              3h35m (x2 over 3h45m)  kubelet, tkc-1-workers-2klw8-f4dbb4875-s6t69  Started container vehicle-streaming-geode-sink
    Warning  NodeNotReady         3h30m                  node-controller                               Node is not ready
    Warning  Evicted              3h25m                  kubelet, tkc-1-workers-2klw8-f4dbb4875-s6t69  The node was low on resource: ephemeral-storage. Container vehicle-streaming-geode-sink was using 1586092Ki, which exceeds its request of 0.
    Normal   Killing              3h25m                  kubelet, tkc-1-workers-2klw8-f4dbb4875-s6t69  Stopping container vehicle-streaming-geode-sink
    Warning  ExceededGracePeriod  3h24m                  kubelet, tkc-1-workers-2klw8-f4dbb4875-s6t69  Container runtime did not kill the pod within specified grace period.
    
**RabbitMQ**

- Review logs of across the RabbitMQ nodes to observe errors/warnings
- Observed connections input/output rates per connections/channels.
  - Lower message rates per connection/channel/stream indicate application levels issues such as out of memory errors
    
- Spring AMQP
  - Default RabbitMQ Template creates a channel per operations.
    - Use a ThreadChannelConnectionFactory when using threadings to efficiently reuse channels in long-lived threads
      ```kotlin 
         @Bean
        fun connectionFactory( ) : ConnectionFactory
        {
        return ThreadChannelConnectionFactory(RabbitConnectionFactoryBean().rabbitConnectionFactory);
        }
      ```
  - Uses an @Async to limit and uses long-lived threads
    - Set max threads spring.
      task.execution.pool.max-size: 8
- This error means stream exist with differenct properties
   - .publisher.RabbitStreamingVehicleSender]: Constructor threw exception; nested exception is com.rabbitmq.stream.StreamException: Error while creating stream 'VehicleStream' (17 (PRECONDITION_FAILED))
- Streaming
  - No no pause - Garabage collection was not starting

**GemFire**

- GemFire pulse uses to get writes per seconds
- Manually delete Persistence Volume claims after delete the service

Issues

- 2021-06-23 10:34:21.724  WARN 1 --- [skStoreMonitor1] o.a.g.internal.cache.DiskStoreMonitor    : The disk volume /workspace/. for log files has exceeded the warning usage threshold and is 99.7% full.

**Performance**

- Pods get Evicted status o
- AMPQ 1 instance 25 vehicles 8 threads 15K GemFire write per second
- NO LOGGING: 1 instance 25 8 htreads 25K GemFire writes per second
- STREAMING 1  instance 25 vehicles 8 threads = 65K writes per second
- Observer memory usages using jconsole locally 
- GemFire
    - Use k9s to connect to a locator
        - gfsh
        - connect
        - describe config --member=<cacheserver>
    - Garbage Collection
      - on the cache server tail -f /data/logsAndStats/gemfire*-server-gc.txt
    - When overriding JVM  these are not need
       -      "-Dgemfire.default.locators=gemfire1-locator-0.gemfire1-locator[10334]"
           - "-Dgemfire.statistic-archive-file=/data/logsAndStats/gemfire1-server.gfs"
    

Breaking point

```shell
NAME                                            READY   STATUS                 RESTARTS   AGE
gemfire1-locator-0                              1/1     Running                0          13h
gemfire1-server-0                               1/1     Running                1          13h
gemfire1-server-1                               1/1     Running                1          13h
gemfire1-server-2                               1/1     Running                0          13h
gemfire1-server-3                               1/1     Running                0          13h
gemfire1-server-4                               1/1     Running                0          13h
gemfire1-server-5                               1/1     Running                0          13h
rabbitmq-server-0                               1/1     Running                0          36h
rabbitmq-server-1                               1/1     Running                2          36h
rabbitmq-server-2                               1/1     Running                2          36h
vehicle-generator-source-ampq-v                 1/1     Running                0          9m43s
vehicle-generator-source-amqp-a                 1/1     Running                0          9m43s
vehicle-generator-source-amqp-b                 1/1     Running                0          9m43s
vehicle-generator-source-amqp-c                 1/1     Running                0          9m43s
vehicle-generator-source-amqp-x                 1/1     Running                0          9m43s
vehicle-generator-source-amqp-y                 1/1     Running                0          9m43s
vehicle-generator-source-amqp-z                 1/1     Running                0          9m43s
vehicle-generator-streaming-source-d            1/1     Running                0          7h40m
vehicle-generator-streaming-source-g            0/1     CreateContainerError   0          8m44s
vehicle-generator-streaming-source-j            1/1     Running                0          8m44s
vehicle-generator-streaming-source-r            1/1     Running                0          7h40m
vehicle-generator-streaming-source-stream-a     0/1     Evicted                0          7h40m
vehicle-generator-streaming-source-stream-b     1/1     Running                0          7h40m
vehicle-generator-streaming-source-stream-c     1/1     Running                0          7h40m
vehicle-generator-streaming-source-v            1/1     Running                0          7h40m
vehicle-streaming-geode-sink-56c5bc59cf-2wxsv   1/1     Running                0          7h15m
vehicle-streaming-geode-sink-56c5bc59cf-4zjh4   1/1     Running                1          13h
vehicle-streaming-geode-sink-56c5bc59cf-674mk   1/1     Running                4          13h
vehicle-streaming-geode-sink-56c5bc59cf-798f5   1/1     Running                1          13h
vehicle-streaming-geode-sink-56c5bc59cf-b64nx   1/1     Running                3          13h
vehicle-streaming-geode-sink-56c5bc59cf-brtd2   1/1     Running                2          13h
vehicle-streaming-geode-sink-56c5bc59cf-fbtzb   1/1     Running                2          13h
vehicle-streaming-geode-sink-56c5bc59cf-fg4d7   1/1     Running                6          13h
vehicle-streaming-geode-sink-56c5bc59cf-frlj4   1/1     Running                2          13h
vehicle-streaming-geode-sink-56c5bc59cf-gcf6j   1/1     Running                2          13h
vehicle-streaming-geode-sink-56c5bc59cf-hqrbn   1/1     Running                3          13h
vehicle-streaming-geode-sink-56c5bc59cf-jx5rb   0/1     Evicted                0          13h
vehicle-streaming-geode-sink-56c5bc59cf-lfxmn   1/1     Running                1          13h
vehicle-streaming-geode-sink-56c5bc59cf-npz9x   1/1     Running                1          13h
vehicle-streaming-geode-sink-56c5bc59cf-psjwl   1/1     Running                2          13h
vehicle-streaming-geode-sink-56c5bc59cf-qmbp5   1/1     Running                3          13h
vehicle-streaming-geode-sink-56c5bc59cf-sfnb5   1/1     Running                2          13h
vehicle-streaming-geode-sink-56c5bc59cf-wkhqw   1/1     Running                3          13h
vehicle-streaming-geode-sink-56c5bc59cf-xxb88   1/1     Running                1          13h
vehicle-streaming-geode-sink-56c5bc59cf-z4bdk   1/1     Running                1          13h
vehicle-streaming-geode-sink-56c5bc59cf-z6d6m   1/1     Running                1          13h
vehicles-geode-sink-76db9f7cf-65xj6             1/1     Running                0          13h
vehicles-geode-sink-76db9f7cf-hq8gb             1/1     Running                0          13h
vehicles-geode-sink-76db9f7cf-kdndk             1/1     Running                0          13h
vehicles-geode-sink-76db9f7cf-qj6qn             1/1     Running                0          13h
vehicles-geode-sink-76db9f7cf-svpmx             1/1     Running                0          13h
```

Client out of memory issues


Resource exhaustion event: the JVM was unable to allocate memory from the heap.
ResourceExhausted! (1/0)
| Instance Count | Total Bytes | Class Name                                                                                                       |
| 11450583       | 1007651304  | Ljava/lang/reflect/Method;                                                                                       |
| 11445928       | 549404544   | Lorg/springframework/aop/framework/ReflectiveMethodInvocation;                                                   |
| 11445928       | 366269696   | Ljava/util/concurrent/FutureTask;                                                                                |
| 11452594       | 275060400   | [Ljava/lang/Object;                                                                                              |
| 11445928       | 274702272   | Lorg/springframework/aop/interceptor/AsyncExecutionInterceptor$$Lambda$982/1036227602;                           |
| 11445923       | 274702152   | Ljava/util/concurrent/LinkedBlockingQueue$Node;                                                                  |
| 56314          | 6353072     | [B                                                                                                               |
| 44616          | 1427712     | Ljava/util/concurrent/ConcurrentHashMap$Node;                                                                    |
| 11657          | 1376376     | Ljava/lang/Class;                                                                                                |
| 49475          | 1187400     | Ljava/lang/String;                                                                                               |
| 3406           | 561432      | [I                                                                                                               |
| 414            | 460160      | [Ljava/util/concurrent/ConcurrentHashMap$Node;                                                                   |
| 28020          | 448320      | Ljava/lang/Object;                                                                                               |
| 9777           | 391080      | Ljava/util/LinkedHashMap$Entry;                                                                                  |
| 4522           | 375744      | [Ljava/util/HashMap$Node;                                                                                        |
| 6196           | 346976      | Ljava/util/LinkedHashMap;                                                                                        |
| 10628          | 340096      | Ljava/util/HashMap$Node;                                                                                         |
| 5047           | 124632      | [Ljava/lang/Class;                                                                                               |
| 2374           | 113952      | Lorg/springframework/core/ResolvableType;                                                                        |
| 2127           | 85080       | Ljava/lang/invoke/MethodType;                                                                                    |
| 438            | 84096       | Lorg/springframework/context/annotation/ConfigurationClassBeanDefinitionReader$ConfigurationClassBeanDefinition; |
| 1476           | 70848       | Lsun/util/locale/LocaleObjectCache$CacheEntry;                                                                   |
| 2153           | 68896       | Ljava/lang/invoke/MethodType$ConcurrentWeakInternSet$WeakEntry;                                                  |
| 1034           | 66176       | Ljava/util/concurrent/ConcurrentHashMap;                                                                         |
| 1346           | 64608       | Ljava/util/HashMap;                                                                                              |
| 1276           | 61248       | Ljava/lang/invoke/MemberName;                                                                                    |
| 2429           | 58296       | Ljava/util/ArrayList;                                                                                            |
| 716            | 57280       | Ljava/lang/reflect/Constructor;                                                                                  |
| 1780           | 56960       | Lsun/security/util/ObjectIdentifier;                                                                             |
| 1418           | 55024       | [Ljava/lang/String;                                                                                              |
| 2074           | 49776       | Ljava/util/concurrent/ConcurrentLinkedDeque$Node;                                                                |
| 1240           | 49600       | Ljava/util/TreeMap$Entry;                                                                                        |
| 880            | 49280       | Lorg/springframework/core/annotation/TypeMappedAnnotation;                                                       |
| 273            | 48048       | Lorg/springframework/beans/factory/support/RootBeanDefinition;                                                   |
| 892            | 42816       | Lorg/springframework/util/ConcurrentReferenceHashMap$SoftEntryReference;                                         |
| 1046           | 41840       | Ljava/lang/ref/SoftReference;                                                                                    |
| 1212           | 38784       | Lsun/security/util/DerInputBuffer;                                                                               |
| 1212           | 38784       | Lsun/security/util/DerValue;                                                                                     |
| 122            | 38728       | [C                                                                                                               |
| 2256           | 36096       | Ljava/util/LinkedHashSet;                                                                                        |
| 871            | 34840       | Ljava/math/BigInteger;                                                                                           |
| 1068           | 34176       | Ljava/lang/ref/ReferenceQueue;                                                                                   |
| 1418           | 34032       | Ljava/util/concurrent/CopyOnWriteArrayList;                                                                      |
| 646            | 31008       | Lch/qos/logback/classic/Logger;                                                                                  |
| 768            | 30944       | [Lorg/springframework/util/ConcurrentReferenceHashMap$Reference;                                                 |
| 768            | 30720       | Lorg/springframework/util/ConcurrentReferenceHashMap$Segment;                                                    |
| 950            | 30400       | Ljava/util/concurrent/locks/ReentrantLock$NonfairSync;                                                           |
| 1212           | 29088       | Lsun/security/util/DerInputStream;                                                                               |
| 725            | 29000       | Ljava/lang/Package$VersionInfo;                                                                                  |
| 420            | 28560       | [Ljava/lang/ref/SoftReference;                                                                                   |
| 1185           | 28440       | [Lsun/security/x509/AVA;                                                                                         |
| 1185           | 28440       | Lsun/security/x509/AVA;                                                                                          |
| 1185           | 28440       | Lsun/security/x509/RDN;                                                                                          |
| 798            | 25536       | Ljava/lang/Package;                                                                                              |
| 786            | 25152       | Ljava/lang/invoke/LambdaForm$Name;                                                                               |
| 737            | 23584       | Lsun/util/locale/BaseLocale$Key;                                                                                 |
| 737            | 23584       | Lsun/util/locale/BaseLocale;                                                                                     |
| 1017           | 23016       | [Lorg/springframework/core/ResolvableType;                                                                       |
| 950            | 22800       | Ljava/util/concurrent/ConcurrentLinkedDeque;                                                                     |
| 282            | 22400       | [Z                                                                                                               |
| 923            | 22152       | Lorg/springframework/core/convert/converter/GenericConverter$ConvertiblePair;                                    |
| 870            | 20880       | Ljava/lang/invoke/ResolvedMethodName;                                                                            |
| 1289           | 20624       | Ljava/util/LinkedHashMap$LinkedKeySet;                                                                           |
| 856            | 20544       | Lorg/springframework/beans/factory/annotation/InjectionMetadata;                                                 |
| 614            | 19648       | Ljava/lang/invoke/LambdaForm$NamedFunction;                                                                      |
| 335            | 18760       | Lsun/security/ssl/CipherSuite;                                                                                   |
| 768            | 18432       | Lorg/springframework/util/ConcurrentReferenceHashMap$ReferenceManager;                                           |
| 751            | 18024       | Lorg/springframework/beans/BeanMetadataAttribute;                                                                |
| 554            | 17728       | Lkotlin/reflect/jvm/internal/impl/name/FqNameUnsafe;                                                             |
| 738            | 17712       | Lorg/apache/commons/logging/LogAdapter$Slf4jLocationAwareLog;                                                    |
| 1070           | 17120       | Ljava/lang/ref/ReferenceQueue$Lock;                                                                              |
| 263            | 16832       | Ljava/net/URL;                                                                                                   |
| 660            | 15840       | Lsun/reflect/annotation/AnnotationInvocationHandler;                                                             |
| 247            | 15808       | Lorg/springframework/core/MethodParameter;                                                                       |
| 949            | 15184       | Lorg/springframework/core/convert/support/GenericConversionService$ConvertersForPair;                            |
| 135            | 15120       | Lorg/springframework/beans/factory/annotation/AnnotatedGenericBeanDefinition;                                    |
| 938            | 15008       | Ljava/util/concurrent/CopyOnWriteArraySet;                                                                       |
| 311            | 14928       | Lsun/security/x509/X500Name;                                                                                     |
| 925            | 14800       | Lorg/springframework/beans/factory/support/MethodOverrides;                                                      |
| 453            | 14496       | Lsun/security/x509/AlgorithmId;                                                                                  |
| 256            | 14336       | Ljava/lang/invoke/MethodTypeForm;                                                                                |
| 350            | 14000       | Ljava/lang/reflect/Parameter;                                                                                    |
| 569            | 13656       | Lorg/springframework/boot/autoconfigure/condition/ConditionEvaluationReport$ConditionAndOutcome;                 |
| 569            | 13656       | Lorg/springframework/boot/autoconfigure/condition/ConditionOutcome;                                              |
| 847            | 13552       | Ljava/util/concurrent/atomic/AtomicInteger;                                                                      |
| 841            | 13456       | Ljava/lang/Integer;                                                                                              |
| 415            | 13280       | Ljava/util/RegularEnumSet;                                                                                       |
| 551            | 13224       | Ljava/security/Provider$ServiceKey;                                                                              |
| 812            | 12992       | Lorg/springframework/core/ResolvableType$DefaultVariableResolver;                                                |
| 263            | 12624       | Ljdk/internal/ref/CleanerImpl$PhantomCleanableRef;                                                               |
| 394            | 12608       | Lorg/springframework/core/convert/support/GenericConversionService$ConverterAdapter;                             |
| 524            | 12576       | Lkotlin/reflect/jvm/internal/impl/name/FqName;                                                                   |
| 314            | 12560       | Ljava/lang/invoke/DirectMethodHandle$Accessor;                                                                   |
| 222            | 12432       | Ljava/security/Provider$Service;                                                                                 |
| 141            | 12176       | [Ljava/util/WeakHashMap$Entry;                                                                                   |
| 151            | 12080       | Lsun/security/x509/X509CertImpl;                                                                                 |
| 179            | 11456       | Ljava/util/zip/Inflater;                                                                                         |
| 475            | 11400       | Ljava/util/Arrays$ArrayList;                                                                                     |
| 345            | 11040       | Ljava/lang/ref/WeakReference;                                                                                    |
| 211            | 10968       | [Ljava/lang/invoke/LambdaForm$Name;