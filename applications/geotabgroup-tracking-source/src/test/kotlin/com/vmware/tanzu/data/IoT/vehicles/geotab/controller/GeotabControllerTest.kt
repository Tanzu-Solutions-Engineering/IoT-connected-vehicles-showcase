package com.vmware.tanzu.data.IoT.vehicles.geotab.controller

import com.google.gson.JsonObject
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.io.IO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import java.util.function.Function

@ExtendWith(MockitoExtension::class)
internal class GeotabControllerTest{

    private lateinit var subject: GeotabController;
    private lateinit var json : String;

    @Mock
    private lateinit var mockFunction: Function<JsonObject, Vehicle>;
    @Mock
    private lateinit var mockQueue: Queue<Vehicle>;

    @BeforeEach
    internal fun setUp() {
        json = IO.readFile("src/test/resources/Geotab.json");
        subject = GeotabController(mockQueue,mockFunction);
    }

    @Test
    internal fun load() {
        subject.load(json);
        verify(mockFunction, atLeastOnce()).apply(any());
        verify(mockQueue, atLeastOnce()).add(any());

    }
}