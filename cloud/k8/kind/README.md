Prerequisite

docker network create  edge
1- k apply -f cloud/k8/data-services/edge/gemfire.yml
2- gfsh>create region --name=Vehicle --type=PARTITION


kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
k apply -f cloud/k8/apps/config-maps.yml
k apply -f cloud/k8/apps/secrets/vehicle-secrets.yaml


RabbitMQ

k apply -f cloud/k8/data-services/edge/rabbitmq.yml

Add new users

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user vehicle <password>

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / vehicle ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags vehicle administrator

```
k port-forward rabbitmq-server-0 15672:15672


Applications

k apply -f cloud/k8/apps/sink/geode-sink/vehicles-geode-sink.yml
k apply -f cloud/k8/apps/source/vehicle-generator-source/edge-vehicle.yml


----

Edge running RabbitMq

docker run --network edge --name rabbitmqEdge --hostname localhost -it -p 5672:5672 -p 5552:5552 -p 15674:15672  -p  1883:1883 --rm pivotalrabbitmq/rabbitmq-stream



FAQ

- No space on disk 
  
```shell
docker system prune
```