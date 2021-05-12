package com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.function.Function


/**
 * Test for MqttVehicleSender
 * @author Gregory Green
 */
internal class MqttVehicleSenderTest {

//    @Test
    internal fun send() {
        val topic = "topic";
        val expected = Vehicle(vin = "123");
        val mockPublisher: IMqttClient = mock<IMqttClient>();
        var mockConverter: Function<Vehicle, ByteArray> = mock<Function<Vehicle, ByteArray>>()
        {
            on { apply(expected) } doReturn "{}".toByteArray();
        }

        var subject = MqttVehicleSender(topic, mockPublisher, mockConverter);

        subject.send(expected);
        verify(mockPublisher).publish(any(), any());
        verify(mockConverter).apply(any());
    }

    //-----------------------------------------------------------
//    @Test
    fun integrationTest() {
        val publisherId: String = UUID.randomUUID().toString()
        val mqttClient: IMqttClient = MqttClient("tcp://localhost:1883", publisherId, MemoryPersistence())

        val options = MqttConnectOptions()
        options.isAutomaticReconnect = true
        options.isCleanSession = true
        options.connectionTimeout = 10

        mqttClient.connect(options)

        val payload = "{\"vin\": \"123\"}".encodeToByteArray();
        val msg: MqttMessage = MqttMessage(payload);
        msg.qos = 0
        msg.isRetained = true
        val topic = "vehicleSink";

        val receivedSignal = CountDownLatch(10)

        val listener: IMqttMessageListener = IMqttMessageListener { topic: String, msg: MqttMessage ->
                println("TOPIC: ${topic} MSG: ${msg.payload.decodeToString()}")
                receivedSignal.await(1, TimeUnit.MINUTES);
        };
        mqttClient.subscribe(topic, listener);

        sleep(300L)

        mqttClient.publish(topic, msg);

        receivedSignal.await(1, TimeUnit.MINUTES);

    }
}