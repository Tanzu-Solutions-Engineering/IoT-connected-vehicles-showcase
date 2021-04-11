package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.controller

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VehicleWriteControllerTest{

    @Mock
    private lateinit var repository: VehicleRepository;
    private lateinit var subject: VehicleWriteController;
    private val vehicle: Vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();

    @BeforeEach
    internal fun setUp() {
        subject = VehicleWriteController(repository);
    }

    @Test
    internal fun save() {
        subject.saveVehicle(vehicle);
        Mockito.verify(repository).save(vehicle);

    }
}