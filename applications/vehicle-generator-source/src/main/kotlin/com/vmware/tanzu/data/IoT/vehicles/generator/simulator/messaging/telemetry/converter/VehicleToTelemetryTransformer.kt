package com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.converter

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * @author Gregory Green
 */
@Component
class VehicleToTelemetryTransformer : Function<Vehicle,VehicleTelemetry> {
    /**
     * Applies this function to the given argument.
     *
     * @param vehicle the function argument
     * @return the function result
     */
    override fun apply(vehicle: Vehicle): VehicleTelemetry {
        return VehicleTelemetry(vehicle)
    }
}