package com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.converter

import com.fasterxml.jackson.databind.ObjectMapper
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import java.util.function.Function

class VehicleToBytes : Function<Vehicle,ByteArray> {
    private val objectMapper : ObjectMapper = ObjectMapper();
    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    override fun apply(vehicle: Vehicle): ByteArray {
        return objectMapper.writeValueAsBytes(vehicle);
    }
}