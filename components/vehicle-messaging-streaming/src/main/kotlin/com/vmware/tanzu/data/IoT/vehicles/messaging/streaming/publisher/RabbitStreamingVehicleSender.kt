package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.publisher

import com.rabbitmq.stream.ConfirmationHandler
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * This class is responsible for publish vehicle data using a given streaming
 * producer.
 * @param producer the stream producer
 * @param converter converts a given vehicle structure to a message  payload
 *
 * @author Gregory Green
 */
@Component
class RabbitStreamingVehicleSender(
    private val envCreator: Creator<Environment>,
    private val converter: Function<Vehicle, ByteArray>,
    @Value("\${streamName}")
    private val streamName: String,
    private val streamSetup: StreamSetup
) : VehicleSender
{
    private val producer : Producer;

    init {
        streamSetup.initialize(streamName);
        producer = envCreator.create().producerBuilder().stream(streamName).build()
    }

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