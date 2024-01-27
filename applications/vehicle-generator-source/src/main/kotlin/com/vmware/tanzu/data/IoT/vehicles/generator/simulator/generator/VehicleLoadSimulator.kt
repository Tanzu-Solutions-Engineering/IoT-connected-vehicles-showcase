package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


/**
 * @author Gregory Green
 */
@Component
class VehicleLoadSimulator(
    private val sender: VehicleSender,
    @Value("\${vehicleCount}")
    private val vehicleCount: Int,
    @Value("\${messageCount}")
    private val messageCount: Int,
    @Value("\${distanceIncrements}")
    private val distanceIncrements: Double,
    @Value("\${delayMs}")
    private val delayMs: Long,
    @Value("\${vinPrefix}")
    private val vinPrefix: String = "V",
    private val generator: VehicleGenerator = VehicleGenerator(
        distanceIncrements = distanceIncrements
    ),
    private val vehicleRider: VehicleRider
) : CommandLineRunner {


    /**
     * Start multi-threading send generated vehicle information
     * @param vehicles the input arguments
     */
    fun process(vehicles: Array<Vehicle>) {

        println("Processing  vinPrefix:${vinPrefix} vehicleCount:${vehicleCount} messageCount:${messageCount}")

        for (vehicle in vehicles) {
            vehicleRider.ride(vehicle,messageCount,sender,generator,distanceIncrements,delayMs);
        }
    }

    /**
     * Generate VIN number
     */
    fun toVin(vinNumber: Int): String {
        return "${vinPrefix}${vinNumber}";
    }

    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    override fun run(vararg args: String?) {
        val vehicles = constructVehicles();
        process(vehicles);
    }

    fun constructVehicles(): Array<Vehicle> {
        var vehicles =
            Array<Vehicle>(vehicleCount) { vinNumber -> generator.vin = this.toVin(vinNumber); generator.create(); };

        return vehicles;
    }
}