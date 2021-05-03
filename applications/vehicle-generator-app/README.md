## Docker Notes


Build docker image

```shell
gradle :applications:vehicle-generator-app:bootBuildImage
```

```shell script
docker tag vehicle-generator-app:0.0.1-SNAPSHOT nyla/vehicle-generator-app:0.0.1-SNAPSHOT 
docker push nyla/vehicle-generator-app:0.0.1-SNAPSHOT
```


```shell
k apply -f cloud/GKE/k8/vehicle-generator-app
```


