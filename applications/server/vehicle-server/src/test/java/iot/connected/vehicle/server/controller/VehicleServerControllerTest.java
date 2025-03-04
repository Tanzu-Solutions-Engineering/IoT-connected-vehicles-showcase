package iot.connected.vehicle.server.controller;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.service.SelfDrivingService;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServerControllerTest {

    @Mock
    private SelfDrivingService service;

    @Mock
    private Vehicle expected = JavaBeanGeneratorCreator.of(Vehicle.class).create();
    private VehicleServerController subject;
    private ThreadFactory threadFactory = Executors.defaultThreadFactory();

    @BeforeEach
    void setUp() {

        subject = new VehicleServerController(service,threadFactory);
    }

    @Test
    void start() {
        subject.start();

        verify(service).start();
    }

    @Test
    void updateChangeEngine() {

        subject.updateCheckEngine(true);

        verify(service).updateCheckEngine(anyBoolean());
    }

    @Test
    void getVehicle() {

        when(service.getVehicle()).thenReturn(expected);

        var actual = subject.getVehicle();

        assertThat(actual).isNotNull();

        assertThat(actual.next().block().data()).isEqualTo(expected);

    }
}