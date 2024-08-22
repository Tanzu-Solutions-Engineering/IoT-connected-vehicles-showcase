# vehicle-sink

# Start RabbitMq in Docker


```shell
docker network create tanzu
docker run --name rabbitmq01  --network tanzu --rm -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```


# Start Sink App


```shell
java -jar applications/vehicle-sink/target/vehicle-sink-0.0.1-SNAPSHOT.jar --spring.rabbitmq.username=user  --spring.rabbitmq.password=bitnami
```



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