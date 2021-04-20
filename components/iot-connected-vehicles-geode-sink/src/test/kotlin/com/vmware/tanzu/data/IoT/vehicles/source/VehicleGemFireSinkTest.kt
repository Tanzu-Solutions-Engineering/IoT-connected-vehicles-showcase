package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.sink.VehicleGemFireSink
import org.apache.geode.cache.Region
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.function.Function

internal class VehicleGemFireSinkTest {
    private val vin = "hello";
    private lateinit var mockRegion: Region<String, Vehicle>;
    private lateinit var subject: VehicleGemFireSink;
    private lateinit var mockFunction: Function<String,Vehicle>;


    @BeforeEach
    internal fun setUp() {
        mockRegion = mock(Region::class.java) as Region<String, Vehicle>;
        mockFunction =  mock(Function::class.java) as Function<String, Vehicle>;
        subject = VehicleGemFireSink(mockRegion) ;
    }

    @Test
    internal fun store() {

        var vehicle = Vehicle(vin);

        subject.accept(vehicle);

        verify(mockRegion)!!.put(anyString(), any(Vehicle::class.java));

    }
}