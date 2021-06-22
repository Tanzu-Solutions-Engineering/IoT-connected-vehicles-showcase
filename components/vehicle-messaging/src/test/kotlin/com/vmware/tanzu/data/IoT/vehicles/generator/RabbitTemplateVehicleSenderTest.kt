package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.amqp.RabbitTemplateVehicleSender
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.springframework.amqp.rabbit.core.RabbitTemplate

/**
 * test for RabbitTemplateVehicleSender
 * @author Gregory Green
 */
internal class RabbitTemplateVehicleSenderTest{
    private val routingKey: String = "";
    private val exchange: String = "Vehicle";
    private lateinit var vehicle: Vehicle;
    private lateinit var mockTemplate : RabbitTemplate;
    private lateinit var subject : RabbitTemplateVehicleSender;

    @BeforeEach
    internal fun setUp() {
        mockTemplate = mock<RabbitTemplate>{}
        vehicle = JavaBeanGeneratorCreator
                    .of(Vehicle::class.java).create();

        subject = RabbitTemplateVehicleSender(mockTemplate, exchange,);
    }

    @Test
    internal fun send() {
        subject.send(vehicle);
        verify(mockTemplate).convertAndSend(exchange,routingKey,vehicle);
    }
}