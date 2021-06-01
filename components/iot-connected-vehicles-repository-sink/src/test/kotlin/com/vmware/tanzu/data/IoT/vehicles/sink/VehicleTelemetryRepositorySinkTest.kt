package com.vmware.tanzu.data.IoT.vehicles.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import com.vmware.tanzu.data.IoT.vehicles.telemetry.repositories.VehicleTelemetryRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class VehicleTelemetryRepositorySinkTest {

    @Mock
    private lateinit var mockRepository : VehicleTelemetryRepository;

    @Test
    fun accept() {
        var subject = VehicleTelemetryRepositorySink(mockRepository);
        val telemetry = VehicleTelemetry();
        subject.accept(telemetry);
        verify(mockRepository).save(telemetry);

    }
}