package com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * @author Gregory Green
 */
@Component
class MqttVehicleSender(
    @Value("\${mqtt.topic:vehicleSink}")
    private val topic: String,
    private val publisher: IMqttClient,
    private val converter: Function<Vehicle, ByteArray>
) : VehicleSender {
    @Async
    override fun send(vehicle: Vehicle) {
        val payload = converter.apply(vehicle);
        var message = MqttMessage(payload);
        publisher.publish(topic,message);
    }
}