package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.publisher

import com.rabbitmq.stream.ConfirmationHandler
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
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
    private val streamSetup: StreamSetup,
    @Value("\${spring.application.name}")
    private val producerName: String,
    @Value("\${batchSize:50000}")
    private val batchSize: Int = 5_0000,
    @Value("\${subEntrySize:1000}")
    private val subEntrySize: Int = 1000,
    @Value("\${maxUnconfirmedMessages:20000}")
    private val maxUnconfirmedMessages: Int = 20_000,
    private var handler : ConfirmationHandler = ConfirmationHandler{status -> if(!status.isConfirmed)
        println("ERROR: $status.code NOT confirmed MESSAGE:${status.message} batchSize:$batchSize subEntrySize:$subEntrySize maxUnconfirmedMessages:$maxUnconfirmedMessages")}

) : VehicleSender
{
    private val producer : Producer;

    init {
        streamSetup.initialize(streamName);
        producer = envCreator.create()
            .producerBuilder()
            .name(producerName)
            .batchSize(batchSize)
            .subEntrySize(subEntrySize)
            .maxUnconfirmedMessages(maxUnconfirmedMessages)
            .stream(streamName).build()
    }

    /**
     * Send the vehicle using streaming
     * @param vehicle the vehicle data
     */
    @Async
    override fun send(vehicle: Vehicle)
    {
        val msg = producer.messageBuilder()
                        .addData(converter.apply(vehicle)).build();
        producer.send(msg, handler );
    }
}