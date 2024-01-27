package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import nyla.solutions.core.operations.performance.stats.ThroughputStatistics
import org.apache.commons.logging.LogFactory
import org.springframework.stereotype.Component
import java.time.LocalDateTime

/**
 * Generate GPS for a vehicle to simulate a parallel threaded ride.
 * @author Gregory Green
 */
@Component
class VehicleRiderRouteSimulator(
    private val throughPutStats: ThroughputStatistics = ThroughputStatistics()) : VehicleRider
{
    private val logger = LogFactory.getLog("VehicleRiderRouteSimulator")

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

            var startTime = LocalDateTime.now();

            while(true)
            {

                logger.info("START Processing  vehicle: ${vehicle.vin} messageCount:${messageCount}");


                try {
                    sender.send(vehicle);

                    for (i in 0 until messageCount) {
                        generator.move(vehicle, distanceIncrements);

                        sender.send(vehicle);

                        if (delayMs > 0)
                            Thread.sleep(delayMs);
                    }

                    throughPutStats.increment((messageCount+1).toLong());

                    logger.info("END Processing ~ throughput:${throughPutStats.throughputPerSecond(startTime, LocalDateTime.now())} msg/sec vehicle: ${vehicle.vin} messageCount:${messageCount}")

                } catch (exception: RuntimeException) {
                    exception.printStackTrace();
                }

            }
        }

        var thread = Thread(runnable);
        thread.name = "rider-${vehicle.vin}";
        thread.start();
    }
}