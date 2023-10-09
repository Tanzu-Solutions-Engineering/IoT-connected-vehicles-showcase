# Setup


## RabbitMQ

```shell
cd /Users/devtools/integration/messaging/rabbit/rabbit-devOps
./start.sh
```

## Geode

```shell
cd /Users/devtools/repositories/IMDG/geode/apache-geode-1.13.7/bin
./gfsh
```


In gfsh

See **datastores/geode/README.md**


```shell
create region --name=test --type=PARTITION
```


### Pulse

```shell
open -n -a "Google Chrome" --args "--new-window" "http://localhost:7070/pulse/clusterDetail.html"
````

User admin/admin

# Applications

## Dashboard

```shell script
cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/iot-connected-vehicle-dashboard/build/libs/iot-connected-vehicle-dashboard-0.0.2-SNAPSHOT.jar --server.port=1000
```

```shell script
open -n -a "Google Chrome" --args "--new-window" "http://localhost:1000"
```


Added record

```shell
query --query="select * from /VehicleAggregation"
```


Eviction after
```shell
query --query="select * from /Vehicle"

```

## Sink

```shell script
cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
 java -jar applications/vehicles-geode-sink/build/libs/vehicles-geode-sink-0.0.4-SNAPSHOT.jar --server.port=-1
```

## Generator

```shell script
cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/vehicle-generator-source/build/libs/vehicle-generator-source-0.0.4-SNAPSHOT.jar --delayMs=5 --server.port--1

```

```shell
open -n -a "Google Chrome" --args "--new-window" "http://localhost:15672"
```
-------------------------
# Tanzu Data Services
## RabbitMQ
### Routing based on keywords


Exchange

```yaml
Exchange: example.vehicle
```

```shell
Queue: example.vehicle.all
Routing Key: #
```


```shell
Queue: example.vehicle.vmware
Routing Key: VMware.#
```

routing_key: AMC1
```json
{
  "id": "AMC1",
  "make": "AMC",
  "model": "CONCORD"
}
```

routing_key: VMware.Tanzu1
```json
{
  "id": "Tanzu1",
  "make": "VMware1",
  "model": "Tanzu1"
}
```

### Streaming

```yaml
Stream: example.vehicle.vmware.stream
Routing Key: VMware.Stream.#
```

routing_key: VMware.Stream.Stream1
```json
{
  "id": "Stream1",
  "make": "VMwareStream1",
  "model": "TanzuStream1"
}
```


### Throughput / scaling

Default

- batch size is 100
- A publisher can have at most 10,000 unconfirmed
- message is 10 bytes
- 20 GB for the maximum size of a stream
- 500 MB Segments

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuRabbitMQ/dev/tanzu-rabbitmq-event-streaming-showcase
java -jar applications/performance/lib/stream-perf-test-0.5.0.jar
```

## Observability
....

---------------------------------------------
# GemFire

Data structure support


### Low sub-millisecond data access

Put String 10 character string, 10000 times with the key is generated in the range of 1 to 20

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/apache-geode-extensions
java -jar applications/geode-perf-test/build/libs/geode-perf-test-0.0.1-SNAPSHOT.jar --action=putString --regionName=test  --threadCount=10  --threadSleepMs=0  --loopCount=10000 --startKeyValue=1 --endKeyValue=20 --valueSize=10
```


Region Get 10,000 times of an entry of the first data entry in the region

```shell
cd /Users/Projects/VMware/Tanzu/TanzuData/TanzuGemFire/dev/apache-geode-extensions
java -jar applications/geode-perf-test/build/libs/geode-perf-test-0.0.1-SNAPSHOT.jar --action=get --regionName=test  --threadCount=10  --threadSleepMs=0  --loopCount=10000
```


### Data expiration


### WAN replication

**Stop all applications and cluster**

**Set Up Cluster 1**

```shell
start locator --name=c1-locator --port=10000 --enable-cluster-configuration=true --J=-Dgemfire.jmx-manager-port=10100  --J=-Dgemfire.remote-locators=localhost[20000] --J=-Dgemfire.distributed-system-id=1
```

```shell
start server --name=c1-server1 --locators=localhost[10000] --server-port=40001
```


```shell
connect --locator=localhost[10000]
```

```shell
create gateway-receiver --start-port=50101 --end-port=50110
```

```shell
create gateway-sender --id=sender-1-to-2 --remote-distributed-system-id=2 --parallel=true
```

```shell
create region --name=test-gw --type=PARTITION_REDUNDANT --gateway-sender-id=sender-1-to-2
```


**Set Up Cluster 2**


```shell
disconnect
```

```shell
start locator --name=c2-locator --port=20000 --enable-cluster-configuration=true --J=-Dgemfire.http-service-port=7020 --J=-Dgemfire.jmx-manager-port=20100 --J=-Dgemfire.remote-locators=localhost[10000]  --J=-Dgemfire.distributed-system-id=2
```

```shell
start server --name=c2-server1 --locators=localhost[20000] --server-port=50001 --use-cluster-configuration=true
```

```shell
connect --locator=localhost[20000]
```

```shell
create gateway-receiver --start-port=50201 --end-port=50210
```

```shell
create gateway-sender --id=sender-2-to-1 --remote-distributed-system-id=1 --parallel=true
```

```shell
create region --name=test-gw --type=PARTITION_REDUNDANT --gateway-sender-id=sender-2-to-1
```

**WAN Testing**


```shell
disconnect
connect --locator=localhost[20000]
```

```shell
put --region=/test-gw --key=2 --value=2
```

```shell
disconnect
connect --locator=localhost[10000]
```

```shell
put --region=/test-gw --key=1 --value=1
```

```shell
put --region=/test-gw --key=2 --value=2
```

```shell
disconnect
connect --locator=localhost[20000]
```


```shell
query --query="select * from /test-gw"
```

```shell
remove --region=/test-gw --key=1
remove --region=/test-gw --key=2
```
