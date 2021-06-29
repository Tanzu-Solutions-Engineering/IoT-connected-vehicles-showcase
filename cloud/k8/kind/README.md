Prerequisite


k apply -f cloud/k8/data-services/gemfire/gf-cluster-locators-2-datanodes-3.yml
kubectl apply -f "https://github.com/rabbitmq/cluster-operator/releases/latest/download/cluster-operator.yml"


RabbitMQ

k apply -f cloud/k8/data-services/rabbitmq/rabbitmq.yml




FAQ

- No space on disk 
  
```shell
docker system prune
```