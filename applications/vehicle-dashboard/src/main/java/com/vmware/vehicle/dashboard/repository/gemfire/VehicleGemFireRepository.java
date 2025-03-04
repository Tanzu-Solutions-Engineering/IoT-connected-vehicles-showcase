package com.vmware.vehicle.dashboard.repository.gemfire;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.vehicle.dashboard.repository.VehicleRepository;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleGemFireRepository extends VehicleRepository, GemfireRepository<Vehicle,String> {
}
