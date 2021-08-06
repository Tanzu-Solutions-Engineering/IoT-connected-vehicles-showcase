
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install scdf bitnami/spring-cloud-dataflow

kind load docker-image nyla/vehicle-generator-source:0.0.4-SNAPSHOT
docker:nyla/vehicle-generator-source:0.0.4-SNAPSHOT


docker:nyla/vehicles-geode-sink:0.0.1-SNAPSHOT

vehicle-generator-source --server.port=8080 --vehicleCount=10 --messageCount=10000 --distanceIncrements=1 --delayMs=5 --vinPrefix="FLEET-A" | vehicles-geode-sink --spring.cloud.stream.bindings.vehicleGemFireSink-in-0.consumer.concurrency=5 --server.port=8080 --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334]

k apply -f cloud/k8/apps/config-maps.yml

k apply -f cloud/k8/apps/dashboard/scdf


k port-forward iot-connected-vehicle-dashboard 7000:7000



--------------

## Prometheus and Grafana

```shell
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus/prometheus-clusterroles.yaml
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus/prometheus-clusterrolebinding.yaml
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus/prometheus-serviceaccount.yaml
```

```shell
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus-proxy/
```

n the following commands to deploy Prometheus:

```shell
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus/prometheus-configmap.yaml
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus/prometheus-deployment.yaml
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/prometheus/prometheus-service.yaml
```
Grafana:

```shell
kubectl apply -f cloud/k8/data-services/springCloudDataFlow/grafana/
```
--------------
Service account, run the following commands:

```shell
kubectl create -f cloud/k8/data-services/springCloudDataFlow/server/server-roles.yaml
kubectl create -f cloud/k8/data-services/springCloudDataFlow/server/server-rolebinding.yaml
kubectl create -f cloud/k8/data-services/springCloudDataFlow/server/service-account.yaml
```

Deploy Skipper
```shell
kubectl create -f cloud/k8/data-services/springCloudDataFlow/skipper/skipper-config-rabbit.yaml
kubectl create -f cloud/k8/data-services/springCloudDataFlow//skipper/skipper-deployment.yaml
kubectl create -f cloud/k8/data-services/springCloudDataFlow//skipper/skipper-svc.yaml
```
