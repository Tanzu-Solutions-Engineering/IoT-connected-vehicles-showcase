package com.vmware.tanzu.data.IoT.vehicles.generator

//import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class RabbitStreamingVehicleSenderTest {

//    private lateinit var producer : Producer
    private lateinit var subject : RabbitStreamingVehicleSender;
    private lateinit var vehicle: Vehicle;

    @BeforeEach
    internal fun setUp() {
//        producer = mock<Producer>(){};
//        subject = RabbitStreamingVehicleSender(producer)

    }

//    @Test
//    fun send() {
//        subject.send(vehicle);
//       // verify(producer).send(any(),any());
//    }
}