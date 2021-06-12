package com.vmware.tanzu.data.IoT.vehicles.sink.telemetry

import com.vmware.tanzu.data.IoT.vehicles.domains.IVehicle
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import com.vmware.tanzu.data.IoT.vehicles.messaging.telemetry.repositories.VehicleTelemetryRepository
import org.springframework.stereotype.Service
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author Gregory Green
 */
@Service
class VehicleTelemetryRepositorySink(
    private val repository: VehicleTelemetryRepository,
    private val transformer: Function<IVehicle, VehicleTelemetry>
) : Consumer<Vehicle> {
    /**
     * Performs this operation on the given argument.
     *
     * @param vehicle the input argument
     */
    override fun accept(vehicle: Vehicle) {
        repository.save(transformer.apply(vehicle));
    }
}