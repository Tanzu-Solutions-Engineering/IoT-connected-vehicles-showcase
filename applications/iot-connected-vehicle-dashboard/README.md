#

http://localhost:7010



# k8

k port-forward iot-connected-vehicle-dashboard 7000:7000

k apply -f cloud/GKE/k8/dashboard


## Rough Notes Docker Hub

From the directory with the Dockerfile


```shell
gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
```

```shell script
docker tag iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT nyla/iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT 

docker login
docker push nyla/iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT

```


Starting GemFire


export GEMFIRE_HOME=/Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-10.0.2

```shell
./deployments/local/dataServices/gemfire/start.sh
```
