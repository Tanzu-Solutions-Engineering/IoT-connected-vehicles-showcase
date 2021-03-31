package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.repositories

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


/**
 * @author Gregory Green
 */
@Repository
interface VehicleRepository : CrudRepository<Vehicle,String> {
}