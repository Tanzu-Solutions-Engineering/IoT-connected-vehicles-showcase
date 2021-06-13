package com.vmware.tanzu.data.IoT.vehicles.generator

import com.rabbitmq.stream.*
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.publisher.RabbitStreamingVehicleSender
import nyla.solutions.core.patterns.creational.Creator
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import java.util.function.Function

/**
 * Test for RabbitStreamingVehicleSender
 * @author Gregory Green
 */
internal class RabbitStreamingVehicleSenderTest {

    private val streamName = "myStream";
    private  var producerBuilder: ProducerBuilder? = null;
    private lateinit var environment : Environment;
    private lateinit var producer : Producer
    private lateinit var streamSetup : StreamSetup;
    private lateinit var subject : RabbitStreamingVehicleSender;
    private lateinit var messageBuilder : MessageBuilder;
    private lateinit var addDataMessageBuilder : MessageBuilder;
    private lateinit var function : Function<Vehicle,ByteArray>;
    private lateinit var envCreator : Creator<Environment>;
    private var vehicle: Vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();

    @BeforeEach
    internal fun setUp() {

        streamSetup = mock<StreamSetup>();

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

        /*
        .name(applicationName)
            .stream(streamName)
            .build();
         */
        producerBuilder = mock<ProducerBuilder>{
            on{ name(anyString())} doReturn it;
            on{ stream(anyString())} doReturn it;
            on{ build()} doReturn producer;
        }

        environment = mock<Environment>{
            on{producerBuilder()} doReturn producerBuilder;
        }

        envCreator = mock<Creator<Environment>>{
            on{create()} doReturn environment;
        }


        subject = RabbitStreamingVehicleSender(envCreator, function,streamName,streamSetup);
    }

    @Test
    fun send() {
        verify(streamSetup).initialize(streamName);

        subject.send(vehicle);
        verify(producer).send(any(),any());
        verify(function).apply(vehicle);
    }
}