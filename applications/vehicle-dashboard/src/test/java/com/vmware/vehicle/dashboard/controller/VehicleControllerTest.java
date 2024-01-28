package com.vmware.vehicle.dashboard.controller;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.vehicle.dashboard.repository.VehicleRepository;
import jakarta.servlet.http.HttpServletResponse;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @Mock
    private VehicleRepository repository;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PrintWriter printWriter;
    private VehicleController subject;
    private Vehicle vehicle = JavaBeanGeneratorCreator.of(Vehicle.class).create();

    @BeforeEach
    void setUp() {
        subject = new VehicleController(repository);
    }

    @Test
    void updates() throws IOException {
        when(response.getWriter()).thenReturn(printWriter);
        subject.update(response);

        verify(repository).findAll();
    }

    @Test
    void saveVehicle() {
        subject.saveVehicle(vehicle);
        verify(repository).save(any(Vehicle.class));
    }
}