package com.vmware.tanzu.data.IoT.vehicles.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.IVehicle
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import com.vmware.tanzu.data.IoT.vehicles.sink.telemetry.VehicleTelemetryRepositorySink
import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.repositories.VehicleTelemetryRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.function.Function;

@ExtendWith(MockitoExtension::class)
internal class VehicleTelemetryRepositorySinkTest {

    @Mock
    private lateinit var mockRepository : VehicleTelemetryRepository;

    @Mock
    private lateinit var mockTransformer :  Function<IVehicle,VehicleTelemetry>;

    @Test
    fun accept() {

        var subject = VehicleTelemetryRepositorySink(mockRepository,mockTransformer);
        val vehicle = Vehicle();
        subject.accept(vehicle);
        verify(mockTransformer).apply(vehicle);
        verify(mockRepository).save(any());

    }
}