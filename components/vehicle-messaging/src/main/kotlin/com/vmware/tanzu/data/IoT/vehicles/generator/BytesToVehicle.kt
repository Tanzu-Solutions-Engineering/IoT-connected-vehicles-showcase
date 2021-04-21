package com.vmware.tanzu.data.IoT.vehicles.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import java.util.function.Function

/**
 * @author Gregory Green
 */
class BytesToVehicle(private val objectMapper: ObjectMapper = ObjectMapper()) : Function<ByteArray,Vehicle>{
    /**
     * Applies this function to the given argument.
     *
     * @param bytes the vehicle JSON
     * @return the Vehicle result
     */
    override fun apply(bytes: ByteArray): Vehicle {
        return objectMapper.readValue(bytes,Vehicle::class.java);
    }
}