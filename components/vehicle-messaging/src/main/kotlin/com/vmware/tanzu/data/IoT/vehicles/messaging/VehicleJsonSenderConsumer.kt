package com.vmware.tanzu.data.IoT.vehicles.messaging

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.StringToVehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import java.util.function.Consumer
import java.util.function.Function

/**
 * Handles
 * @author Gregory Green
 */
class VehicleJsonSenderConsumer(private val vehicleSender: VehicleSender,
                                private val converter: Function<String, Vehicle> =  StringToVehicle()
) : Consumer<String>
{
    /**
     * Performs this operation on the given argument.
     *
     * @param json the input argument
     */
    override fun accept(json: String) {
        vehicleSender.send(converter.apply(json))

    }

}