## Postgres


**CRD Manager**

kubectl create namespace cert-manager
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true


```shell
cd /Users/devtools/repositories/RDMS/PostgreSQL/kubernetes/VMware/postgres-for-kubernetes-v1.1.0 
docker load -i ./images/postgres-instance
docker load -i ./images/postgres-operator 
docker images "postgres-*"
kubectl create secret docker-registry regsecret --docker-server=https://registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD
kind load  docker-image postgres-instance:v1.1.0
kind load  docker-image postgres-operator:v1.1.0
kind load docker-image gcr.io/gregoryg-playground/postgres-operator:v1.1.0
kind load docker-image gcr.io/gregoryg-playground/postgres-instance:v1.1.0

helm install postgres-operator operator/
cd /Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
k apply -f cloud/k8/data-services/postgres/postgres.yml
```

*RabbitMq*


```shell
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
k apply -f cloud/k8/apps/config-maps.yml
k apply -f cloud/k8/apps/secrets/vehicle-secrets.yaml
k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml

```

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user vehicle security
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / vehicle ".*" ".*" ".*"
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags vehicle monitoring
k port-forward rabbitmq-server-0 15670:15672 

```




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
    k apply -f cloud/k8/data-services/gemfire/gemfire-vmworld.yml

    kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION"



k port-forward pod/gemfire1-locator-0 7070:7070

# Apps

```shell
k apply -f cloud/k8/apps/config-maps.yml
```


## SCDF

```shell
kind load docker-image nyla/vehicle-generator-source:0.0.4-SNAPSHOT
```
```shell
k apply -f cloud/k8/data-services/postgres/postgres.yml
```


kubectl exec -it postgres-0 -- psql

ALTER USER postgres WITH PASSWORD 'security';
exit


cd /Users/devtools/integration/scdf/scdf-kubernetes/commericial
./install.sh
cd /Users/devtools/integration/scdf/scdf-kubernetes/commericial/spring-cloud-data-flow

./bin/install-dev.sh

./bin/uninstall-dev.sh

--monitoring prometheus


k port-forward service/scdf-server 9393:80

Register

    app register --name vehicle-generator-source --type source --uri docker:nyla/vehicle-generator-source:0.0.4-SNAPSHOT
    app register --name vehicles-geode-sink --type sink --uri docker:nyla/vehicles-geode-sink:0.0.1-SNAPSHOT


Create Stream

    stream create --name "iot-vehicle" --definition "vehicle-generator-source --server.port=8080 --vehicleCount=10 --messageCount=100000 --distanceIncrements=1 --delayMs=1 --vinPrefix='FLEET-A' | vehicles-geode-sink --spring.cloud.stream.bindings.vehicleGemFireSink-in-0.consumer.concurrency=5 --server.port=8080 --spring.data.gemfire.pool.locators=gemfire1-locator-0.gemfire1-locator[10334]"
    stream deploy --name iot-vehicle --properties "deployer.vehicle-generator-source.kubernetes.requests.memory=1Gi, deployer.vehicles-geode-sink.kubernetes.requests.memory=1Gi, deployer.vehicle-generator-source.kubernetes.limits.memory=1Gi, deployer.vehicles-geode-sink.kubernetes.limits.memory=1Gi"

  
Deployment dashboard

```shell
k apply -f cloud/k8/apps/dashboard
k port-forward service/vehicle-dashboards-service 7000:80 
```


----

stream scale app instances --name iot-vehicle --applicationName vehicles-geode-sink --count 5
