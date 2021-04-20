package com.vmware.tanzu.data.IoT.vehicles.generator

import com.rabbitmq.stream.Message
import com.rabbitmq.stream.MessageBuilder
import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.RabbitStreamingVehicleSender
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.function.Function

/**
 * Test for RabbitStreamingVehicleSender
 * @author Gregory Green
 */
internal class RabbitStreamingVehicleSenderTest {

    private lateinit var producer : Producer
    private lateinit var subject : RabbitStreamingVehicleSender;
    private lateinit var messageBuilder : MessageBuilder;
    private lateinit var addDataMessageBuilder : MessageBuilder;
    private lateinit var function : Function<Vehicle,ByteArray>;
    private var vehicle: Vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();

    @BeforeEach
    internal fun setUp() {
        addDataMessageBuilder = mock<MessageBuilder>() { on{build()} doReturn mock<Message>() };
        messageBuilder = mock<MessageBuilder>(){
            on{ addData(any())} doReturn addDataMessageBuilder;
        }
        producer = mock<Producer>()
        {
            on { messageBuilder() } doReturn messageBuilder;
        }
        /*
        .messageBuilder().addData(converter.apply(vehicle)).build();
         */
        function = mock<Function<Vehicle,ByteArray>>(){
            on{apply(any())} doReturn byteArrayOf(0x2E, 0x38);
        };
        subject = RabbitStreamingVehicleSender(producer,function);
    }

    @Test
    fun send() {
        subject.send(vehicle);
        verify(producer).send(any(),any());
        verify(function).apply(vehicle);
    }
}