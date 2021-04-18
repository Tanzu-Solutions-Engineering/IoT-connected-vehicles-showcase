package com.vmware.tanzu.data.IoT.vehicles.generator

import org.springframework.boot.CommandLineRunner
import java.util.concurrent.Executors

/**
 * @author Gregory Green
 */
class VehicleLoadSimulator(
    private val sender: VehicleSender,
    private val vehicleCount: Int,
    private val messageCount: Int,
    private val distanceIncrements: Double,
    private val delayMs: Long
) : CommandLineRunner {

    private val pool = Executors.newCachedThreadPool();

    /**
     * Start multi-threading send generated vehicle information
     * @param args the input arguments
     */
    override fun run(vararg args: String?) {

        for (vinNumber in 1 .. vehicleCount)
        {
            pool.submit {
                val generator = VehicleGenerator(
                    distanceIncrements = distanceIncrements,
                    vin = "V$vinNumber"
                );

                var vehicle = generator.create();
                println("publishing vehicle:$vehicle")

                try {
                    this.sender.send(vehicle);

                    for (i in 0 until messageCount) {
                        vehicle = generator.move(vehicle, distanceIncrements);
                        println("publishing vehicle:$vehicle")
                        this.sender.send(vehicle);
                        Thread.sleep(delayMs);
                    }
                }
                catch(exception : RuntimeException)
                {
                    exception.printStackTrace();
                }

            };
        }

    }
}