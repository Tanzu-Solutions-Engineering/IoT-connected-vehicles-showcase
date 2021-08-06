    kubectl create namespace cert-manager
    helm repo add jetstack https://charts.jetstack.io
    helm repo update
    helm install cert-manager jetstack/cert-manager --namespace cert-manager  --version v1.0.2 --set installCRDs=true
    cd /Users/devtools/repositories/RDMS/PostgreSQL/kubernetes/VMware/postgres-for-kubernetes-v1.1.0
    docker load -i ./images/postgres-instance
    docker load -i ./images/postgres-operator
    docker images "postgres-*"
    gcloud auth configure-docker
    PROJECT=$(gcloud config list core/project --format='value(core.project)')
    REGISTRY="gcr.io/${PROJECT}"
    INSTANCE_IMAGE_NAME="${REGISTRY}/postgres-instance:$(cat ./images/postgres-instance-tag)"

    docker tag $(cat ./images/postgres-instance-id) ${INSTANCE_IMAGE_NAME}
    docker push ${INSTANCE_IMAGE_NAME}

    OPERATOR_IMAGE_NAME="${REGISTRY}/postgres-operator:$(cat ./images/postgres-operator-tag)"
    docker tag $(cat ./images/postgres-operator-id) ${OPERATOR_IMAGE_NAME}
    docker push ${OPERATOR_IMAGE_NAME}

    source ~/.bash_profile
    kubectl create secret docker-registry regsecret --docker-server=https://registry.pivotal.io --docker-username=$HARBOR_USER --docker-password=$HARBOR_PASSWORD

    helm install postgres-operator operator-gke/
    kubectl get all