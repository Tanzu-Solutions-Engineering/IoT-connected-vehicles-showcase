package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.VehicleJsonSenderConsumer
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.function.Function

/**
 * @author Gregory Green
 */
internal class VehicleJsonSenderConsumerTest{
    @Test
    internal fun accept() {
        var mockSender = mock<VehicleSender>();
        var mockFunction = mock<Function<String,Vehicle>>()
        {
            on { apply(any())} doReturn Vehicle()
        }
        var subject = VehicleJsonSenderConsumer(mockSender,mockFunction);

        val vehicleJson = "{}";
        subject.accept(vehicleJson);
        verify(mockSender).send(any());
    }
}