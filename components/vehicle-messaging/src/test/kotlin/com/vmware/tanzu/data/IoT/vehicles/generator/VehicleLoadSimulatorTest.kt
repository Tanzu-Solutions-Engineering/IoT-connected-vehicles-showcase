package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 *
 * Test for VehicleLoadSimulator
 * @author Gregory Green
 */
class VehicleLoadSimulatorTest {
    private val delayMs = 10L;
    private lateinit var subject: VehicleLoadSimulator;
    private val distanceIncrements = 1.0;
    private val vehicleCount = 1;
    private val messageCount = 10;
    private lateinit var sender : VehicleSender;
    private lateinit var vehicleRider : VehicleRider;

    @BeforeEach
    internal fun setUp() {
        sender = mock<VehicleSender>{};
        vehicleRider = mock<VehicleRider>{};
        subject = VehicleLoadSimulator(sender, vehicleCount, messageCount, distanceIncrements, delayMs,vehicleRider =vehicleRider);
    }

    @Test
    internal fun constructVehicles() {
        var vehicles : Array<Vehicle> = subject.constructVehicles();
        assertEquals(vehicleCount,vehicles.size);
        for (i in vehicles.indices)
        {
            assertEquals("V$i",vehicles[i].vin)
        }
    }

    @Test
    internal fun process() {

        val vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();

        val vehicles : Array<Vehicle> = arrayOf(vehicle);

        subject.process(vehicles);
        Thread.sleep(400);
        verify(vehicleRider, atLeastOnce())
            .ride(any(),any(), any(),any<VehicleGenerator>(), any(), any());

    }

    @Test
    internal fun toVin() {
        assertEquals("V1",subject.toVin(1))
    }

    @Test
    internal fun toVin_WithPrefix() {
        subject = VehicleLoadSimulator(
            sender,
            vehicleCount,
            messageCount,
            distanceIncrements,
            delayMs,
            "G",
            vehicleRider = vehicleRider);
        assertEquals("G2",subject.toVin(2))

    }
}