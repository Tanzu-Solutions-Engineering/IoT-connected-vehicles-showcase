package com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleSender
import org.eclipse.paho.client.mqttv3.IMqttClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.function.Function

/**
 * @author Gregory Green
 */
class MqttVehicleSender(
    private val topic: String,
    private val publisher: IMqttClient,
    private val converter: Function<Vehicle, ByteArray>
) :VehicleSender {
    override fun send(vehicle: Vehicle) {
        val payload = converter.apply(vehicle);
        var message = MqttMessage(payload);
        publisher.publish(topic,message);
    }
}