# Setup 

## Build Applications

```shell
./gradlew build
```


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

```shell
start locator --name=locator
configure pdx --read-serialized=true --disk-store

start server --name=server1
exit
```

### Pulse

```shell
open -n -a "Google Chrome" --args "--new-window" "http://localhost:7070/pulse/clusterDetail.html"
````

User admin/admin

# Applications

## Dashboard

```shell script
java -jar applications/iot-connected-vehicle-dashboard/build/libs/iot-connected-vehicle-dashboard-0.0.2-SNAPSHOT.jar --server.port=1000
```

```shell script
open -n -a "Google Chrome" --args "--new-window" "http://localhost:1000"
```

## Sink

```shell script
 java -jar applications/vehicles-geode-sink/build/libs/vehicles-geode-sink-0.0.4-SNAPSHOT.jar --serverport--1
```

## Generator

```shell script
java -jar applications/vehicle-generator-source/build/libs/vehicle-generator-source-0.0.4-SNAPSHOT.jar --delayMs=5 --serverport--1 

```

```shell
open -n -a "Google Chrome" --args "--new-window" "http://localhost:15672"
```

---------------
# Streaming

## Setup

### Rabbit Streaming
```shell
cd /Users/devtools/integration/messaging/rabbit/rabbitmq-streaming/docker-streaming
./run.sh
```

#### Applications


Streaming GemFire sink

```shell
java -jar applications/iot-connected-vehicles-streaming-geode-sink/build/libs/iot-connected-vehicles-streaming-geode-sink-0.0.1-SNAPSHOT.jar
```

Streaming Generator
```shell
java -jar applications/vehicle-generator-streaming-app/build/libs/vehicle-generator-streaming-app-0.0.1-SNAPSHOT.jar
```

Replay

First Stop Streaming Sink

```shell
java -jar applications/iot-connected-vehicles-streaming-geode-sink/build/libs/iot-connected-vehicles-streaming-geode-sink-0.0.1-SNAPSHOT.jar --replay=true
 ```

```json
{
  "vin": "123",
  "odometer": 0,
  "speed": 0,
  "temperature": 0,
  "gpsLocation": {
    "latitude": 52.10575788433631,
    "longitude": -110.12573484495269
  }
}
```

------------------------

## MQTT edge


- start a rabbitmq for DC
- start a rabbitmq for edge
- enable shovel and MQTT on both

rabbitmq-plugins enable rabbitmq_web_mqtt



TODO


- Everything working in GKE
- Improve replay speed





docker network create edge

docker run -it --hostname rabbitmqedge1 --name rabbitmqedge1 --network edge -e RABBITMQ_ERLANG_COOKIE=12345  -p 25672:15672 -p 2883:1883 -e RABBITMQ_DEFAULT_USER=guest  -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:3-management



```
docker run -it --hostname rabbitmqDC1 --name rabbitmqDC1 --network edge -e RABBITMQ_ERLANG_COOKIE=12345  -p 15672:15672 -p 1883:1883 -e RABBITMQ_DEFAULT_USER=guest  -e RABBITMQ_DEFAULT_PASS=guest rabbitmq:3-management


docker exec -it rabbitmqDC1 bash
rabbitmqctl add_user shovel shovel
rabbitmqctl set_permissions shovel ".*" ".*" ".*"

rabbitmqctl set_user_tags shovel administrator


rabbitmq-plugins enable  rabbitmq_shovel_management


```

```
docker exec -it rabbitmqedge1 bash



rabbitmq-plugins enable rabbitmq_mqtt rabbitmq_shovel_management

rabbitmqctl set_parameter shovel dc1-shovel  '{"src-protocol": "amqp091", "src-uri": "amqp://", "src-queue": "vehicleSink.vehicleSink", "dest-protocol": "amqp091", "dest-uri": "amqp://shovel:shovel@rabbitmqDC1", "dest-queue": "vehicleSink.vehicleSink"}'

```

----------------

# MQTT
docker exec -it rabbitmqDC1 bash
rabbitmqctl add_user mqtt mqtt
rabbitmqctl set_permissions mqtt ".*" ".*" ".*"
rabbitmq-plugins enable rabbitmq_mqtt

rabbitmqadmin declare queue name=vehicleSink.vehicleSink queue_type=quorum

rabbitmqadmin declare binding source=amq.topic  destination=vehicleSink.vehicleSink routing_key=vehicleSink.vehicleSink



docker exec -it rabbitmqedge1 bash
rabbitmqctl add_user mqtt mqtt
rabbitmqctl set_permissions mqtt ".*" ".*" ".*"

rabbitmqadmin declare queue name=vehicleSink.vehicleSink queue_type=quorum

rabbitmqadmin declare binding source=amq.topic  destination=vehicleSink.vehicleSink routing_key=vehicleSink.vehicleSink


