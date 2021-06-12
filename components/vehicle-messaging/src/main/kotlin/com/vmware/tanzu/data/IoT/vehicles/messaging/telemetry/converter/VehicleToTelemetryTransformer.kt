package com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.converter

import com.vmware.tanzu.data.IoT.vehicles.domains.IVehicle
import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import java.util.function.Function

/**
 * @author Gregory Green
 */
class VehicleToTelemetryTransformer : Function<IVehicle,VehicleTelemetry> {
    /**
     * Applies this function to the given argument.
     *
     * @param vehicle the function argument
     * @return the function result
     */
    override fun apply(vehicle: IVehicle): VehicleTelemetry {
        return VehicleTelemetry(vin = vehicle.vin,
        temperature = vehicle.temperature,
        speed = vehicle.speed,
        odometer = vehicle.odometer,
        gpsLocation = vehicle.gpsLocation);
    }
}