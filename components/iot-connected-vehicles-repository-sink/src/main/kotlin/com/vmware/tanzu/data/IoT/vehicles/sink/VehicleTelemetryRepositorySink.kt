package com.vmware.tanzu.data.IoT.vehicles.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import com.vmware.tanzu.data.IoT.vehicles.telemetry.repositories.VehicleTelemetryRepository
import org.springframework.stereotype.Service
import java.util.function.Consumer

/**
 * @author Gregory Green
 */
@Service
class VehicleTelemetryRepositorySink(private val repository: VehicleTelemetryRepository) : Consumer<VehicleTelemetry> {
    /**
     * Performs this operation on the given argument.
     *
     * @param vehicleTelemetry the input argument
     */
    override fun accept(vehicleTelemetry: VehicleTelemetry) {
        repository.save(vehicleTelemetry);
    }
}