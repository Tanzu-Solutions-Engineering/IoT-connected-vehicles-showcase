# Setup 

## RabbitMQ

```shell
cd /Users/devtools/integration/messaging/rabbit/rabbit-devOps
./start.sh
```

## Geode Pulse
```shell
open -n -a "Google Chrome" --args "--new-window" "http://localhost:7070/pulse/clusterDetail.html"
````

# Applications

## Dashboard

```shell script
java -jar applications/iot-connected-vehicle-dashboard/build/libs/iot-connected-vehicle-dashboard-0.0.1-SNAPSHOT.jar
```

```shell script
open -n -a "Google Chrome" --args "--new-window" "http://localhost:7000"
```

## Sink

```shell script
java -jar applications/iot-connected-vehicles-sink/build/libs/iot-connected-vehicles-sink-0.0.1-SNAPSHOT.jar
```

## Generator

```shell script
java -jar applications/vehicle-generator-app/build/libs/vehicle-generator-app-0.0.1-SNAPSHOT.jar  --delayMs=0
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

## Applications


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

## MQTT

rabbitmq-plugins enable rabbitmq_web_mqtt



TODO


- Everything working in GKE
- Improve replay speed




