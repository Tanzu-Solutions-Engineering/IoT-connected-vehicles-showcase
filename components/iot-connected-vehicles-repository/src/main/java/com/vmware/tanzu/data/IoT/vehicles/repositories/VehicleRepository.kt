package com.vmware.tanzu.data.IoT.vehicles.repositories

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VehicleRepository : CrudRepository<Vehicle,String> {
}