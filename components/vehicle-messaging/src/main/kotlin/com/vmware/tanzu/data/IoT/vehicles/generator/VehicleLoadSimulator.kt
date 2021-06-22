package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.util.concurrent.Executors

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
    private val vinPrefix: String = "V"
) : CommandLineRunner {

    private val pool = Executors.newCachedThreadPool();

    /**
     * Start multi-threading send generated vehicle information
     * @param args the input arguments
     */
    fun process(vararg args: String?) {

        for (vinNumber in 1 .. vehicleCount)
        {
            pool.submit {
                val generator = VehicleGenerator(
                    distanceIncrements = distanceIncrements,
                    vin = toVin(vinNumber)
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

    fun toVin(vinNumber: Int): String {
        return "${vinPrefix}${vinNumber}";

    }

    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    override fun run(vararg args: String?) {
        while(true)
        {
            process(*args);
        }
    }
}