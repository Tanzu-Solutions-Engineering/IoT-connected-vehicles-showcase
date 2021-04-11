package com.vmware.tanzu.data.IoT.vehicles.domains

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class VehicleTest{
    @Test
    internal fun id() {
        val expected = "Hi";
        var subject = Vehicle(vin = expected);
        assertEquals(expected,subject.id);

        subject.id ="out";
        assertEquals(subject.id,subject.vin);


    }

    @Test
    internal fun idNull() {
        val expected = "Hi";
        var subject = Vehicle(vin = expected);

        subject.id = null;
        assertEquals(expected, subject.id)
    }
}