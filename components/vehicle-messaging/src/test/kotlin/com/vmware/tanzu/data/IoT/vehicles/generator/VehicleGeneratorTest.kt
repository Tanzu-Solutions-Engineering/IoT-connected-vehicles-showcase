package com.vmware.tanzu.data.IoT.vehicles.generator

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.Math.abs

internal class VehicleGeneratorTest{
    private val odometer: Long = 10;
    private val vin  = "Hello";
    private val distanceIncrements  = 0.01;
    private lateinit var subject :VehicleGenerator ;

    @BeforeEach
    internal fun setUp() {
        subject = VehicleGenerator(
            distanceIncrements= distanceIncrements,
            vin= vin);
    }


    @Test
    internal fun constructNoVin() {
        assertTrue(VehicleGenerator(
            distanceIncrements= distanceIncrements).vin.isEmpty());
    }

    @Test
    internal fun vin() {

        subject = VehicleGenerator(
            distanceIncrements= distanceIncrements);

        val excepted = "hello"
        subject.vin = excepted;

        assertEquals(excepted,subject.vin);
    }

    @Test
    internal fun generate() {

        var subject = VehicleGenerator(
            distanceIncrements= distanceIncrements,
            vin= vin);

        var actual = subject.create();

        println("ACTUAL: $actual");
        assertTrue(actual.speed != 0);
        assertTrue(actual.odometer != 0L);
        assertTrue(actual.temperature != 0);
        assertTrue(actual.vin.isNotEmpty());
        assertNotNull(actual.gpsLocation);
        assertNotNull( abs(actual.gpsLocation!!.latitude) > 0);
        assertNotNull( abs(actual.gpsLocation!!.longitude) > 0);
    }

    @Test
    internal fun moveVehicle() {
        var original = subject.create();


        var actual = subject.move(original.copy(),distanceIncrements);
        assertNotEquals(original,actual);

    }
}