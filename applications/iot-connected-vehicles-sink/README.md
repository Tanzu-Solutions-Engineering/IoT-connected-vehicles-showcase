## Rough Notes Docker Hub

From the directory with the Dockerfile

```shell script
docker tag iot-connected-vehicle-dashboard:0.0.1-SNAPSHOT nyla/iot-connected-vehicle-dashboard:0.0.1-SNAPSHOT 

docker login
docker push nyla/iot-connected-vehicle-dashboard:0.0.1-SNAPSHOT

```