package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.converter.VehicleToTelemetryTransformer
import com.vmware.tanzu.data.IoT.vehicles.sink.telemetry.VehicleTelemetryRepositorySink
import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.repositories.VehicleTelemetryRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Gregory Green
 */
@Configuration
class AppConfig {
    @Bean
    fun vehicleTelemetryRepositorySink(repository: VehicleTelemetryRepository): VehicleTelemetryRepositorySink {
        return VehicleTelemetryRepositorySink(repository, VehicleToTelemetryTransformer());
    }

}