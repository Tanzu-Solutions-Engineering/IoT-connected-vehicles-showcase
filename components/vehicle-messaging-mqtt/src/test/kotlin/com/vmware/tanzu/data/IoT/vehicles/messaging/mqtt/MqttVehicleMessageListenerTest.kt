package com.vmware.tanzu.data.IoT.vehicles.messaging.mqtt

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.function.Consumer
import java.util.function.Function

/**
 * Testing for MqttVehicleMessageListener
 * @author Gregory Green
 */
@ExtendWith(MockitoExtension::class)
internal class MqttVehicleMessageListenerTest {

    private var msg: MqttMessage? = null;
    private val expected : Vehicle = Vehicle(vin = "123");
    private val topic: String? = "topic";
    private lateinit var  subject : MqttVehicleMessageListener;
    private lateinit var  mockConsumer : Consumer<Vehicle> ;
    private lateinit var  converter : Function<ByteArray,Vehicle>;

    @BeforeEach
    internal fun setUp() {
        mockConsumer = mock();
        converter = mock(){
            on {apply(any())} doReturn expected;
            val msg : MqttMessage = mock<MqttMessage>();
        }

        msg = mock(){
            on { payload} doReturn ByteArray(0)
        }
    }

    @Test
    fun messageArrived() {


        var subject = MqttVehicleMessageListener(converter,mockConsumer);


        subject.messageArrived(topic,msg);
        verify(converter).apply(any());
        verify(mockConsumer).accept(expected);
    }
}