# Setup 

## Build Applications

```shell
mvn package
```

## RabbitMQ

```shell
./deployments/local/dataServices/rabbit/rabbit-start.sh
```

## GemFire 

```shell
./deployments/local/dataServices/gemfire/start-docker-gemfire.sh
```

### Pulse

```shell
open http://localhost:7070/pulse/clusterDetail.html
````

User admin/admin

# Applications

## Dashboard

```shell script
java -jar applications/vehicle-dashboard/target/vehicle-dashboard-0.0.1-SNAPSHOT.jar --server.port=1000
```

```shell script
open "http://localhost:1000"
```


## Sink

```shell script
 java -jar applications/vehicle-sink/target/vehicle-sink-0.0.1-SNAPSHOT.jar --server.port=-1 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami
```

## Generator

```shell script
java -jar applications/vehicle-generator-source/target/vehicle-generator-source-0.0.6-SNAPSHOT.jar --delayMs=20 --server.port--1 --spring.rabbitmq.username=user --spring.rabbitmq.password=bitnami

```

```shell
open -n -a "Google Chrome" --args "--new-window" "http://localhost:15672"
```
