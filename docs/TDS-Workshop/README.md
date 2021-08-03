# IoT-data-workshop

**Use cases**

- Hundreds of thousands of vehicles driving across the globe, 24 hours a day, 7 days a week.
- Need to access aggregated real-time location, driving, behavior data, temperature sensors and more.

**Solution**

- Scalable, resiliency Kubernetes platform based on Tanzu
- Cloud Native Microservices based on Spring
- Reliable messaging deliverable with RabbitMQ
- Low latency data read/write operations with GemFire
- show telemetry in PostgreSQL


![img.png](../images/overview.png)
![connected_car_demo.png](../images/connected_car_demo.png)

# Project Modules


Applications                                                                        |    Notes
-------------------------------------------------------------------------           |    ----------------------
[IoT-connected-vehicle-dashboard](applications/IoT-connected-vehicle-dashboard)     |    GUI interface to views vehicle information
[vehicles-geode-sink](applications/vehicles-geode-sink)                             |    Microservice sink for storing Vehicle data in GemFire
[vehicle-generator-source](applications/vehicle-generator-source)                   |    Car positioning data generator for vehicle details in JSON format

# Prerequisite

```text
kubernetes env and kubectl（or Docker app with kubernetes)
jdk: 11
gradle: 6(if you are not sure which gradle version you are using, please try `gradle -v` and use `./gradlew` instead of `gradle`)
kind https://kind.sigs.k8s.io/
cert-manager(<=v1.2) https://cert-manager.io/docs/
helm(>=v3) https://helm.sh/
```

nice to have:

```text
k9s https://k9scli.io/
kubectx https://github.com/ahmetb/kubectx
```

# Preparation
1. install operators of VMware Tanzu GemFire for Kubernetes(v1.0), VMware Tanzu RabbitMQ for Kubernetes(v1.1.0) and VMware Tanzu SQL with Postgres for Kubernetes(v1.2) 


Tanzu Products | URL
---------------|--------
VMware Tanzu GemFire for Kubernetes(v1.0)           | https://tgf.docs.pivotal.io/tgf/1-0/index.html
VMware Tanzu RabbitMQ for Kubernetes(v1.1.0)        | https://www.rabbitmq.com/kubernetes/operator/operator-overview.html
VMware Tanzu SQL with Postgres for Kubernetes(v1.2) | https://postgres-kubernetes.docs.pivotal.io/1-2/index.html



1. clean and build local code
   
    ```shell script
    ./gradlew clean
    ./gradlew build
    ```

# Docker images

1. Use the following command to build docker images

    ```shell script
    ./gradlew :applications:vehicle-generator-source:bootBuildImage
    ./gradlew :applications:iot-connected-vehicle-dashboard:bootBuildImage
    ./gradlew :applications:vehicles-geode-sink:bootBuildImage
    
    ```

# K8

1. load docker-image of apps

    ```shell script
    kind load docker-image vehicle-generator-source:0.0.4-SNAPSHOT
    kind load docker-image iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT
    kind load docker-image vehicles-geode-sink:0.0.4-SNAPSHOT
    ```

1. add vehicle-secrets to kubernetes
    ```shell script
    kubectl apply -f cloud/k8/secrets -n tds-workshop
    ```

1. add config-map to kubernetes 
    ```shell script
    kubectl create -f cloud/k8/apps/config-maps.yml -n tds-workshop
    ```
1. deploy pods
   
    ```shell script
    kubectl apply -f cloud/k8/iot-connected-vehicle-dashboard.yml -n tds-workshop
    kubectl apply -f cloud/k8/apps/source/vehicle-generator-source/vehicle-generator-source.yml -n tds-workshop
    kubectl apply -f cloud/k8/apps/sink/geode-sink/vehicles-geode-sink.yml -n tds-workshop
    ```

# Accessing K8 Services



## RabbitMQ Access
1. Get the RabbitMQ user/password
    ```shell script
    kubectl -n tds-workshop get secret rabbitmq-default-user -o jsonpath="{.data.username}"
    
    export ruser=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.username}"| base64 --decode`
    export rpwd=`kubectl get secret rabbitmq-default-user -o jsonpath="{.data.password}"| base64 --decode`
    
    echo ""
    echo "USER:" $ruser
    echo "PASWORD:" $rpwd
    ```

1. start RabbitMQ with 3 nodes

    ```shell script
    kubectl -n tds-workshop apply -f cloud/k8/data-services/rabbitmq/local-cluster-node3.yml
    ```

1. Add new users

    ```shell
    # <user>: vehicle
    # <password>: security
    kubectl -n tds-workshop exec rabbitmq-server-0 -- rabbitmqctl add_user <user> <password>
    
    kubectl -n tds-workshop exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / <user> ".*" ".*" ".*"
    
    kubectl -n tds-workshop exec rabbitmq-server-0 -- rabbitmqctl set_user_tags <user> administrator
    
    ```
4. forward port
    ```shell script
    kubectl -n tds-workshop port-forward rabbitmq-server-0 15672:15672
    ```

## GemFire

1. start GemFire to 2 locator and 3 datanodes

    ```shell script
    kubectl -n tds-workshop apply -f cloud/k8/data-services/gemfire/gf-cluster-locators-2-datanodes-3.yml
    ```

## App
1. forward app port
    ```shell
    kubectl -n tds-workshop port-forward iot-connected-vehicle-dashboard 7070:7070
    ```


# FAQ
1. gradle version 
   
    if you are not sure the gradle version on your env will match this repo,  please use `./gradlew` instead of `gradle`
1. namespace
   
    if you choose to run all the app and data service in a namespace other than default namespace