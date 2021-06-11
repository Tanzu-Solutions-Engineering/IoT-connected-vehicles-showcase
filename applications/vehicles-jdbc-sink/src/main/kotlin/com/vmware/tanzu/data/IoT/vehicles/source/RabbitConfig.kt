package com.vmware.tanzu.data.IoT.vehicles.source

import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Gregory Green
 */
@Configuration
class RabbitConfig {

    @Value("\${spring.application.name}")
    private val applicationName: String? = null



    @Bean
    fun connectionNameStrategy(): ConnectionNameStrategy? {
        return ConnectionNameStrategy { applicationName!! }
    }
}