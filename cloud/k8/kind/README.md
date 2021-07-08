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

k port-forward rabbitmq-server-0 5672:5672

#docker run --network edge --name rabbitmqEdge --hostname localhost -it -p 15674:15672  -p  1883:1883 -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream
docker run --network edge --name rabbitmqEdge --hostname localhost -it -p 15674:15672  -p  1883:1883 -p 5554:5554 -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_stream advertised_host localhost -rabbitmq_stream advertised_port 5554 -rabbitmq_stream tcp_listeners [5554]" -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream

docker exec -it rabbitmqEdge bash

rabbitmqctl -n rabbit add_user mqtt

rabbitmqctl -n rabbit set_permissions mqtt "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*"
rabbitmqctl set_user_tags mqtt monitoring

rabbitmqadmin declare queue name=vehicleSink.vehicleRepositorySink queue_type=quorum arguments='{"x-max-length":10000,"x-max-in-memory-bytes":0}'

rabbitmqadmin declare binding source=amq.topic  destination=vehicleSink.vehicleRepositorySink routing_key=#

rabbitmqctl set_parameter shovel dc-shovel  '{"src-protocol": "amqp091", "src-uri": "amqp://", "src-queue": "vehicleSink.vehicleRepositorySink", "dest-protocol": "amqp091", "dest-uri": "amqp://vehicle:security@host.docker.internal", "dest-queue": "vehicleSink.vehicleRepositorySink"}'








# Postgres

k apply -f cloud/k8/data-services/edge/postgres.yml

ALTER USER postgres PASSWORD '<PASSWORD>'


k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink


ALTER ROLE postgres SET search_path TO vehicle_iot;

java -jar applications/vehicle-generator-mqtt-source/build/libs/vehicle-generator-mqtt-source-0.0.1-SNAPSHOT.jar

*Streaming Postgres*

k port-forward rabbitmq-server-0 5552:5552

rabbitmqadmin declare queue name=VehicleStream queue_type=stream arguments='{"x-max-age":"3600s","x-stream-max-segment-size-bytes":500000000, "x-max-length-bytes" : 90000000000}'

rabbitmqadmin declare binding source=amq.topic  destination=VehicleStream routing_key=#


java -jar applications/shovel-streaming-app/build/libs/shovel-streaming-app-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.stream.maxLengthGb=90 --rabbitmq.streaming.routing.input.uris="rabbitmq-stream://mqtt:mqtt@localhost:5554" --rabbitmq.streaming.routing.output.uris="rabbitmq-stream://vehicle:security@localhost:5552" --rabbitmq.streaming.routing.input.stream.name=VehicleStream --rabbitmq.streaming.routing.output.stream.name=VehicleStream

select * from vehicle_iot.vehicle_telemetry




FAQ

- No space on disk 
  
```shell
docker system prune
```