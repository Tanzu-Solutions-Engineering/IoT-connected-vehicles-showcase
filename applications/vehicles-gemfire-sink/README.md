# vehicles-geode-sink

This application stores consumed messages from RabbitMQ into Apache Geode/GemFire.


Create region in gfsh

```shell
create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION
```

## Rough Notes Docker build

From the directory with the Dockerfile


```shell
gradle :applications:vehicles-geode-sink:bootBuildImage
```


```shell script
docker tag vehicles-geode-sink:0.0.4-SNAPSHOT nyla/vehicles-geode-sink:0.0.4-SNAPSHOT 

docker login
docker push nyla/vehicles-geode-sink:0.0.4-SNAPSHOT
```

## Running in Kubernetes

```shell
k apply -f cloud/GKE/k8/geode-sink
```

