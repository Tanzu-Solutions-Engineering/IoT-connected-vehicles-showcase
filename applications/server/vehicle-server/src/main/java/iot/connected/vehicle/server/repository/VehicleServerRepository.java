package iot.connected.vehicle.server.repository;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;

import java.util.Optional;

public interface VehicleServerRepository {

    Optional<Vehicle> findById(String vehicleId);
}
