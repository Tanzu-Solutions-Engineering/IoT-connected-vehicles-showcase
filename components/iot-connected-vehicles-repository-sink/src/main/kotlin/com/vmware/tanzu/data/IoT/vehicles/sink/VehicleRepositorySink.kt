package com.vmware.tanzu.data.IoT.vehicles.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.stereotype.Service
import java.util.function.Consumer


/**
 * @author Gregory Green
 */
@Service
class VehicleRepositorySink(
    private val vehicleRepository: VehicleRepository
) : Consumer<Vehicle> {
    override fun accept(vehicleData: Vehicle) {
          vehicleRepository.save(vehicleData);
    }
}