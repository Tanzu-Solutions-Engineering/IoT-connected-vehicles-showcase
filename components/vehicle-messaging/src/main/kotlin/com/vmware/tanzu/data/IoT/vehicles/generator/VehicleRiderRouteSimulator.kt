package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component

/**
 * @author Gregory Green
 */
@Component
class VehicleRiderRouteSimulator : VehicleRider
{
    private val logger = LogFactory.getLog(VehicleRiderRouteSimulator::class.java)

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

                logger.info("START Processing  vehicle: ${vehicle.vin} messageCount:${messageCount}")

                try {
                    sender.send(vehicle);

                    for (i in 0 until messageCount) {
                        generator.move(vehicle, distanceIncrements);

                        sender.send(vehicle);

                        if (delayMs > 0)
                            Thread.sleep(delayMs);
                    }
                    logger.info("END Processing vehicle: ${vehicle.vin} messageCount:${messageCount}")

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