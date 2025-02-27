package iot.connected.vehicle.server.repository.gemfire;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import lombok.RequiredArgsConstructor;
import org.apache.geode.cache.Region;

import java.util.Optional;

@RequiredArgsConstructor
public class VehicleGemFireRepository implements VehicleServerRepository {

    private final Region<String, Vehicle> region;

    @Override
    public Optional<Vehicle> findById(String vehicleId) {
        return Optional.ofNullable(region.get(vehicleId));
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return region.put(vehicle.getId(),vehicle);
    }
}
