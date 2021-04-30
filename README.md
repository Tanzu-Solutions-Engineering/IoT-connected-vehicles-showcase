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


# Docker

Use the following command to build docker images

```shell script
gradle :applications:vehicle-generator-app:bootBuildImage
gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
gradle :applications:IoT-connected-vehicles-sink:bootBuildImage

```

TODO: What does the prod look like (source providers or vehicle)


# K8 

Locally

```shell script
kind load docker-image vehicle-generator-app:0.0.1-SNAPSHOT
kind load docker-image iot-connected-vehicle-dashboard:0.0.1-SNAPSHOT
kind load docker-image iot-connected-vehicles-sink:0.0.1-SNAPSHOT
```

```shell script
kubectl apply -f cloud/k8/secrets
```

vehicle-secrets

```shell script
kubectl apply -f cloud/k8
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


```shell script
kubectl port-forward rabbitmq-server-0 15672:15672
```

Scale RabbitMQ to 3 nodes

```shell script
kubectl apply -f cloud/k8/data-services/rabbitmq/rabbitmq-cluster-node3.yml
```

Scale GemFire to 2 locator and 3 datanodes

```shell script
kubectl apply -f cloud/k8/data-services/gemfire/gf-cluster-locators-2-datanodes-3.yml
```


k port-forward iot-connected-vehicle-dashboard 7000:7000


### GKE

k apply -f cloud/GKE/k8/iot-connected-vehicle-dashboard.yml

k apply -f cloud/GKE/k8/iot-dashboard-service.yml



# WaveFront

```shell script
helm repo add wavefront https://wavefronthq.github.io/helm/
helm repo update
```

To deploy the Wavefront Collector and Wavefront Proxy:

Using helm 2:

helm install wavefront/wavefront --name wavefront --set wavefront.url=https://YOUR_CLUSTER.wavefront.com --set wavefront.token=YOUR_API_TOKEN --set clusterName=<YOUR_CLUSTER_NAME> --namespace wavefront

Using helm 3:

kubectl create namespace wavefront

```shell script

```
helm install wavefront wavefront/wavefront --set wavefront.url=https://YOUR_CLUSTER.wavefront.com --set wavefront.token=YOUR_API_TOKEN --set clusterName=<YOUR_CLUSTER_NAME> --namespace wavefront


[Local WaveFront](https://vmware.wavefront.com/dashboards/integration-kubernetes-clusters#_v01(g:(d:7200,ls:!t,s:1617894218,w:'2h'),p:(cluster_name:(v:gregoryg-cluster))))

Look for gregoryg-cluster



Return generate 

k delete pod vehicle-generator-app

## Streaming


mvn -Dmaven.test.skip=true install

git remote -v
origin	https://github.com/rabbitmq/rabbitmq-stream-java-client.git (fetch)
origin	https://github.com/rabbitmq/rabbitmq-stream-java-client.git (push)