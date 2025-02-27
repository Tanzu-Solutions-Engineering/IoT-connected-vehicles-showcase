package iot.connected.vehicle.server.repository;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import nyla.solutions.core.patterns.repository.SaveRepository;

import java.util.Optional;

public interface VehicleServerRepository extends SaveRepository<Vehicle> {

    Optional<Vehicle> findById(String vehicle);
}
