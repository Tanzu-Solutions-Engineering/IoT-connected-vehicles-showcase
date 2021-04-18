package com.vmware.tanzu.data.IoT.vehicles.generator.simulator

import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.generator.streaming.VehicleStreamingTask
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.atLeastOnce
import org.mockito.junit.jupiter.MockitoExtension
import java.lang.Thread.sleep
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class VehicleStreamingTaskTest{
    private val delayMs = 10L;
    private lateinit var subject: VehicleStreamingTask;
    private val distanceIncrements = 1.0;
    private val vehicleCount = 1;
    private val messageCount = 10;

    @Mock
    private lateinit var mockQueue: Queue<Vehicle>

    @Mock
    private lateinit var rabbitProducer : Producer;

    @Test
    internal fun run() {

        subject = VehicleStreamingTask(rabbitProducer,
            vehicleCount,messageCount, distanceIncrements,delayMs);

        val args = "";
        subject.run(args);
        sleep(100);
        Mockito.verify(rabbitProducer, atLeastOnce())
            .send(any(),any());
    }
}