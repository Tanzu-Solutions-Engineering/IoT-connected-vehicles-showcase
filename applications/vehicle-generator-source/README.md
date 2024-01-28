## Docker building image

```shell
mvn install
cd applications/vehicle-generator-source
mvn spring-boot:build-image
```

```shell
docker tag vehicle-generator-source:0.0.6-SNAPSHOT cloudnativedata/vehicle-generator-source:0.0.6-SNAPSHOT
docker push cloudnativedata/vehicle-generator-source:0.0.6-SNAPSHOT
```

```shell
k apply -f cloud/GKE/k8/vehicle-generator-app
```


