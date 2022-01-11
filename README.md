# IoT-data-showCase


**Use cases**

- Hundreds of thousands of vehicles driving across the globe, 24 hours a day, 7 days a week.
- Need to access aggregated real-time location, driving, behavior data, temperature sensors and more.

**Solution**

- Scalable, resiliency Kubernetes platform based on Tanzu
- Cloud Native Microservices based on Spring
- Reliable messaging deliverable with RabbitMQ
- Low latency data read/write operations with GemFire


![img.png](docs/images/overview.png)

# Project Modules


Applications                                                                        |    Notes
-------------------------------------------------------------------------           |    ----------------------
[geotabgroup-tracking-source](applications/geotabgroup-tracking-source)             |    Any car positioning based Geotab's fleet management streaming source microservice for vehicle details in JSON format
[IoT-connected-vehicle-dashboard](applications/IoT-connected-vehicle-dashboard)     |    GUI interface to views vehicle information
[IoT-connected-vehicles-sink](applications/IoT-connected-vehicles-sink)             |    Microservice streaming sink for storing Vehicle data in GemFire
[randmcnally-triplanning-source](applications/randmcnally-triplanning-source)       |    Trip planninhg details based on Rand McNally fleet management streaming source for vehicle data in XML format
[volvo-safecar-source](applications/volvo-safecar-source)                           |    Safe car Volvo Fleet Management management streaming source for vehicle data in XML format

# Env prerequisite

```
jdk: 11
gradle: 6(if you are not sure which gradle version you are using, please try `gradle -v` and use `./gradlew` instead of `gradle`)
kind
kubectl
cert-manager(<=1.2)
helm(>=version3)

nice to have:
k9s
kubectx
```


# Docker

Use the following command to build docker images

```shell script
gradle :applications:vehicle-generator-source:bootBuildImage
gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
gradle :applications:vehicles-geode-sink:bootBuildImage

```


# K8 

Locally

```shell script
kind load docker-image vehicle-generator-source:0.0.4-SNAPSHOT
kind load docker-image iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT
kind load docker-image vehicles-geode-sink:0.0.4-SNAPSHOT
```

```shell script
kubectl apply -f cloud/k8/secrets
```

vehicle-secrets

```shell script
#kubectl apply -f cloud/k8
kubectl apply -f cloud/k8/iot-connected-vehicle-dashboard.yml -n tds-workshop
kubectl apply -f cloud/k8/apps/source/vehicle-generator-source/vehicle-generator-source.yml -n tds-workshop
kubectl apply -f cloud/k8/apps/sink/geode-sink/vehicles-geode-sink.yml -n tds-workshop
```



# Accessing K8 Services

```shell
kubectl port-forward iot-connected-vehicle-dashboard 7070:7070
```


## RabbitMQ Access 
Get the RabbitMQ user/password
```shell script
kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"

export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`

echo ""
echo "USER:" $ruser
echo "PASWORD:" $rpwd
```


Add new users

```shell
kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user $APP_USER $APP_PWD

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / $APP_USER ".*" ".*" ".*"

kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags $APP_USER administrator

```


```shell script
kubectl port-forward rabbitmq-server-0 15672:15672
```

Scale RabbitMQ to 3 nodes

```shell script
kubectl apply -f cloud/k8/data-services/rabbitmq/local-cluster-node3.yml
```

Scale GemFire to 2 locator and 3 datanodes

```shell script
kubectl apply -f cloud/k8/data-services/gemfire/gf-cluster-locators-2-datanodes-3.yml
k port-forward iot-connected-vehicle-dashboard 7000:7000
kubectl create -f cloud/k8/apps/config-maps.yml -n tds-workshop
kubectl apply -f cloud/k8/secrets -n tds-workshop
```




### GKE

```shell script
k apply -f cloud/GKE/k8/iot-connected-vehicle-dashboard.yml
k apply -f cloud/GKE/k8/iot-dashboard-service.yml
```



# WaveFront

```shell script
helm repo add wavefront https://wavefronthq.github.io/helm/
helm repo update
```

To deploy the Wavefront Collector and Wavefront Proxy:

Using helm 2:

```shell script
helm install wavefront/wavefront --name wavefront --set wavefront.url=https://YOUR_CLUSTER.wavefront.com --set wavefront.token=YOUR_API_TOKEN --set clusterName=<YOUR_CLUSTER_NAME> --namespace wavefront
```

Using helm 3:

```shell script
kubectl create namespace wavefront
```
`
```shell script
helm install wavefront wavefront/wavefront --set wavefront.url=https://YOUR_CLUSTER.wavefront.com --set wavefront.token=YOUR_API_TOKEN --set clusterName=<YOUR_CLUSTER_NAME> --namespace wavefront
```

[Local WaveFront](https://vmware.wavefront.com/dashboards/integration-kubernetes-clusters#_v01(g:(d:7200,ls:!t,s:1617894218,w:'2h'),p:(cluster_name:(v:gregoryg-cluster))))


