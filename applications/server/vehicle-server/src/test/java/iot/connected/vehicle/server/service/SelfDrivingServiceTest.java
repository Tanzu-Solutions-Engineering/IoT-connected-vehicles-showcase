package iot.connected.vehicle.server.service;

import iot.connected.vehicle.server.repository.VehicleServerRepository;
import iot.connected.vehicles.engine.VehicleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

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
}