
## Geode Setup

Gfsh Commands


Time to live is 1 minute.

```shell script
start locator --name=locator
configure pdx --read-serialized=true --disk-store=DEFAULT
start server --name=server1

```


```shell script
deploy --jar=/Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/datastores/geode/libs/nyla.solutions.core-1.4.3.jar

deploy --jar=/Users/Projects/Pivotal/dataTx/IMDG/geode/extensions/dataTx-geode-extensions-core/components/geode-extensions-api/build/libs/dataTx-geode-extensions-core-2.5.0-SNAPSHOT.jar

deploy --jar=/Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase/components/IoT-connected-vehicles-geode/build/libs/IoT-connected-vehicles-geode-0.0.1-SNAPSHOT.jar

create async-event-queue --id=VehicleAggregationQueue --parallel=true --enable-batch-conflation=true --batch-size=80000 --batch-time-interval=5000 --persistent=false --forward-expiration-destroy=true --max-queue-memory=1000 --dispatcher-threads=10 --order-policy=KEY --listener-param=fieldName#speed --listener=com.vmware.tanzu.data.IoT.vehicles.geode.VehicleAggregationAsyncListener


create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION --async-event-queue-id=VehicleAggregationQueue 

create region --name=VehicleAggregation --type=PARTITION  --colocated-with=/Vehicle

```



```shell script

execute function --id=ClearRegionFunction --region=/Vehicle
```

```shell script
show log --member=server1
```

```shell script
query --query="select * from /VehicleAggregation"

```


# K8


```shell script
kubectl exec -it gemfire1-locator-0 -- gfsh

connect --locator=gemfire1-locator[10334]
```




