package com.vmware.tanzu.data.IoT.vehicles.generator.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.Producer
import com.rabbitmq.stream.StreamException
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.RabbitStreamingVehicleSender
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleLoadSimulator
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleSender
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleToBytes
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * spring.cloud.stream.bindings.input-in-0.group=someGroup
spring.cloud.stream.bindings.input-in-0.consumer.batch-mode=true
spring.cloud.stream.rabbit.bindings.input-in-0.consumer.enable-batching=true
spring.cloud.stream.rabbit.bindings.input-in-0.consumer.batch-size=10
spring.cloud.stream.rabbit.bindings.input-in-0.consumer.receive-timeout=200

spring.cloud.function.definition=vehicles;beaconRequests
spring.cloud.function.definition=vehicles
spring.cloud.stream.bindings.vehicles-out-0.destination=vehicles
spring.cloud.stream.bindings.beaconRequests-in-0.destination=beaconRequests

spring.rabbitmq.password=tanzu
spring.rabbitmq.port=5672
spring.rabbitmq.username=vmware
 */
@Configuration
class StreamConfig {
    @Value("\${spring.application.name}")
    private val applicationName: String = "VehicleGeneratorStreaming"

    @Value("\${delayMs}")
    private var delayMs: Long = 0;

    @Value("\${distanceIncrements}")
    private var distanceIncrements: Double = 1.0;

    @Value("\${messageCount}")
    private var messageCount: Int = 10;

    @Value("\${vehicleCount}")
    private var vehicleCount: Int = 10;

    @Value("\${streamName}")
    private var streamName = "vehicleStream";

    @Bean
    fun sender() : VehicleSender
    {
        val environment: Environment = Environment.builder().build()

        var creator = environment.streamCreator().stream(streamName);

        try{
            creator.create()
        }
        catch(e : StreamException)
        {
            if(e.message == null)
                throw e;

            if(!e.message!!.contains("STREAM_ALREADY_EXISTS"))
                throw e;
        }
        val producer: Producer = environment.producerBuilder()
            .name(applicationName)
            .stream(streamName)
            .build();

        return RabbitStreamingVehicleSender(producer, VehicleToBytes());
    }

    @Bean
    fun streamTask(sender: VehicleSender): VehicleLoadSimulator
    {
        return VehicleLoadSimulator(sender,vehicleCount,messageCount,distanceIncrements,delayMs);
    }
}