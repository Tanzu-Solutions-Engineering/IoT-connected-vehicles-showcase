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


**GemFire**

- GemFire pulse uses to get writes per seconds
- Manually delete Persistence Volume claims after delete the service

Issues

- 2021-06-23 10:34:21.724  WARN 1 --- [skStoreMonitor1] o.a.g.internal.cache.DiskStoreMonitor    : The disk volume /workspace/. for log files has exceeded the warning usage threshold and is 99.7% full.

**Performance**

- Pods get Evicted status o
- AMPQ 1 instance 25 vehicles 8 threads 15K GemFire write per second

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