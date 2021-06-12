package com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import java.util.function.Function

/**
 * Convert json to Vehicle
 * @author Gregory Green
 */
class StringToVehicle(private val objectMapping: ObjectMapper = ObjectMapper()) : Function<String,Vehicle> {
    /**
     * Applies this function to the given argument.
     *
     * @param json the function argument
     * @return the function result
     */
    override fun apply(json: String): Vehicle {
        return objectMapping.readValue(json,Vehicle::class.java);
    }
}