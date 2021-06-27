package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

/**
 * @author Gregory Green
 */
@Component
class VehicleRiderRouteSimulator : VehicleRider
{

    /**
     * Start multi-threading send generated vehicle information
     */
    override fun ride(vehicle: Vehicle,
                      messageCount: Int,
                      sender:VehicleSender,
                      generator : VehicleGenerator,
                      distanceIncrements : Double,
                      delayMs : Long) {

        var runnable : Runnable  = Runnable {

            while(true)
            {

                println("Processing  vehicle: ${vehicle.vin} messageCount:${messageCount}")

                try {
                    sender.send(vehicle);

                    for (i in 0 until messageCount) {
                        generator.move(vehicle, distanceIncrements);

                        sender.send(vehicle);

                        if (delayMs > 0)
                            Thread.sleep(delayMs);
                    }
                } catch (exception: RuntimeException) {
                    exception.printStackTrace();
                }

                System.gc();
            }
        }

        var thread = Thread(runnable);
        thread.name = "rider-${vehicle.vin}";
        thread.start();
    }
}