

RabbitMQ Configurations

rabbitmq-plugins enable rabbitmq_mqtt

restart server



rabbitmqctl -n rabbit add_user mqtt

rabbitmqctl -n rabbit set_permissions mqtt ".*" ".*" ".*"



## Docker Notes


Build docker image

```shell
gradle :applications:vehicle-generator-source:bootBuildImage
```

```shell script
docker tag vehicle-generator-source:0.0.4-SNAPSHOT nyla/vehicle-generator-source:0.0.4-SNAPSHOT 
docker push nyla/vehicle-generator-source:0.0.4-SNAPSHOT
```


```shell
k apply -f cloud/GKE/k8/vehicle-generator-app
```


