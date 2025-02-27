package iot.connected.vehicle.server.service;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import iot.connected.vehicles.engine.VehicleEngine;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SelfDrivingServiceTest {


    private SelfDrivingService subject;

    @Mock
    private VehicleEngine engine;

    @Mock
    private VehicleServerRepository repository;

    @BeforeEach
    void setUp() {
        subject = new SelfDrivingService(engine,repository);
    }

    @Test
    void drive() {
        subject.drive();


        verify(engine).move(any());
        verify(repository).save(any());
    }

    @Test
    void getVehicle() {

        var expected = JavaBeanGeneratorCreator.of(Vehicle.class).create();
        when(engine.create()).thenReturn(expected);
        subject = new SelfDrivingService(engine,repository);

        assertThat(subject.getVehicle()).isEqualTo(expected);
    }
}