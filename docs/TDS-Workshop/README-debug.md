# debug help

## gradle related
1. `brew install gradle`
1.  run `gradle build` first
1. `./gradlew clean`
1. `./gradlew`
1. `./gradlew tasks`
1. `./gradlew buildNeeded`
1. `./gradlew buildDependents`
1. `./gradlew build`

## kind related

1. `kubectl config get-contexts`  or    ` kubectx`
1. `kubectl config use-context kind-kind` or `kubectx kind-kind`
1. `kind delete cluster`
1. `kind create cluster` 

## kubenetes related
1. namespace
   1. `kubectl create namespace tds-workshop`
2. configMap
    1. `kubectl get cm -n tds-workshop`
    1. please use `kubectl create -f cloud/k8/apps/config-maps.yml -n tds-workshop` instead of `kubectl create configmap vehicle-configmap --from-file=./cloud/k8/apps/` to create configMap
    1. `kubectl describe configmaps vehicle-configmap -n tds-workshop`
    1. `kubectl delete configmap vehicle-configmap -n tds-workshop`
3. RabbitMQ secret
   1. `kubectl -n tds-workshop get secret rabbitmq-default-user -o yaml`
   1. username and passwd to login RabbiMQ
      1. `echo "USER:" $ruser`
      1. `echo "PASWORD:" $rpwd`
4. secret
    1. `kubectl get secret -n tds-workshop`
    1. `kubectl get secret -n tds-workshop vehicle-secrets -o yaml`
5. GemFire
    1. run GFSH `kubectl -n tds-workshop exec -it gemfire1-locator-0 -- gfsh`
    1. get configMap of GemFire `kubectl get cm gemfire1-config -o yaml -n tds-workshop`
6. pods
   1. `kubectl -n tds-workshop get pods`
   2. `kubectl -n tds-workshop get pods -o wide`
   3. `kubectl -n tds-workshop get all`
   4. `kubectl -n tds-workshop describe pod vehicle-generator-source`
   5. `kubectl -n tds-workshop logs vehicle-generator-source`
   6. `kubectl -n tds-workshop delete pod --all`
   7. `kubectl -n tds-workshop delete pod vehicle-generator-source`
   8. `kubectl -n tds-workshop delete pod vehicles-geode-sink`
   9. `kubectl -n tds-workshop delete pod iot-connected-vehicle-dashboard`
7. cluster
   1. `kind delete cluster` 
8. port forward
   1. `kubectl -n tds-workshop port-forward rabbitmq-server-0 15672:15672 > /tmp/k8s-rabbitMq1.log &`
   1. `kubectl -n tds-workshop port-forward iot-connected-vehicle-dashboard 7000:7000 >/tmp/iot-dashboard1.log&`