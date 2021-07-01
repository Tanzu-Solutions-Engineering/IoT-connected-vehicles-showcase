package com.vmware.tanzu.data.IoT.vehicles.generator.streaming

import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleGenerator
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleLoadSimulator
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.converter.VehicleToBytes
import com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt.MqttVehicleSender
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@Configuration
@ComponentScan(basePackageClasses = [VehicleLoadSimulator::class, VehicleGenerator::class])
class RabbitmqMqttSenderConfig {

    private val delayMs: Long = 1;
    private val distanceIncrements: Double = 3.0;
    private var messageCount: Int = 20;
    private var vehicleCount: Int = 5;

    @Value("\${mqtt.userName:mqtt}")
    private val userName: String = "mqtt";


    @Value("\${mqtt.userPassword:mqtt}")
    private val userPassword: String = "mqtt";

    @Value("\${spring.application.name:vehicle-generator-mqtt-app}")
    private var publisherId: String = "vehicle-generator-mqtt-app";

    @Value("\${mqtt.connectionUrl:tcp://localhost:1883}")
    private var connectionUrl: String = "tcp://localhost:1883";

    @Value("\${mqtt.topic:vehicleSink}")
    private val topic: String = "vehicleSink";


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