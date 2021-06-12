package com.vmware.tanzu.data.IoT.vehicles.mqtt.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.BytesToVehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt.MqttVehicleMessageListener
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Consumer


@Configuration
class MqttConsumerConfig {

    @Value("\${mqtt.userName:mqtt}")
    private val userName: String = "mqtt";


    @Value("\${mqtt.userPassword:mqtt}")
    private val userPassword: String = "mqtt";

    @Value("\${spring.application.name:geotabgroups-tracking-source}")
    private var publisherId: String = "geotabgroups-tracking-source";

    @Value("\${mqtt.connectionUrl:tcp://localhost:1883}")
    private var connectionUrl: String = "tcp://localhost:1883";

    @Value("\${mqtt.topic:vehicleSink}")
    private val topic: String = "vehicleSink";

    @Bean
    fun listener(vehicleConsumer : Consumer<Vehicle>) :IMqttMessageListener
    {
        return MqttVehicleMessageListener(BytesToVehicle(),vehicleConsumer)
    }

    @Bean
    fun runner(listener: IMqttMessageListener): CommandLineRunner
    {

        return CommandLineRunner {

            val mqttClient: IMqttClient = MqttClient(connectionUrl, publisherId, MemoryPersistence())

            val options = MqttConnectOptions()
            options.isAutomaticReconnect = true
            options.isCleanSession = true
            options.connectionTimeout = 10
            options.userName = userName
            options.password = userPassword.toCharArray()

            mqttClient.connect(options)
            mqttClient.subscribe(topic,listener)
        };

    }

}