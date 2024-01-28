package com.vmware.vehicle.sink.functions;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.vehicle.sink.repository.VehicleRepository;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class VehicleRepositorySink implements Consumer<Vehicle> {
    private final VehicleRepository repository;

    public VehicleRepositorySink(VehicleRepository repository) {
        this.repository = repository;
    }

    @Override
    public void accept(Vehicle vehicle) {
        repository.save(vehicle);
    }
}
