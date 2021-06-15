package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.EnvironmentBuilder
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Initialize the Rabbit streaming environment connection object.
 *
 * @author Gregory Green
 */
@Component
class RabbitStreamEnvironmentCreator(
    @Value("\${rabbitmq.streaming.uris}")
    private val uris: String,
    private val environmentBuilder: EnvironmentBuilder = Environment.builder()) : Creator<Environment> {
    /**
     *
     * @return the create object
     */
    override fun create(): Environment {
        return environmentBuilder.uris(uris.split(",")).build();
    }
}