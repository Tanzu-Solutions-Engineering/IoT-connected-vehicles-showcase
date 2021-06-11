package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.ConfirmationHandler
import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleSender
import java.util.function.Function

/**
 * This class is responsible for publish vehicle data using a given streaming
 * producer.
 * @param producer the stream producer
 * @param converter converts a given vehicle structure to a message  payload
 *
 * @author Gregory Green
 */
class RabbitStreamingVehicleSender(private val producer: Producer, private val converter: Function<Vehicle,ByteArray>) :
    VehicleSender
{
    private val sumOfTwoNumber : (Int, Int) -> Int = { a: Int, b: Int -> a + b }

    /**
     * Send the vehicle using streaming
     * @param vehicle the vehicle data
     */
    override fun send(vehicle: Vehicle)
    {
        val msg = producer.messageBuilder()
                        .addData(converter.apply(vehicle)).build();
        var handler : ConfirmationHandler = ConfirmationHandler{status -> if(!status.isConfirmed)
            println("ERROR: $status.code NOT confirmed}")}

        producer.send(msg, handler );
    }
}