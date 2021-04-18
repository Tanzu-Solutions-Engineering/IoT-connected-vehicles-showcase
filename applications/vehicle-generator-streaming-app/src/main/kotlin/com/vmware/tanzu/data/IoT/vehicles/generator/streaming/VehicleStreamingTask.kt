package com.vmware.tanzu.data.IoT.vehicles.generator.streaming

//import com.rabbitmq.stream.Producer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component


/**Ò
 * @author Gregory Green
 */
@Component
class VehicleStreamingTask(
//    private val rabbitProducer: Producer,
    @Value("\${vehicleCount}")
    private val vehicleCount: Int,
    @Value("\${messageCount}")
    private val messageCount: Int,
    @Value("\${distanceIncrements}")
    private val distanceIncrements: Double,
    @Value("\${delayMs}")
    private val delayMs: Long
) : CommandLineRunner {
    override fun run(vararg args: String?) {

    }

}