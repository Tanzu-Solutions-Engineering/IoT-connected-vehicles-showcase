package com.vmware.maintence.prediction.interference.ai

import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MlPredictionTest{

    private lateinit var subject: MlPrediction
    private lateinit var vehicleTelemetry: VehicleTelemetry

    @BeforeEach
    fun setUp() {
        vehicleTelemetry = JavaBeanGeneratorCreator.of(VehicleTelemetry::class.java).create()
        subject = MlPrediction()
    }

    @Test
    fun calculate() {
        var prediction = subject.calculate(vehicleTelemetry);

        assertNotNull(prediction);
    }

    @Test
    fun train() {


    }
}