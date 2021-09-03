# Build docker images

Add region in GemFire
 
```shell
kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION"
```


```shell script
gradle :applications:vehicle-generator-source:bootBuildImage
gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
gradle :applications:vehicles-geode-sink:bootBuildImage
gradle :applications:vehicles-jdbc-sink:bootBuildImage
```

Locally

```shell script
kind load docker-image vehicle-generator-source:0.0.4-SNAPSHOT
kind load docker-image iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT
kind load docker-image vehicles-geode-sink:0.0.4-SNAPSHOT
kind load docker-image vehicles-jdbc-sink:0.0.1-SNAPSHOT 
```

```shell script
kubectl apply -f cloud/k8/apps/secrets
kubectl apply -f cloud/k8/apps/config-maps.yml
```

vehicle-secrets

```shell script
#kubectl apply -f cloud/k8
kubectl apply -f cloud/k8/apps/dashboard
```

k port-forward rabbitmq-server-0 15672:15672 &


# Accessing K8 Services

```shell
kubectl port-forward service/vehicle-dashboards-service 7000:80
```


# Prerequisite


See setup script [cloud/k8/local/vmworld-local-setup.sh](cloud/k8/local/vmworld-local-setup.sh)


k port-forward deployment/scdf-server 9393:8080&


```shell
export SCDF_SHELL_HOME=/Users/devtools/integration/scdf/
java -jar $SCDF_SHELL_HOME/spring-cloud-dataflow-shell-*.jar
```


```shell
```
app register --name vehicle-generator-source --type source --uri docker:nyla/vehicle-generator-source:0.0.4-SNAPSHOT
app register --name vehicles-geode-sink --type sink --uri docker:nyla/vehicles-geode-sink:0.0.1-SNAPSHOT
stream create --name "iot-vehicle" --definition "vehicle-generator-source --server.port=8080 --vehicleCount=10 --messageCount=100000 --distanceIncrements=15 --delayMs=400 --vinPrefix='FLEET-A' | vehicles-geode-sink --spring.cloud.stream.bindings.vehicleGemFireSink-in-0.consumer.concurrency=5 --server.port=8080 --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334]"

stream deploy --name iot-vehicle --properties "deployer.vehicle-generator-source.kubernetes.requests.memory=700Mi, deployer.vehicles-geode-sink.kubernetes.requests.memory=1Gi, deployer.vehicle-generator-source.kubernetes.limits.memory=700Mi, deployer.vehicles-geode-sink.kubernetes.limits.memory=1Gi"




# Scale RabbitMQ

k apply -f cloud/k8/data-services/rabbitmq/local/rabbitmq-3.yml


# Scale GemFire

kubectl apply -f cloud/k8/data-services/gemfire/local/gf-cluster-locators-1-datanode-3.yml


## Scale Dashboard

```shell
k apply -f cloud/k8/apps/dashboard/scaled/iot-connected-vehicle-dashboard-2.yml
```


stream scale app instances --name iot-vehicle --applicationName vehicles-geode-sink --count 3


#--------------
# Postgres Apps

```shell
app register --name vehicle-telemetry-jdbc-sink --type sink --uri docker:vehicles-jdbc-sink:0.0.1-SNAPSHOT
```

stream create --name "iot-vehicle-jdbc" --definition ":iot-vehicle > vehicle-telemetry-jdbc-sink --spring.datasource.password=CHANGEME --spring.datasource.url=jdbc:postgresql://postgres:5432/postgres --spring.datasource.username=postgres  "


stream deploy --name iot-vehicle-jdbc --properties "deployer.vehicle-telemetry-jdbc-sink.kubernetes.requests.memory=700Mi, deployer.vehicle-telemetry-jdbc-sink.kubernetes.limits.memory=700Mi"



kubectl exec -it postgres-0 -- psql -c "select count(*), avg(speed), max(speed), min(speed) from vehicle_iot.vehicle_telemetry"


# --------------------
# Cleanup 

```shell script
kubectl exec -it postgres-0 -- psql -c "delete from vehicle_iot.vehicle_telemetry"
```

SCDF Shell

```shell
stream destroy --name iot-vehicle-jdbc
stream destroy --name iot-vehicle
```

