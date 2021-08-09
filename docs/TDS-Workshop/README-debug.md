# debug help

## gradle
1. `brew install gradle`
1.  run `gradle build` first
1. `./gradlew clean`
1. `./gradlew`
1. `./gradlew tasks`
1. `./gradlew buildNeeded`
1. `./gradlew buildDependents`
1. `./gradlew build`

## kind

1. `kubectl config get-contexts`.    ` kubectx`
1.  `kubectl config use-context kind-kind`.   `kubectx kind-kind`
1. `kind delete cluster`
1. `kind create cluster` 

## kubenetes pods
1. `kubectl logs vehicle-generator-source -n tds-workshop`
1. `kubectl describe pod vehicle-generator-source -n tds-workshop`
1. `kubectl get pods -n tds-workshop`
1. `kubectl -n tds-workshop delete pod --all`
1. `kubectl -n tds-workshop delete pod vehicle-generator-source`
1. `kubectl -n tds-workshop delete pod vehicles-geode-sink`
1. `kubectl -n tds-workshop delete pod iot-connected-vehicle-dashboard`

## kubenetes others
1. namespace
    2. `kubectl create namespace tds-workshop`
1. config map
    1. `kubectl get cm -n tds-workshop`
    1. please use `kubectl create -f cloud/k8/apps/config-maps.yml -n tds-workshop` instead of `kubectl create configmap vehicle-configmap --from-file=./cloud/k8/apps/` to create configMap
    1. `kubectl describe configmaps vehicle-configmap -n tds-workshop`
    1. `kubectl delete configmap vehicle-configmap -n tds-workshop`
1. rabbitmq secret
    1. `kubectl -n tds-workshop get secret rabbitmq-default-user -o yaml`
1. secret
    1. `kubectl get secret -n tds-workshop`
1. GemFire
    1. run GFSH `kubectl -n tds-workshop exec -it gemfire1-locator-0 -- gfsh`
    3. get configMap of GemFire `kubectl get cm gemfire1-config -o yaml -n tds-workshop`