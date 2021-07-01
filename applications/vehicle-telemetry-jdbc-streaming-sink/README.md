## Rough Notes Docker

From the directory with the Dockerfile


```shell
gradle :applications:vehicle-telemetry-jdbc-streaming-sink:bootBuildImage
```


```shell script
docker tag vehicle-telemetry-jdbc-streaming-sink:0.0.1-SNAPSHOT nyla/vehicle-telemetry-jdbc-streaming-sink:0.0.1-SNAPSHOT 

docker login
docker push nyla/vehicle-telemetry-jdbc-streaming-sink:0.0.1-SNAPSHOT
```


```shell
k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink
```

