# vehicle-generator-source


# Start RabbitMq in Docker


```shell
docker network create tanzu
docker run --name rabbitmq01  --network tanzu --rm -e RABBITMQ_MANAGEMENT_ALLOW_WEB_ACCESS=true -p 5672:5672 -p 5552:5552 -p 15672:15672  -p  1883:1883  bitnami/rabbitmq:3.13.1 
```

# Start Application

```shell
java -jar applications/vehicle-generator-source/target/vehicle-generator-source-0.0.6-SNAPSHOT.jar  --spring.rabbitmq.username=user  --spring.rabbitmq.password=bitnami
```

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


