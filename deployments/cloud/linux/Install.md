sudo yum install java-11-openjdk-devel


## RabbitMQ

See https://www.rabbitmq.com/install-rpm.html

```shell script
sudo vi /etc/yum.repos.d/rabbitmq.repo
yum -q makecache -y --disablerepo='*' --enablerepo='rabbitmq_erlang' --enablerepo='rabbitmq_server'
sudo yum install socat logrotate -y
yum install --repo rabbitmq_erlang --repo rabbitmq_server erlang rabbitmq-server -y
sudo yum install --repo rabbitmq_erlang --repo rabbitmq_server erlang rabbitmq-server -y
```
```shell
sudo su
chkconfig rabbitmq-server on
```

```shell
/sbin/service rabbitmq-server start
```
```shell
/sbin/service rabbitmq-server stop
```

```shell
/sbin/service rabbitmq-server status
```

# Limits

```shell
mkdir -p /etc/systemd/system/rabbitmq-server.service.d
vi /etc/systemd/system/rabbitmq-server.service.d/limits.conf
```

Add the following
```
[Service]
LimitNOFILE=300000
```

## RabbitMQ Commands

```shell
rabbitmq-plugins enable rabbitmq_management rabbitmq_shovel_management rabbitmq_prometheus rabbitmq_mqtt 
```

Disable guest

```shell
rabbitmqctl delete_user guest
```

# GemFire

wget https://apache.claz.org/geode/1.13.7/apache-geode-1.13.7.tgz

