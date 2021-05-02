package com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author Gregory Green
 */
class MqttVehicleMessageListener(
    private val converter: Function<ByteArray, Vehicle>,
    private val consumer: Consumer<Vehicle>) : IMqttMessageListener {
    /**
     * @param topic the MQTT topic
     * @param msg the message
     */
    override fun messageArrived(topic: String?, msg: MqttMessage?) {
        if(msg == null)
            return;

        var vehicle = converter.apply(msg.payload);
        consumer.accept(vehicle)
    }
}