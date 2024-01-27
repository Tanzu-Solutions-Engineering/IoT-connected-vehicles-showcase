package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
//import org.springframework.stereotype.Service

//@Service
interface  VehicleRider {

    /**
     * Start multi-threading send generated vehicle information
     */
    fun ride(
        vehicle: Vehicle,
        messageCount: Int,
        sender: VehicleSender,
        generator: VehicleGenerator,
        distanceIncrements: Double,
        delayMs: Long
    );
}