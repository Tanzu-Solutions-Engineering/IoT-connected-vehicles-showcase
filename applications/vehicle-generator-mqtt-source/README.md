

RabbitMQ Configurations

rabbitmq-plugins enable rabbitmq_mqtt

restart server



rabbitmqctl -n rabbit add_user mqtt

rabbitmqctl -n rabbit set_permissions mqtt ".*" ".*" ".*"



## Docker Notes


Build docker image

```shell
gradle :applications:vehicle-generator-mqtt-source:bootBuildImage
```

```shell script
docker tag vehicle-generator-mqtt-source:0.0.1-SNAPSHOT nyla/vehicle-generator-mqtt-source:0.0.1-SNAPSHOT 
docker push nyla/vehicle-generator-mqtt-source:0.0.1-SNAPSHOT
```


```shell
k apply -f cloud/GKE/k8/vehicle-generator-app
```


