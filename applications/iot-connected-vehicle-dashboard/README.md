# iot-connected-vehicle-dashboard

This application provides a map visual of vehicles.


The default port is 7000.


```shell
open http://localhost:7000
```

## Setup

In [Apache Geode](https://geode.apache.org/) gfsh


```shell
start locator --name=locator
configure pdx --read-serialized=true --disk-store
start server --name=server
```


Create regions


```shell
create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION
```


# Kubernetes

k port-forward iot-connected-vehicle-dashboard 7000:7000

k apply -f cloud/GKE/k8/dashboard


## Rough Notes Docker Hub

From the directory with the Dockerfile


```shell
gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
```

```shell script
docker tag iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT nyla/iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT 

docker login
docker push nyla/iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT

```