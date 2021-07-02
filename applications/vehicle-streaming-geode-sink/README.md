## Rough Notes Docker

From the directory with the Dockerfile


```shell
gradle :applications:vehicle-streaming-geode-sink:bootBuildImage
```


```shell script
docker tag vehicle-streaming-geode-sink:0.0.6-SNAPSHOT nyla/vehicle-streaming-geode-sink:0.0.6-SNAPSHOT 

docker login
docker push nyla/vehicle-streaming-geode-sink:0.0.6-SNAPSHOT
```


```shell
k apply -f cloud/GKE/k8/geode-sink
```

