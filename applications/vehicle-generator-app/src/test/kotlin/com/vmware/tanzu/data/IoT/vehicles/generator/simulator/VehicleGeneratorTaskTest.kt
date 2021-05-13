package com.vmware.tanzu.data.IoT.vehicles.generator.simulator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.amqp.rabbit.core.RabbitTemplate
import java.lang.Thread.sleep
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class VehicleGeneratorTaskTest{
    private val delayMs = 10L;
    private lateinit var subject: VehicleGeneratorTask;
    private val distanceIncrements = 1.0;
    private val vehicleCount = 1;
    private val messageCount = 10;

    @Mock
    private lateinit var mockQueue: Queue<Vehicle>

    @Mock
    private lateinit var rabbitTemplate : RabbitTemplate;


    @Test
    internal fun process() {

        subject = VehicleGeneratorTask(rabbitTemplate,vehicleCount,messageCount, distanceIncrements,delayMs);

        val args = "";
        subject.process();
        sleep(100);
        Mockito.verify(rabbitTemplate, atLeastOnce())
            .convertAndSend(anyString(),anyString(),any<Object>());
    }
}