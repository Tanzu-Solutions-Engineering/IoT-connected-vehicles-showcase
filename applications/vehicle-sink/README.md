## Docker building image

```shell
mvn install
cd applications/vehicle-sink
mvn spring-boot:build-image
```

```shell
docker tag vehicle-sink:0.0.1-SNAPSHOT cloudnativedata/vehicle-sink:0.0.1-SNAPSHOT
docker push cloudnativedata/vehicle-sink:0.0.1-SNAPSHOT
```