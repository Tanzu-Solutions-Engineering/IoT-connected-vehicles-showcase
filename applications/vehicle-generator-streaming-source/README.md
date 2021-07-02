

# Docker Notes


```shell
gradle :applications:vehicle-generator-streaming-source:bootBuildImage
```


```shell script
docker tag vehicle-generator-streaming-source:0.0.5-SNAPSHOT nyla/vehicle-generator-streaming-source:0.0.5-SNAPSHOT 

docker login
docker push nyla/vehicle-generator-streaming-source:0.0.5-SNAPSHOT
