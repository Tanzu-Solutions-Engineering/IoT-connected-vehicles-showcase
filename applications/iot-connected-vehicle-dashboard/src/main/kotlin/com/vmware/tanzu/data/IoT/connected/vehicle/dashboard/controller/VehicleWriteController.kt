package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.controller

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class VehicleWriteController(private val repository: VehicleRepository) {
    @PostMapping("/saveVehicle")
    fun saveVehicle(@RequestBody vehicle: Vehicle) {
        repository.save(vehicle);
    }
}