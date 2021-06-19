package com.vmware.tanzu.data.IoT.streaming.shovel

import com.rabbitmq.stream.Environment
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing.RabbitStreamingRoutingFunctionHandler
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing.ShovelFunction
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

/**
 * @author Gregory Green
 */
@Configuration
class StreamShovelConfig {
    @Value("\${rabbitmq.streaming.routing.input.uris}")
    private lateinit var inputUris: String;

    @Value("\${rabbitmq.streaming.routing.output.uris}")
    private lateinit var outputUris: String;

    @Value("\${rabbitmq.streaming.routing.input.stream.name}")
    private lateinit var inputStreamName: String;

    @Value("\${rabbitmq.streaming.routing.output.stream.name}")
    private lateinit var outputStreamName: String;


    @Value("\${spring.application.name}")
    private lateinit var applicationName: String;


    @Bean
    fun rabbitStreamingRoutingFunctionHandler() : RabbitStreamingRoutingFunctionHandler
    {
        var routingFunction: Function<ByteArray,ByteArray> = ShovelFunction();
        var inputEnvCreator: Creator<Environment> = RabbitStreamEnvironmentCreator(inputUris);
        var inputStreamSetup: StreamSetup = RabbitStreamSetup(inputEnvCreator,maxAgeHours = 1,maxLengthGb = 1,maxSegmentSizeMb = 20);
        var outputEnvCreator: Creator<Environment> = RabbitStreamEnvironmentCreator(outputUris);
        var outputStreamSetup: StreamSetup = RabbitStreamSetup(outputEnvCreator,maxAgeHours = 1,maxLengthGb = 1,maxSegmentSizeMb = 20);
        return RabbitStreamingRoutingFunctionHandler(
            inputStreamName = inputStreamName,
            inputStreamSetup = inputStreamSetup,
            inputEnvCreator = inputEnvCreator,
            outputStreamName = outputStreamName,
            outputEnvCreator = outputEnvCreator,
            outputStreamSetup = outputStreamSetup,
            routingFunction = routingFunction,
            applicationName = applicationName,
        );
    }
}