package com.vmware.vehicle.sink.functions;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.vehicle.sink.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class VehicleRepositorySink implements Consumer<Vehicle> {
    private final VehicleRepository repository;

    @Override
    public void accept(Vehicle vehicle) {
        repository.save(vehicle);
    }
}
