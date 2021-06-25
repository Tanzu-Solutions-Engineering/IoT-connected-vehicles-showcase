package com.vmware.tanzu.data.IoT.vehicles.sink.vehicle

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import nyla.solutions.core.util.Debugger
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.lang.RuntimeException
import java.util.function.Consumer


/**
 * @author Gregory Green
 */
@Service
class VehicleRepositorySink(
    private val vehicleRepository: VehicleRepository
) : Consumer<Vehicle> {
    override fun accept(vehicleData: Vehicle) {
        try {
            vehicleRepository.save(vehicleData);
        }
        catch (e : RuntimeException)
        {
            println("ERROR: Exception $e Vehicle: $vehicleData TRACK: ${Debugger.stackTrace(e)}")
            throw e;
        }

    }
}