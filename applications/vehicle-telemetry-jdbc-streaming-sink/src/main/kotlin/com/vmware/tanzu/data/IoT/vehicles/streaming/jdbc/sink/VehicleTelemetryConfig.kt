package com.vmware.tanzu.data.IoT.vehicles.streaming.jdbc.sink

import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.converter.VehicleToTelemetryTransformer
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.BytesToVehicle
import com.vmware.tanzu.data.IoT.vehicles.sink.telemetry.VehicleTelemetryRepositorySink
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

/**
 * @author Gregory Green
 */
@Configuration
@ComponentScan(basePackageClasses = [
    BytesToVehicle::class,
    VehicleTelemetryRepositorySink::class])
class VehicleTelemetryConfig {
    @Bean
    fun vehicleToTelemetryTransformer() : VehicleToTelemetryTransformer
    {
        return VehicleToTelemetryTransformer();
    }
}