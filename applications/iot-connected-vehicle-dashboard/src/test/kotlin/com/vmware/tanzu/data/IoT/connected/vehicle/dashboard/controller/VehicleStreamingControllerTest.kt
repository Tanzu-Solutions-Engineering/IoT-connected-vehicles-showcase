package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard.controller

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import jakarta.servlet.http.HttpServletResponse
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.io.PrintWriter

@ExtendWith(MockitoExtension::class)
internal class VehicleStreamingControllerTest{

    private lateinit var expectedVehicle: Vehicle;
    @Mock
    private lateinit var vehicleRepository: VehicleRepository;

    @Mock
    private lateinit var writer: PrintWriter;

    @Mock
    private lateinit var response: HttpServletResponse;

    private lateinit var subject: VehicleStreamingController;



    @BeforeEach
    internal fun setUp() {
//        response = mock(HttpServletResponse::class.java);
//        writer = mock(PrintWriter::class.java);
        `when`(response.writer).thenReturn(writer);
//        vehicleRepository = mock(VehicleRepository.class);
        expectedVehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create();

        subject = VehicleStreamingController(vehicleRepository);

    }

    @Test
    internal fun vehicleUpdates() {

        val expectedList = arrayListOf<Vehicle>(expectedVehicle);

        Mockito.`when`(vehicleRepository.findAll()).thenReturn(expectedList);
        subject.updates(response);
        verify(vehicleRepository).findAll();
        verify(response).writer;
        verify(writer).println(anyString())
        verify(writer).flush();
    }
}