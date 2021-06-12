package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.converter.VehicleToTelemetryTransformer
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class VehicleToTelemetryTransformerTest {

    @Test
    fun apply() {
        var subject = VehicleToTelemetryTransformer();

        val expected = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();
        val actual = subject.apply(expected);
        assertEquals(expected.gpsLocation,actual.gpsLocation);
        assertEquals(expected.vin,actual.vin);
        assertEquals(expected.odometer,actual.odometer);
        assertEquals(expected.temperature,actual.temperature);
    }
}