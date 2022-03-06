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
create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION
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
