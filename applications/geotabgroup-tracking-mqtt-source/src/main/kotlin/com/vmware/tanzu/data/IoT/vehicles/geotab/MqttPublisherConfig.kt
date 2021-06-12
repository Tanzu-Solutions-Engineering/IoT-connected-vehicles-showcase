package com.vmware.tanzu.data.IoT.vehicles.geotab

import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.converter.VehicleToBytes
import com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt.MqttVehicleSender
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class MqttPublisherConfig {

    @Value("\${mqtt.userName:mqtt}")
    private val userName: String = "mqtt";


    @Value("\${mqtt.userPassword:mqtt}")
    private val userPassword: String = "mqtt";

    @Value("\${spring.application.name:geotabgroups-tracking-source}")
    private var publisherId: String = "geotabgroups-tracking-source";

    @Value("\${mqtt.connectionUrl:tcp://localhost:2883}")
    private var connectionUrl: String = "tcp://localhost:2883";

    @Value("\${mqtt.topic:vehicleSink.vehicleSink}")
    private val topic: String = "vehicleSink.vehicleSink";


    @Bean
    fun sender() : VehicleSender
    {
        val publisher: IMqttClient = MqttClient(connectionUrl, publisherId, MemoryPersistence())

        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        options.connectionTimeout = 10
        options.userName = userName
        options.password = userPassword.toCharArray()

        publisher.connect(options)

        return MqttVehicleSender(topic,publisher, VehicleToBytes());
    }

}