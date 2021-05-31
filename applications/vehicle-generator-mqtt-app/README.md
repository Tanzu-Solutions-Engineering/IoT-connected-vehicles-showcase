rabbitmq-plugins enable rabbitmq_mqtt

restart server



rabbitmqctl -n rabbit add_user mqtt

rabbitmqctl -n rabbit set_permissions mqtt ".*" ".*" ".*"