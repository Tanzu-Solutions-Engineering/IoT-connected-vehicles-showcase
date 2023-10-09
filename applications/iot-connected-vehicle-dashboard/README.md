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

```shell
start locator --name=locator
start server --name=server1
create region --name=Vehicle --type=PARTITION

```
