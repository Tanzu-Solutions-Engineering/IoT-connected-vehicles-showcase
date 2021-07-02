package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.EnvironmentBuilder
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Initialize the Rabbit streaming environment connection object.
 *
 * @author Gregory Green
 */
@Component
class RabbitStreamEnvironmentCreator(
    @Value("\${rabbitmq.streaming.uris}")
    private val uris: String,
    @Value("\${rabbitmq.streaming.rpcTimeout:30}")
    private val rpcTimeout : Long = 30,
    private val environmentBuilder: EnvironmentBuilder = Environment.builder()) : Creator<Environment>
{
    private val environment : Environment = environmentBuilder.uris(uris.split(","))
        .rpcTimeout(Duration.ofSeconds(rpcTimeout)).build();

    /**
     *
     * @return the create object
     */
    override fun create(): Environment {
        return  environment;
    }
}