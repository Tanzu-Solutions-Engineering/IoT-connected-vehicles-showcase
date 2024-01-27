package com.vmware.vehicle.sink.functions;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.vehicle.sink.repository.VehicleRepository;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VehicleRepositorySinkTest {
    @Mock
    private VehicleRepository repository;
    private Vehicle vehicle = JavaBeanGeneratorCreator.of(Vehicle.class).create();

    @Test
    void apply() {
        var subject = new VehicleRepositorySink(repository);
        subject.accept(vehicle);

        verify(repository).save(any(Vehicle.class));
    }
}