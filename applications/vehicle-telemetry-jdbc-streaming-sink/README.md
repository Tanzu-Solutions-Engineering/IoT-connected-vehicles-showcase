## Rough Notes Docker

From the directory with the Dockerfile


```shell
gradle :applications:vehicle-telemetry-jdbc-streaming-sink:bootBuildImage
```


```shell script
docker tag vehicle-telemetry-jdbc-streaming-sink:0.0.2-SNAPSHOT nyla/vehicle-telemetry-jdbc-streaming-sink:0.0.2-SNAPSHOT 

docker login
docker push nyla/vehicle-telemetry-jdbc-streaming-sink:0.0.2-SNAPSHOT
```


k apply -f cloud/k8/data-services/edge/postgres.yml
k port-forward postgres-0 5432:5432


# Postgres

ALTER USER postgres PASSWORD '<PASSWORD>'
ALTER ROLE postgres SET search_path TO vehicle_iot;


# App 


```shell
k apply -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink
```


-----

BACKUP


CREATE USER vehicle WITH PASSWORD '$DBUSER_PW';
GRANT ALL PRIVILEGES ON DATABASE "postgres" to vehicle;
k delete -f cloud/k8/apps/sink/vehicle-telemetry-jdbc-streaming-sink
k delete -f cloud/k8/data-services/edge/postgres.yml