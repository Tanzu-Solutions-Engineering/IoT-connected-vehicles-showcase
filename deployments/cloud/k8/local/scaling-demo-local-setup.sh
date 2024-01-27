#!/bin/bash

export PROJECT_HOME=/Users/Projects/VMware/Tanzu/IoT/dev/IoT-connected-vehicles-showcase
export POSTGRES_INSTALL_DIR=/Users/devtools/repositories/RDMS/PostgreSQL/kubernetes/VMware/postgres-for-kubernetes-v1.2.0
export SCDF_HOME=/Users/devtools/integration/scdf/scdf-kubernetes/commericial/spring-cloud-data-flow
export GF_INSTALL=/Users/devtools/repositories/IMDG/gf-kubernetes
export APP_USER=vehicle
export APP_PWD=security

set -x #echo on

kind create cluster  --config $PROJECT_HOME/cloud/k8/local/k8-1wnode.yaml

# Set GemFire Pre-Requisite
kubectl create namespace cert-manager
helm repo add jetstack https://charts.jetstack.io
helm repo update
helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true
kubectl create namespace gemfire-system


kubectl create secret docker-registry image-pull-secret --namespace=gemfire-system --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

kubectl create secret docker-registry image-pull-secret --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

kubectl create rolebinding psp-gemfire --clusterrole=psp:vmware-system-privileged --serviceaccount=gemfire-system:default

#--------------
# Install the GemFire Operator
#export HELM_EXPERIMENTAL_OCI=1
#helm registry login -u $HARBOR_USER -p $HARBOR_PASSWORD registry.pivotal.io
#helm chart pull registry.pivotal.io/tanzu-gemfire-for-kubernetes/gemfire-operator:1.0.1
#helm chart export registry.pivotal.io/tanzu-gemfire-for-kubernetes/gemfire-operator:1.0.1

sleep 1m

#helm install gemfire-operator gemfire-operator --namespace gemfire-system
kubectl wait pod -l=app.kubernetes.io/component=webhook -n cert-manager --for=condition=Ready --timeout=1m


helm install gemfire-operator $GF_INSTALL/gemfire-operator-1.0.2.tgz --namespace gemfire-system

sleep 30s
kubectl apply -f $PROJECT_HOME/cloud/k8/data-services/gemfire/local/gf-cluster-locators-1-datanode-1.yml


# Install Postgres

docker load -i $POSTGRES_INSTALL_DIR/images/postgres-instance
docker load -i $POSTGRES_INSTALL_DIR/images/postgres-operator
docker images "postgres-*"
export HELM_EXPERIMENTAL_OCI=1
helm registry login registry.pivotal.io --username=$HARBOR_USER --password=$HARBOR_PASSWORD

helm chart pull registry.pivotal.io/tanzu-sql-postgres/postgres-operator-chart:v1.2.0
helm chart export registry.pivotal.io/tanzu-sql-postgres/postgres-operator-chart:v1.2.0  --destination=/tmp/

kubectl create secret docker-registry regsecret \
--docker-server=https://registry.pivotal.io --docker-username=$HARBOR_USER \
--docker-password=$HARBOR_PASSWORD

helm install --wait postgres-operator /tmp/postgres-operator/ --timeout=1m
sleep 30s


kubectl apply -f $PROJECT_HOME/cloud/k8/data-services/postgres
sleep 15s
kubectl wait pod -l=postgres-instance=postgres --for=condition=Ready --timeout=30m
kubectl exec -it postgres-0 -- psql -c "ALTER USER postgres PASSWORD 'CHANGEME'"


# -----------------------
# Rabbit MQ

kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"
kubectl apply -f cloud/k8/data-services/rabbitmq/local/rabbitmq-1.yml
kubectl wait pod -l=app.kubernetes.io/name=rabbitmq --for=condition=Ready --timeout=1m


# --------------------------------
## SCDF

