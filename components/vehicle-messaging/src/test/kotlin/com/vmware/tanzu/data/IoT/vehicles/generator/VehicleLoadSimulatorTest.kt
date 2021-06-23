package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * @author Gregory Green
 */
class VehicleLoadSimulatorTest {
    private val delayMs = 10L;
    private lateinit var subject: VehicleLoadSimulator;
    private val distanceIncrements = 1.0;
    private val vehicleCount = 1;
    private val messageCount = 10;
    private lateinit var sender : VehicleSender;

    @BeforeEach
    internal fun setUp() {
        sender = mock<VehicleSender>{};
        subject = VehicleLoadSimulator(sender, vehicleCount, messageCount, distanceIncrements, delayMs,);
    }

    @Test
    internal fun process() {



        val args = "";
        subject.process(args);
        Thread.sleep(400);
        verify(sender, atLeastOnce())
            .send(any<Vehicle>());
    }

    @Test
    internal fun toVin() {
        assertEquals("V1",subject.toVin(1))
    }

    @Test
    internal fun toVin_WithPrefix() {
        subject = VehicleLoadSimulator(sender, vehicleCount, messageCount, distanceIncrements, delayMs, "G",);
        assertEquals("G2",subject.toVin(2))

    }
}