# Pre-requisite

k port-forward gemfire1-locator-0 7070:7070 &
kubectl port-forward service/vehicle-dashboards-service 7000:80 &
k port-forward deployment/scdf-server 9393:8080&
k port-forward rabbitmq-server-0 15672:15672 &

export PROJECT_HOME=/Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
export POSTGRES_INSTALL_DIR=/Users/devtools/repositories/RDMS/PostgreSQL/kubernetes/VMware/postgres-for-kubernetes-v1.2.0
export SCDF_HOME=/Users/devtools/integration/scdf/scdf-kubernetes/commericial/spring-cloud-data-flow
export GF_INSTALL=/Users/devtools/repositories/IMDG/gf-kubernetes
export APP_USER=vehicle
export APP_PWD=security

kubectl exec -it postgres-0 -- psql -c "ALTER USER postgres PASSWORD 'CHANGEME'"

watch kubectl get pods

cd $PROJECT_HOME




Register Source and Sink Application
scdf-shell.sh


```shell
app register --name vehicle-generator-source --type source --uri docker:nyla/vehicle-generator-source:0.0.4-SNAPSHOT
app register --name vehicles-geode-sink --type sink --uri docker:nyla/vehicles-geode-sink:0.0.1-SNAPSHOT
```

Define data pipeline
```shell
stream create --name "iot-vehicle" --definition "vehicle-generator-source --server.port=8080 --vehicleCount=10 --messageCount=100000 --distanceIncrements=15 --delayMs=400 --vinPrefix='FLEET-A' | vehicles-geode-sink --spring.cloud.stream.bindings.vehicleGemFireSink-in-0.consumer.concurrency=5 --server.port=8080 --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334]"
```

Deploy the pipeline to Kubernetes
```shell
stream deploy --name iot-vehicle --properties "deployer.vehicle-generator-source.kubernetes.requests.memory=700Mi, deployer.vehicles-geode-sink.kubernetes.requests.memory=1Gi, deployer.vehicle-generator-source.kubernetes.limits.memory=700Mi, deployer.vehicles-geode-sink.kubernetes.limits.memory=1Gi"
```
-------------------
# Running MicroServices at Scale on Kubernetes with Tanzu Data Services

--------------------------------
# Scaling Data Services

## Scale RabbitMQ

Increase RabbitMQ to 3 Nodes
```shell
k apply -f cloud/k8/data-services/rabbitmq/local/rabbitmq-3.yml
```


## Scale GemFire

Increase GemFire to 3 data node cache servers

```shell
kubectl apply -f cloud/k8/data-services/gemfire/local/gf-cluster-locators-1-datanode-3.yml
```


## Scale Dashboard

Increase dashboard applications to 2

```shell
k apply -f cloud/k8/apps/dashboard/scaled/iot-connected-vehicle-dashboard-2.yml
```

## Scale vehicles sink

Increase to 3 sink application instances
```shell
stream scale app instances --count 3 --name iot-vehicle --applicationName vehicles-geode-sink
```

--------------
# Data scale new consumers
## Deployment new vehicle jdbc sink

```shell
app register --name vehicle-telemetry-jdbc-sink --type sink --uri docker:vehicles-jdbc-sink:0.0.1-SNAPSHOT


```shell script
kubectl exec -it postgres-0 -- psql -c "CREATE SCHEMA IF NOT EXISTS vehicle_iot;"
```


kubectl exec -it postgres-0 -- psql -c "CREATE TABLE vehicle_iot.vehicle_telemetry ( telemetry_id SERIAL, latitude float8 null, longitude float8 null , capture_ts TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, odometer int8, speed int4 null, temperature int4 null, vin varchar(255) not NULL, CONSTRAINT vehicle_vpkey PRIMARY KEY (telemetry_id) );"


stream create --name "iot-vehicle-jdbc" --definition ":iot-vehicle > vehicle-telemetry-jdbc-sink --spring.datasource.password=CHANGEME --spring.flyway.enabled=false --spring.datasource.url=jdbc:postgresql://postgres:5432/postgres --spring.datasource.username=postgres  "

stream deploy --name iot-vehicle-jdbc --properties "deployer.vehicle-telemetry-jdbc-sink.kubernetes.requests.memory=700Mi, deployer.vehicle-telemetry-jdbc-sink.kubernetes.limits.memory=700Mi"
```

Show dashboard -> Example of creating new applications



Vehicle speed analytical statistics

```shell script
kubectl exec -it postgres-0 -- psql -c "select count(*), avg(speed), max(speed), min(speed) from vehicle_iot.vehicle_telemetry"
```
