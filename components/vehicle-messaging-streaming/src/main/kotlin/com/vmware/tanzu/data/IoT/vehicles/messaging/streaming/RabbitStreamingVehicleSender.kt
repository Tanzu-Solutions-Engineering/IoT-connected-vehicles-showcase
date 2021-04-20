package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.ConfirmationHandler
import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleSender
import java.util.function.Function

/**
 * @author Gregory Green
 */
class RabbitStreamingVehicleSender(private val producer: Producer, private val converter: Function<Vehicle,ByteArray>) :
    VehicleSender
{
    val sumOfTwoNumber : (Int, Int) -> Int = { a: Int, b: Int -> a + b }

    override fun send(vehicle: Vehicle)
    {
        val msg = producer.messageBuilder()
                        .addData(converter.apply(vehicle)).build();
        var handler : ConfirmationHandler = ConfirmationHandler{status -> if(!status.isConfirmed)
            println("ERROR: $status.code NOT confirmed}")}

        producer.send(msg, handler );
    }
}