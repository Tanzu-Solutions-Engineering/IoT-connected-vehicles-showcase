## Rough Notes Docker

From the directory with the Dockerfile


```shell
gradle :applications:vehicles-geode-sink:bootBuildImage
```


```shell script
docker tag vehicles-geode-sink:0.0.1-SNAPSHOT nyla/vehicles-geode-sink:0.0.1-SNAPSHOT 

docker login
docker push nyla/vehicles-geode-sink:0.0.1-SNAPSHOT
```


```shell
k apply -f cloud/GKE/k8/geode-sink
```

