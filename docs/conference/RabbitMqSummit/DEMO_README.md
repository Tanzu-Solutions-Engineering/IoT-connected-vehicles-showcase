# Prerequisite


*Postgres*
Download https://network.pivotal.io/products/tanzu-sql-postgres/
See [Installing Postgres for Kubernetes](https://postgres-kubernetes.docs.pivotal.io/1-1/installing.html)

Example

     kubectl create namespace cert-manager
     helm repo add jetstack https://charts.jetstack.io
     helm repo update
     helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true
    cd /Users/devtools/repositories/RDMS/PostgreSQL/kubernetes/VMware/postgres-for-kubernetes-v1.1.0
    docker load -i ./images/postgres-instance
    docker load -i ./images/postgres-operator
    docker images "postgres-*"
    gcloud auth configure-docker
    PROJECT=$(gcloud config list core/project --format='value(core.project)')
    REGISTRY="gcr.io/${PROJECT}"
    INSTANCE_IMAGE_NAME="${REGISTRY}/postgres-instance:$(cat ./images/postgres-instance-tag)"

    docker tag $(cat ./images/postgres-instance-id) ${INSTANCE_IMAGE_NAME}
    docker push ${INSTANCE_IMAGE_NAME}

    OPERATOR_IMAGE_NAME="${REGISTRY}/postgres-operator:$(cat ./images/postgres-operator-tag)"
    docker tag $(cat ./images/postgres-operator-id) ${OPERATOR_IMAGE_NAME}
    docker push ${OPERATOR_IMAGE_NAME}

    source ~/.bash_profile
    kubectl create secret docker-registry regsecret --docker-server=https://registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

    helm install postgres-operator operator-gke/
    kubectl get all

*Optional in Kind*
kind load  docker-image postgres-instance:v1.1.0
kind load  docker-image postgres-operator:v1.1.0


cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
k apply -f cloud/k8/data-services/postgres/postgres.yml

k port-forward postgres-0 5432:5432

ALTER USER postgres PASSWORD '<PASSWORD>'

ALTER ROLE postgres SET search_path TO vehicle_iot;

----
docker network create  edge


*GemFire*

kubectl apply -f https://github.com/jetstack/cert-manager/releases/download/v1.4.0/cert-manager.yaml
[Install GemFire](https://tgf.docs.pivotal.io/tgf/1-0/install.html)

Example

    kubectl create namespace gemfire-system
    cd /Users/devtools/repositories/IMDG/gf-kubernetes

    kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD
    kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD --namespace gemfire-system

    helm install gemfire-operator gemfire-operator-1.0.1.tgz --namespace gemfire-system
    helm ls --namespace gemfire-system

    cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
    k apply -f cloud/k8/data-services/gemfire/gemfire-gcp.yml

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION"

*RabbitMq*
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
k apply -f cloud/k8/apps/config-maps.yml

k apply -f cloud/k8/apps/secrets/vehicle-secrets.yaml

k apply -f cloud/k8/data-services/rabbitmq/gke-cluster-node3.yml

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user vehicle $UPWD

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / vehicle ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags vehicle monitoring

k port-forward rabbitmq-server-0 15670:15672

SAFARI
open http://localhost:15670

```


* GKE Apps

k apply -f cloud/k8/apps/dashboard
k get services

---------------

**Start Demo**
---------------

k apply -f cloud/k8/apps/sink/geode-sink/gke/vehicles-geode-sink-gke.yml

k apply -f cloud/k8/apps/source/vehicle-generator-source/gke/FLEET-A.yml


k apply -f cloud/k8/apps/source/vehicle-generator-source/gke/FLEET-B.yml



k delete -f cloud/k8/apps/source/vehicle-generator-source/gke







--------------------------------------------------------------------

# RabbitMQ

----

Edge running RabbitMq

k port-forward rabbitmq-server-0 5672:5672

#docker run --network edge --name rabbitmqEdge --hostname localhost -it -p 15674:15672  -p  1883:1883 -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream
#docker run --network edge --name rabbitmqMqttEdge --hostname localhost -it -p 15674:15672  -p  1883:1883 -rabbitmq_stream tcp_listeners [5554]" -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream
docker run --network edge --name rabbitmqMqttEdge --hostname localhost -it -p 15674:15672  -p  1883:1883  -e RABBITMQ_ENABLED_PLUGINS_FILE=/etc/rabbitmq/additional_plugins/enable_plugins -v  /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/cloud/docker/rabbitmq/additional_plugins:/etc/rabbitmq/additional_plugins --rm pivotalrabbitmq/rabbitmq-stream



docker exec -it rabbitmqMqttEdge bash

rabbitmqctl -n rabbit add_user mqtt

rabbitmqctl -n rabbit set_permissions mqtt "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*"
rabbitmqctl set_user_tags mqtt monitoring

rabbitmqadmin declare queue name=vehicleSink.vehicleRepositorySink queue_type=quorum arguments='{"x-max-length":10000,"x-max-in-memory-bytes":0}'

rabbitmqadmin declare binding source=amq.topic  destination=vehicleSink.vehicleRepositorySink routing_key=#

rabbitmqctl set_parameter shovel dc-shovel  '{"src-protocol": "amqp091", "src-uri": "amqp://", "src-queue": "vehicleSink.vehicleRepositorySink", "dest-protocol": "amqp091", "dest-uri": "amqp://vehicle:security@host.docker.internal", "dest-queue": "vehicleSink.vehicleRepositorySink"}'

SAFARI http://localhost:15674


cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/vehicle-generator-mqtt-source/build/libs/vehicle-generator-mqtt-source-0.0.1-SNAPSHOT.jar

chrome http://34.138.55.178/

kill shovel connection


# Postgres streaming

k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink

docker run --network edge --name rabbitmqStreamEdge --hostname localhost -it -p 15676:15672  -p 5554:5554 -e RABBITMQ_SERVER_ADDITIONAL_ERL_ARGS="-rabbitmq_stream advertised_host localhost -rabbitmq_stream advertised_port 5554 -rabbitmq_stream tcp_listeners [5554]" --rm pivotalrabbitmq/rabbitmq-stream

FIRE FOX

http://localhost:15676/



*Streaming Postgres*

k port-forward rabbitmq-server-0 5552:5552

docker exec -it rabbitmqStreamEdge bash

rabbitmqctl -n rabbit add_user streaming streaming

rabbitmqctl -n rabbit set_permissions streaming "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*" "amq.topic|veh.*|mqtt.*|Vehicle.*"
rabbitmqctl set_user_tags streaming monitoring

cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/shovel-streaming-app/build/libs/shovel-streaming-app-0.0.1-SNAPSHOT.jar --rabbitmq.streaming.stream.maxLengthGb=90 --rabbitmq.streaming.routing.input.uris="rabbitmq-stream://streaming:streaming@localhost:5554" --rabbitmq.streaming.routing.output.uris="rabbitmq-stream://vehicle:security@localhost:5552" --rabbitmq.streaming.routing.input.stream.name=VehicleStream --rabbitmq.streaming.routing.output.stream.name=VehicleStream

select avg(speed), max(speed), min(speed),  count(*)  
from vehicle_iot.vehicle_telemetry


cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
java -jar applications/vehicle-generator-streaming-source/build/libs/vehicle-generator-streaming-source-0.0.5-SNAPSHOT.jar --rabbitmq.streaming.uris=rabbitmq-stream://streaming:streaming@localhost:5554 --delayMs=500


Kill connection


select avg(speed), max(speed), min(speed),  count(*)  
from vehicle_iot.vehicle_telemetry


Scale up instances

k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink/vehicle-telemetry-jdbc-streaming-sink.yml

FAQ

- No space on disk

```shell
docker system prune
```
