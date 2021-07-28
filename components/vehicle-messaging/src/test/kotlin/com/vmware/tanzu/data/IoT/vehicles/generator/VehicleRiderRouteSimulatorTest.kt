package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import nyla.solutions.core.operations.performance.stats.ThroughputStatistics
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

internal class VehicleRiderRouteSimulatorTest{
    private lateinit var subject : VehicleRiderRouteSimulator;

    private lateinit var vehicle : Vehicle;
    private var messageCount = 1;
    private lateinit var sender : VehicleSender;
    private lateinit var generator : VehicleGenerator;
    private lateinit var throughtStats :ThroughputStatistics;
    private var distanceIncrements = 0.1;
    private var delayMs = 0L;

    @BeforeEach
    internal fun setUp() {
        throughtStats = mock<ThroughputStatistics>();
        subject = VehicleRiderRouteSimulator(throughtStats);

        vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();
        messageCount = 1;


        sender = mock<VehicleSender>();
        generator = VehicleGenerator(
             distanceIncrements,vehicle.vin
        )

        distanceIncrements = 0.1;

        delayMs = 0L;
    }


//    @Test
    internal fun ride_sends() {

        subject.ride(vehicle,
            messageCount,
            sender,
            generator,
            distanceIncrements,
            delayMs);
        verify(sender, atLeastOnce()).send(vehicle);
        verify(throughtStats, atLeastOnce()).increment(any());

    }
}