#wget https://github.com/mikefarah/yq/releases/download/3.3.0/yq_darwin_amd64
#mv $PROJECT_HOME/yq_darwin_amd64 /usr/local/sbin/yq
#chmod +x /usr/local/sbin/yq
#
#curl -OL https://github.com/vmware-tanzu/carvel-kbld/releases/download/v0.30.0/kbld-linux-amd64
#sudo mv kbld-linux-amd64 /usr/bin/kbld
#sudo chmod +x /usr/bin/kbld
#
#curl -OL https://github.com/vmware-tanzu/carvel-kapp/releases/download/v0.39.0/kapp-linux-amd64
#sudo mv kapp-linux-amd64 /usr/bin/kapp


#--------------------------
# SCDF

kubectl create secret docker-registry scdf-image-regcred --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD
curl -sL https://github.com/operator-framework/operator-lifecycle-manager/releases/download/v0.18.3/install.sh | bash -s v0.18.3
kubectl create -f https://operatorhub.io/install/prometheus.yaml
kubectl get csv -n operators



kubectl apply -f $SCDF_HOME/services/dev/monitoring

kubectl create secret docker-registry scdf-image-regcred --docker-server=registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

kubectl apply -f $SCDF_HOME/services/dev/postgresql/secret.yaml
kubectl apply -f $SCDF_HOME/services/dev/rabbitmq/config.yaml
kubectl apply -f $SCDF_HOME/services/dev/rabbitmq/secret.yaml

kubectl apply -f $SCDF_HOME/services/dev/skipper.yaml
sleep 15s
kubectl wait pod -l=app=skipper --for=condition=Ready --timeout=1m
kubectl apply -f $SCDF_HOME/services/dev/data-flow.yaml

 sleep 10s
kubectl wait pod -l=app=scdf-server --for=condition=Ready --timeout=1m

#kubectl apply -f $SCDF_HOME/services/dev/monitoring-proxy

--------------------------

# Applications


cd $PROJECT_HOME

#gradle :applications:vehicle-generator-source:bootBuildImage
#gradle :applications:iot-connected-vehicle-dashboard:bootBuildImage
#gradle :applications:vehicles-geode-sink:bootBuildImage
#gradle :applications:vehicles-jdbc-sink:bootBuildImage

# Locally add images to docker

#kind load docker-image vehicle-generator-source:0.0.4-SNAPSHOT &
#kind load docker-image iot-connected-vehicle-dashboard:0.0.2-SNAPSHOT &
#kind load docker-image vehicles-geode-sink:0.0.4-SNAPSHOT &
kind load docker-image vehicles-jdbc-sink:0.0.1-SNAPSHOT &

# Setup K8 Config/Secrets

kubectl apply -f $PROJECT_HOME/cloud/k8/apps/secrets
kubectl apply -f $PROJECT_HOME/cloud/k8/apps/config-maps.yml

# Deployment Dashboard application

kubectl apply -f $PROJECT_HOME/cloud/k8/apps/dashboard

# Expose RabbitMQ Dashboard



# Expose Vehicle Dashboard



k port-forward deployment/scdf-server 9393:8080&
k port-forward rabbitmq-server-0 15672:15672 &

kubectl exec gemfire1-locator-0 -- gfsh -e "connect" -e "create region --name=Vehicle --eviction-action=local-destroy --eviction-max-memory=10000 --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY --enable-statistics=true --type=PARTITION"

# Setup Rabbit


kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user $APP_USER $APP_PWD
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / $APP_USER ".*" ".*" ".*"
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags $APP_USER administrator


kubectl exec rabbitmq-server-0 -- rabbitmqctl add_user app CHANGEME
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_permissions  -p / app ".*" ".*" ".*"
kubectl exec rabbitmq-server-0 -- rabbitmqctl set_user_tags app administrator

kubectl port-forward service/vehicle-dashboards-service 7000:80 &