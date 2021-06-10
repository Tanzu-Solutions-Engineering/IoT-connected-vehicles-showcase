package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleToTelemetryTransformer
import com.vmware.tanzu.data.IoT.vehicles.sink.VehicleTelemetryRepositorySink
import com.vmware.tanzu.data.IoT.vehicles.telemetry.repositories.VehicleTelemetryRepository
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