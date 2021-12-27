package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.controller

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

/**
 * Test VehicleReadController
 * @author Gregory Green
 */
internal class VehicleReadControllerTest{
    private val vehicle: Vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create()
    private lateinit var repository: VehicleRepository

    @BeforeEach
    internal fun setUp() {
        repository = mock<VehicleRepository>()
    }

    @Test
    internal fun findVehicles() {
        val expected : Iterable<Vehicle> = listOf(vehicle)
        whenever(repository.findAll()).thenReturn(expected)
        var subject = VehicleReadController(repository)

        var actual = subject.findAll();
        assertEquals(expected,actual);
    }
}