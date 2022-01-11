
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
- Increase young generation [105644.612s][info][gc] GC(80) Pause Young (Allocation Failure) 1876M->27M(6323M) 11.950ms
  [105726.114s][info][gc] GC(81) Pause Young (Allocation Failure) 1875M->27M(6323M) 10.570ms
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



*RabbitMQ Streaming consumers*

- there a at 50000 message batch size



2021-06-25 18:45:58.053  WARN 1 --- [ntLoopGroup-2-5] com.rabbitmq.stream.impl.Client          : Error in stream handler

```
java.lang.OutOfMemoryError: Direct buffer memory
at java.base/java.nio.Bits.reserveMemory(Unknown Source) ~[na:na]
at java.base/java.nio.DirectByteBuffer.<init>(Unknown Source) ~[na:na]
at java.base/java.nio.ByteBuffer.allocateDirect(Unknown Source) ~[na:na]
at io.netty.buffer.UnpooledDirectByteBuf.allocateDirect(UnpooledDirectByteBuf.java:104) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.UnpooledDirectByteBuf.capacity(UnpooledDirectByteBuf.java:156) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.AbstractByteBuf.ensureWritable0(AbstractByteBuf.java:307) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.AbstractByteBuf.ensureWritable(AbstractByteBuf.java:282) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1105) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.codec.ByteToMessageDecoder$1.cumulate(ByteToMessageDecoder.java:99) ~[netty-codec-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:274) ~[netty-codec-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.timeout.IdleStateHandler.channelRead(IdleStateHandler.java:286) ~[netty-handler-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.flush.FlushConsolidationHandler.channelRead(FlushConsolidationHandler.java:152) ~[netty-handler-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:719) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:655) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:581) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989) ~[netty-common-4.1.59.Final.jar:4.1.59.Final]
at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[netty-common-4.1.59.Final.jar:4.1.59.Final]
at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[netty-common-4.1.59.Final.jar:4.1.59.Final]
at java.base/java.lang.Thread.run(Unknown Source) ~[na:na]

2021-06-25 18:46:11.983  WARN 1 --- [ntLoopGroup-2-6] com.rabbitmq.stream.impl.Client          : Error in stream handler

java.lang.OutOfMemoryError: Direct buffer memory
at java.base/java.nio.Bits.reserveMemory(Unknown Source) ~[na:na]
at java.base/java.nio.DirectByteBuffer.<init>(Unknown Source) ~[na:na]
at java.base/java.nio.ByteBuffer.allocateDirect(Unknown Source) ~[na:na]
at io.netty.buffer.UnpooledDirectByteBuf.allocateDirect(UnpooledDirectByteBuf.java:104) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.UnpooledDirectByteBuf.capacity(UnpooledDirectByteBuf.java:156) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.AbstractByteBuf.ensureWritable0(AbstractByteBuf.java:307) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.AbstractByteBuf.ensureWritable(AbstractByteBuf.java:282) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.buffer.AbstractByteBuf.writeBytes(AbstractByteBuf.java:1105) ~[netty-buffer-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.codec.ByteToMessageDecoder$1.cumulate(ByteToMessageDecoder.java:99) ~[netty-codec-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.codec.ByteToMessageDecoder.channelRead(ByteToMessageDecoder.java:274) ~[netty-codec-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.timeout.IdleStateHandler.channelRead(IdleStateHandler.java:286) ~[netty-handler-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.handler.flush.FlushConsolidationHandler.channelRead(FlushConsolidationHandler.java:152) ~[netty-handler-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.fireChannelRead(AbstractChannelHandlerContext.java:357) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.DefaultChannelPipeline$HeadContext.channelRead(DefaultChannelPipeline.java:1410) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:379) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.AbstractChannelHandlerContext.invokeChannelRead(AbstractChannelHandlerContext.java:365) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.DefaultChannelPipeline.fireChannelRead(DefaultChannelPipeline.java:919) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.AbstractNioByteChannel$NioByteUnsafe.read(AbstractNioByteChannel.java:166) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKey(NioEventLoop.java:719) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKeysOptimized(NioEventLoop.java:655) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.processSelectedKeys(NioEventLoop.java:581) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.channel.nio.NioEventLoop.run(NioEventLoop.java:493) ~[netty-transport-4.1.59.Final.jar:4.1.59.Final]
at io.netty.util.concurrent.SingleThreadEventExecutor$4.run(SingleThreadEventExecutor.java:989) ~[netty-common-4.1.59.Final.jar:4.1.59.Final]
at io.netty.util.internal.ThreadExecutorMap$2.run(ThreadExecutorMap.java:74) ~[netty-common-4.1.59.Final.jar:4.1.59.Final]
at io.netty.util.concurrent.FastThreadLocalRunnable.run(FastThreadLocalRunnable.java:30) ~[netty-common-4.1.59.Final.jar:4.1.59.Final]
at java.base/java.lang.Thread.run(Unknown Source) ~[na:na]

2021-06-25 18:46:26.375  WARN 1 --- [ntLoopGroup-2-7] com.rabbitmq.stream.impl.Client          : Error in stream handler




transaction.jta.platform.internal.NoJtaPlatform]
2021-07-07 12:40:41.416  INFO 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2021-07-07 12:40:42.086  WARN 1 --- [           main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'streamingConsumerSpringRunner' defined in URL [jar:file:/workspace/BOOT-INF/lib/vehicle-messaging-streaming-0.0.1-SNAPSHOT.jar!/com/vmware/tanzu/data/IoT/vehicles/messaging/streaming/consumer/StreamingConsumerSpringRunner.class]: Unsatisfied dependency expressed through constructor parameter 4; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'rabbitStreamEnvironmentCreator' defined in URL [jar:file:/workspace/BOOT-INF/lib/messaging-streaming-0.0.1-SNAPSHOT.jar!/com/vmware/tanzu/data/IoT/vehicles/messaging/streaming/creational/RabbitStreamEnvironmentCreator.class]: Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator]: Constructor threw exception; nested exception is java.lang.ExceptionInInitializerError
2021-07-07 12:40:42.087  INFO 1 --- [           main] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2021-07-07 12:40:42.089  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2021-07-07 12:40:42.126  INFO 1 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2021-07-07 12:40:42.129  INFO 1 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2021-07-07 12:40:42.149  INFO 1 --- [           main] ConditionEvaluationReportLoggingListener :

Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2021-07-07 12:40:42.179 ERROR 1 --- [           main] o.s.boot.SpringApplication               : Application run failed

org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'streamingConsumerSpringRunner' defined in URL [jar:file:/workspace/BOOT-INF/lib/vehicle-messaging-streaming-0.0.1-SNAPSHOT.jar!/com/vmware/tanzu/data/IoT/vehicles/messaging/streaming/consumer/StreamingConsumerSpringRunner.class]: Unsatisfied dependency expressed through constructor parameter 4; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'rabbitStreamEnvironmentCreator' defined in URL [jar:file:/workspace/BOOT-INF/lib/messaging-streaming-0.0.1-SNAPSHOT.jar!/com/vmware/tanzu/data/IoT/vehicles/messaging/streaming/creational/RabbitStreamEnvironmentCreator.class]: Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator]: Constructor threw exception; nested exception is java.lang.ExceptionInInitializerError
at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:800) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:229) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1354) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1204) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:564) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:524) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:944) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:917) ~[spring-context-5.3.4.jar:5.3.4]
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:582) ~[spring-context-5.3.4.jar:5.3.4]
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:144) ~[spring-boot-2.4.3.jar:2.4.3]
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) ~[spring-boot-2.4.3.jar:2.4.3]
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) ~[spring-boot-2.4.3.jar:2.4.3]
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) ~[spring-boot-2.4.3.jar:2.4.3]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) ~[spring-boot-2.4.3.jar:2.4.3]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1311) ~[spring-boot-2.4.3.jar:2.4.3]
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1300) ~[spring-boot-2.4.3.jar:2.4.3]
at com.vmware.tanzu.data.IoT.vehicles.streaming.jdbc.sink.VehicleTelemetryStreamingJdbcSinkApplicationKt.main(VehicleTelemetryStreamingJdbcSinkApplication.kt:13) ~[classes/:na]
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(Unknown Source) ~[na:na]
at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(Unknown Source) ~[na:na]
at java.base/java.lang.reflect.Method.invoke(Unknown Source) ~[na:na]
at org.springframework.boot.loader.MainMethodRunner.run(MainMethodRunner.java:49) ~[workspace/:na]
at org.springframework.boot.loader.Launcher.launch(Launcher.java:107) ~[workspace/:na]
at org.springframework.boot.loader.Launcher.launch(Launcher.java:58) ~[workspace/:na]
at org.springframework.boot.loader.JarLauncher.main(JarLauncher.java:88) ~[workspace/:na]
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'rabbitStreamEnvironmentCreator' defined in URL [jar:file:/workspace/BOOT-INF/lib/messaging-streaming-0.0.1-SNAPSHOT.jar!/com/vmware/tanzu/data/IoT/vehicles/messaging/streaming/creational/RabbitStreamEnvironmentCreator.class]: Bean instantiation via constructor failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator]: Constructor threw exception; nested exception is java.lang.ExceptionInInitializerError
at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:315) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.ConstructorResolver.autowireConstructor(ConstructorResolver.java:296) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireConstructor(AbstractAutowireCapableBeanFactory.java:1354) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1204) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:564) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:524) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:335) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:333) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:208) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:276) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1380) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1300) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.ConstructorResolver.resolveAutowiredArgument(ConstructorResolver.java:887) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.ConstructorResolver.createArgumentArray(ConstructorResolver.java:791) ~[spring-beans-5.3.4.jar:5.3.4]
... 28 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator]: Constructor threw exception; nested exception is java.lang.ExceptionInInitializerError
at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:225) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:117) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:311) ~[spring-beans-5.3.4.jar:5.3.4]
... 42 common frames omitted
Caused by: java.lang.ExceptionInInitializerError: null
at com.rabbitmq.stream.impl.StreamEnvironmentBuilder.build(StreamEnvironmentBuilder.java:279) ~[stream-client-0.1.0-SNAPSHOT.jar:0.1.0-SNAPSHOT]
at com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator.<init>(RabbitStreamEnvironmentCreator.kt:24) ~[messaging-streaming-0.0.1-SNAPSHOT.jar:na]
at com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator.<init>(RabbitStreamEnvironmentCreator.kt:21) ~[messaging-streaming-0.0.1-SNAPSHOT.jar:na]
at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:na]
at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(Unknown Source) ~[na:na]
at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(Unknown Source) ~[na:na]
at java.base/java.lang.reflect.Constructor.newInstance(Unknown Source) ~[na:na]
at kotlin.reflect.jvm.internal.calls.CallerImpl$Constructor.call(CallerImpl.kt:41) ~[kotlin-reflect-1.4.30.jar:1.4.30-release-302 (1.4.30)]
at kotlin.reflect.jvm.internal.KCallableImpl.callDefaultMethod$kotlin_reflection(KCallableImpl.kt:173) ~[kotlin-reflect-1.4.30.jar:1.4.30-release-302 (1.4.30)]
at kotlin.reflect.jvm.internal.KCallableImpl.callBy(KCallableImpl.kt:112) ~[kotlin-reflect-1.4.30.jar:1.4.30-release-302 (1.4.30)]
at org.springframework.beans.BeanUtils$KotlinDelegate.instantiateClass(BeanUtils.java:866) ~[spring-beans-5.3.4.jar:5.3.4]
at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:197) ~[spring-beans-5.3.4.jar:5.3.4]
... 44 common frames omitted
Caused by: com.rabbitmq.stream.StreamException: null
at com.rabbitmq.stream.impl.CompressionCodecs.instanciateDefault(CompressionCodecs.java:37) ~[stream-client-0.1.0-SNAPSHOT.jar:0.1.0-SNAPSHOT]
at com.rabbitmq.stream.impl.CompressionCodecs.<clinit>(CompressionCodecs.java:25) ~[stream-client-0.1.0-SNAPSHOT.jar:0.1.0-SNAPSHOT]
... 56 common frames omitted
Caused by: java.lang.reflect.InvocationTargetException: null
at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:na]
at java.base/jdk.internal.reflect.NativeConstructorAccessorImpl.newInstance(Unknown Source) ~[na:na]
at java.base/jdk.internal.reflect.DelegatingConstructorAccessorImpl.newInstance(Unknown Source) ~[na:na]
at java.base/java.lang.reflect.Constructor.newInstance(Unknown Source) ~[na:na]
at com.rabbitmq.stream.impl.CompressionCodecs.instanciateDefault(CompressionCodecs.java:35) ~[stream-client-0.1.0-SNAPSHOT.jar:0.1.0-SNAPSHOT]
... 57 common frames omitted
Caused by: java.lang.NoClassDefFoundError: org/xerial/snappy/SnappyFramedInputStream
at com.rabbitmq.stream.compression.DefaultCompressionCodecFactory.<init>(DefaultCompressionCodecFactory.java:36) ~[stream-client-0.1.0-SNAPSHOT.jar:0.1.0-SNAPSHOT]
... 62 common frames omitted
Caused by: java.lang.ClassNotFoundException: org.xerial.snappy.SnappyFramedInputStream
at java.base/java.net.URLClassLoader.findClass(Unknown Source) ~[na:na]
at java.base/java.lang.ClassLoader.loadClass(Unknown Source) ~[na:na]
at org.springframework.boot.loader.LaunchedURLClassLoader.loadClass(LaunchedURLClassLoader.java:135) ~[workspace/:na]
at java.base/java.lang.ClassLoader.loadClass(Unknown Source) ~[na:na]
... 63 common frames omitted


2021-07-02 22:29:37.358  INFO 1 --- [imer-DEFAULT-90] o.a.g.c.c.i.AutoConnectionSourceImpl     : Communication with locator gemfire1-locator/192.168.9.43:10334 failed

java.net.SocketTimeoutException: Read timed out
	at java.base/java.net.SocketInputStream.socketRead0(Native Method) ~[na:na]
	at java.base/java.net.SocketInputStream.socketRead(Unknown Source) ~[na:na]
	at java.base/java.net.SocketInputStream.read(Unknown Source) ~[na:na]
	at java.base/java.net.SocketInputStream.read(Unknown Source) ~[na:na]
	at java.base/java.net.SocketInputStream.read(Unknown Source) ~[na:na]
	at java.base/java.io.FilterInputStream.read(Unknown Source) ~[na:na]
	at java.base/java.io.DataInputStream.readByte(Unknown Source) ~[na:na]
	at org.apache.geode.internal.InternalDataSerializer.basicReadObject(InternalDataSerializer.java:2493) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.DataSerializer.readObject(DataSerializer.java:2864) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.internal.InternalDataSerializer$1.readObject(InternalDataSerializer.java:301) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.distributed.internal.tcpserver.TcpClient.requestToServer(TcpClient.java:197) ~[geode-tcp-server-1.13.1.jar:na]
	at org.apache.geode.cache.client.internal.AutoConnectionSourceImpl.queryOneLocatorUsingConnection(AutoConnectionSourceImpl.java:217) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.cache.client.internal.AutoConnectionSourceImpl.queryOneLocator(AutoConnectionSourceImpl.java:207) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.cache.client.internal.AutoConnectionSourceImpl.queryLocators(AutoConnectionSourceImpl.java:254) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.cache.client.internal.AutoConnectionSourceImpl.access$200(AutoConnectionSourceImpl.java:68) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.cache.client.internal.AutoConnectionSourceImpl$UpdateLocatorListTask.run2(AutoConnectionSourceImpl.java:457) ~[geode-core-1.13.1.jar:na]
	at org.apache.geode.cache.client.internal.PoolImpl$PoolTask.run(PoolImpl.java:1329) ~[geode-core-1.13.1.jar:na]
	at java.base/java.util.concurrent.Executors$RunnableAdapter.call(Unknown Source) ~[na:na]
	at java.base/java.util.concurrent.FutureTask.runAndReset(Unknown Source) ~[na:na]
	at org.apache.geode.internal.ScheduledThreadPoolExecutorWithKeepAlive$DelegatingScheduledFuture.run(ScheduledThreadPoolExecutorWithKeepAlive.java:276) ~[geode-core-1.13.1.jar:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(Unknown Source) ~[na:na]
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(Unknown Source) ~[na:na]
	at java.base/java.lang.Thread.run(Unknown Source) ~[na:na]

```



