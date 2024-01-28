kubectl create namespace iot-demo
kubectl config set-context --current --namespace=iot-demo
kubectl create secret docker-registry image-pull-secret --docker-server=registry.tanzu.vmware.com --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

kubectl exec -it gemfire-locator-0 -- gfsh -e "connect --locator=gemfire-locator-0.gemfire-locator.iot-demo.svc.cluster.local[10334]" -e "create region --name=Vehicle --type=PARTITION"
kubectl apply -f deployments/cloud/k8/data-services/gemfire/gemfire.yml
kubectl apply -f deployments/cloud/k8/data-services/rabbitmq/rabbitmq.yml
kubectl apply -f deployments/cloud/k8/apps/dashboard/vehicle-dashboard.yml
kubectl apply -f deployments/cloud/k8/apps/sink/vehicle-sink/vehicles-sink.yaml
kubectl apply -f deployments/cloud/k8/apps/source/vehicle-generator-source/vehicle-generator-source.yml
kubectl apply -f deployments/cloud/k8/data-services/gemfire/console/gemfire-mgmt-console.yaml