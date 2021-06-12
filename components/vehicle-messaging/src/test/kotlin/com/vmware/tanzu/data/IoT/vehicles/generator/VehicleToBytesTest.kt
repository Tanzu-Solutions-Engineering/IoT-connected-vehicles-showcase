package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.converter.VehicleToBytes
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @au
 */
internal class VehicleToBytesTest {

    @Test
    fun apply() {
        val subject = VehicleToBytes();
        val vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();


        var bytes = subject.apply(vehicle);
        var actual = String(bytes);

        assertThat(actual).contains(vehicle.vin);
        assertThat(actual).contains(vehicle.odometer.toString());
        assertThat(actual).contains(vehicle.speed.toString());
        assertThat(actual).contains(vehicle.temperature.toString());
    }
}