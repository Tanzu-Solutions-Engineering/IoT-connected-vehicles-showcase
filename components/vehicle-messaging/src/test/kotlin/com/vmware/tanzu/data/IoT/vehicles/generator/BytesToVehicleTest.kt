package com.vmware.tanzu.data.IoT.vehicles.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.BytesToVehicle
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @author Gregory Green
 */
internal class BytesToVehicleTest {

    @Test
    fun apply() {
        var subject = BytesToVehicle();
        val expected = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();
        val bytes : ByteArray = ObjectMapper().writeValueAsBytes(expected)
        val actual = subject.apply(bytes);
        assertThat(expected).isEqualTo(actual);
    }
}