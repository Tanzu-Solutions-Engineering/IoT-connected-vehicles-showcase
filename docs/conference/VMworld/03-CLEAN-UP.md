--------------------
# Cleanup

```shell script
kubectl exec -it postgres-0 -- psql -c "delete from vehicle_iot.vehicle_telemetry"
```

SCDF Shell

```shell
stream destroy --name iot-vehicle-jdbc
stream destroy --name iot-vehicle
