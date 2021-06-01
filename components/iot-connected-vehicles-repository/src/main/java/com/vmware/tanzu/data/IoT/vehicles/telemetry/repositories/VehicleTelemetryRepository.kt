package com.vmware.tanzu.data.IoT.vehicles.telemetry.repositories

import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VehicleTelemetryRepository : CrudRepository<VehicleTelemetry,Long> {
}