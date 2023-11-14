package com.vmware.maintence.prediction.interference.ai

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import java.util.function.Consumer

class InferMaintenanceConsumer(private val repository: VehicleRepository) : Consumer<Vehicle> {
    override fun accept(vehicle : Vehicle)  {
        repository.save(vehicle)
    }
}