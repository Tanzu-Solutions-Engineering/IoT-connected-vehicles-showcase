package com.vmware.tanzu.data.IoT.vehicles.generator.simulator

import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleGenerator
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.Future


/**
 * @author Gregory Green
 */
@Component
class

VehicleGeneratorTask(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${vehicleCount}")
    private val vehicleCount: Int,
    @Value("\${messageCount}")
    private val messageCount: Int,
    @Value("\${distanceIncrements}")
    private val distanceIncrements: Double,
    @Value("\${delayMs}")
    private val delayMs: Long
) : CommandLineRunner {


    private val pool = Executors.newCachedThreadPool();

    override fun run(vararg args: String?) {
        while (true) {
            process();
        }
    }

    fun process() {

        this.rabbitTemplate.messageConverter = Jackson2JsonMessageConverter();

        var futures: MutableList<Future<*>> = mutableListOf();
        var future: Future<*>? = null;

        for (vinNumber in 1..vehicleCount) {
            future = pool.submit {
                val generator = VehicleGenerator(
                    distanceIncrements = distanceIncrements,
                    vin = "V$vinNumber"
                );

                var vehicle = generator.create();
                println("publishing vehicle:$vehicle")

                try {


                    this.rabbitTemplate.convertAndSend("vehicleSink", "", vehicle);

                    for (i in 0 until messageCount) {
                        vehicle = generator.move(vehicle, distanceIncrements);
                        println("publishing vehicle:$vehicle")
                        this.rabbitTemplate.convertAndSend("vehicleSink", "", vehicle);
                        sleep(delayMs);
                    }
                } catch (exception: RuntimeException) {
                    exception.printStackTrace();
                }

            };

            futures.add(future!!);
        }

        for (f in futures) {
            f.get(); //wait for the completion
        }

    }
}