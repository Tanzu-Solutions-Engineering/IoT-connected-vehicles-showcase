package com.vmware.vehicle.dashboard.repository;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle,String> {
}
