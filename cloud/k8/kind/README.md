# Prerequisite


----

*GemFire*
[Install GemFire](https://tgf.docs.pivotal.io/tgf/1-0/install.html)

docker network create  edge
1- k apply -f cloud/k8/data-services/edge/gemfire.yml
2- gfsh>create region --name=Vehicle --type=PARTITION


kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
k apply -f cloud/k8/apps/config-maps.yml

k apply -f cloud/k8/apps/secrets/vehicle-secrets.yaml

*Postgres*
helm install postgres-operator operator/
kind load  docker-image postgres-instance:v1.1.0
kind load  docker-image postgres-operator:v1.1.0
k apply -f cloud/k8/data-services/edge/postgres.yml




--------------------------------------------------------------------

# RabbitMQ

k apply -f cloud/k8/data-services/edge/rabbitmq.yml

Add new users

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user vehicle $UPWD

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / vehicle ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags vehicle administrator

```
k port-forward rabbitmq-server-0 15672:15672


Applications

k apply -f cloud/k8/apps/sink/geode-sink/vehicles-geode-sink.yml
k apply -f cloud/k8/apps/dashboard

k apply -f cloud/k8/apps/source/vehicle-generator-source/edge-vehicle.yml


k port-forward iot-connected-vehicle-dashboard 7000:7000

----

Edge running RabbitMq

docker run --network edge --name rabbitmqEdge --hostname localhost -it -p 5672:5672 -p 5552:5552 -p 15674:15672  -p  1883:1883 -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream 

docker exec -it rabbitmqEdge bash

rabbitmqctl -n rabbit add_user mqtt

rabbitmqctl -n rabbit set_permissions mqtt "amq.topic|veh.*|mqtt.*" "amq.topic|veh.*|mqtt.*" "amq.topic|veh.*|mqtt.*"

rabbitmqadmin declare queue name=vehicleSink.vehicleRepositorySink queue_type=quorum arguments='{"x-max-length":10000,"x-max-in-memory-bytes":0}'

rabbitmqadmin declare binding source=amq.topic  destination=vehicleSink.vehicleRepositorySink routing_key=#

rabbitmqctl set_parameter shovel dc-shovel  '{"src-protocol": "amqp091", "src-uri": "amqp://", "src-queue": "vehicleSink.vehicleRepositorySink", "dest-protocol": "amqp091", "dest-uri": "amqp://vehicle:security@host.docker.internal", "dest-queue": "vehicleSink.vehicleRepositorySink"}'


java -jar applications/vehicle-generator-mqtt-source/build/libs/vehicle-generator-mqtt-source-0.0.1-SNAPSHOT.jar


k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink

FAQ

- No space on disk 
  
```shell
docker system prune
```