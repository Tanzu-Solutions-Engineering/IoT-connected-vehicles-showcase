package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.controller

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

/**
 * @author Gregory Green
 */
@RestController
@RequestMapping("/vehicles")
class VehicleReadController(private val repository: VehicleRepository) {
    @GetMapping("/")
    fun findAll(): Iterable<Vehicle> {
        return repository.findAll()
    }

}