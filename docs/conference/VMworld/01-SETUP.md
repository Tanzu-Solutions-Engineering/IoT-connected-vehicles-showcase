# Prerequisite

See setup script [cloud/k8/local/vmworld-local-setup.sh](cloud/k8/local/vmworld-local-setup.sh)


# Pre DEMO setup
## Build docker images

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

Locally add images to docker

```shell script
kind load docker-image vehicle-generator-source:0.0.4-SNAPSHOT
kind load docker-image iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT
kind load docker-image vehicles-geode-sink:0.0.4-SNAPSHOT
kind load docker-image vehicles-jdbc-sink:0.0.1-SNAPSHOT 
```

Setup K8 Config/Secrets

```shell script
kubectl apply -f cloud/k8/apps/secrets
kubectl apply -f cloud/k8/apps/config-maps.yml
```

Deployment Dashboard application

```shell script
kubectl apply -f cloud/k8/apps/d
ashboard
```

Expose RabbitMQ Dashboard

```shell
k port-forward rabbitmq-server-0 15672:15672 &
```

Expose Vehicle Dashboard

```shell
kubectl port-forward service/vehicle-dashboards-service 7000:80
```

```shell
k port-forward deployment/scdf-server 9393:8080&
```

```shell
scdf-shell.sh
```