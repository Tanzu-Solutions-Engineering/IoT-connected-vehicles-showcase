package com.vmware.tanzu.data.IoT.vehicles.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.StringToVehicle
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @author Gregory Green
 */
internal class StringToVehicleTest{
    @Test
    internal fun apply() {
        var subject = StringToVehicle();
        val expected = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();
        val json : String = ObjectMapper().writeValueAsString(expected)
        val actual = subject.apply(json);
        Assertions.assertThat(expected).isEqualTo(actual);

    }
}