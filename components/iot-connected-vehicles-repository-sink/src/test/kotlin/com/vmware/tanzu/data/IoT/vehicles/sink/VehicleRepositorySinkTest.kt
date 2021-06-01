package com.vmware.tanzu.data.IoT.vehicles.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.function.Function

@ExtendWith(MockitoExtension::class)
internal class VehicleRepositorySinkTest {
    private val vin = "hello";

    @Mock
    private lateinit var mockRepository: VehicleRepository;

    private lateinit var subject: VehicleRepositorySink;

    @Mock
    private lateinit var mockFunction: Function<String,Vehicle>;


    @BeforeEach
    internal fun setUp() {
        subject = VehicleRepositorySink(this.mockRepository) ;
    }

    @Test
    internal fun store() {

        var vehicle = Vehicle(vin);

        subject.accept(vehicle);

        verify(this.mockRepository)!!.save(any(Vehicle::class.java));

    }
}