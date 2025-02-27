package iot.connected.vehicle.server.controller;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServerControllerTest {

    @Mock
    private VehicleServerRepository repository;

    @Mock
    private Vehicle expected;

    private VehicleServerController subject;
    private String vehicleId = "carId";

    @BeforeEach
    void setUp() {
        subject = new VehicleServerController(repository,vehicleId);
    }

    @Test
    void getVehicle() {

        when(repository.findById(any())).thenReturn(Optional.of(expected));

        Mono<Vehicle> actual = subject.getVehicle();

        assertThat(actual).isNotNull();

        assertThat(actual.block()).isEqualTo(expected);

    }
}