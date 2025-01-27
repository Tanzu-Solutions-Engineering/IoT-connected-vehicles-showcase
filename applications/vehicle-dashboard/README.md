# Dashboard

http://localhost:7010


## Starting GemFire

Set GEMFIRE_HOME
```shell
export GEMFIRE_HOME=/Users/devtools/repositories/IMDG/gemfire/vmware-gemfire-10.0.2
```

```shell
./deployments/local/dataServices/gemfire/start.sh
```


# Start Application


```shell
java -jar applications/vehicle-dashboard/target/vehicle-dashboard-0.0.1-SNAPSHOT.jar
```

----------------------------


## Docker building image

```shell
mvn install
cd applications/q
mvn spring-boot:build-image
```

```shell
docker tag vehicle-dashboard:0.0.1-SNAPSHOT cloudnativedata/vehicle-dashboard:0.0.1-SNAPSHOT
docker push cloudnativedata/vehicle-dashboard:0.0.1-SNAPSHOT
```
