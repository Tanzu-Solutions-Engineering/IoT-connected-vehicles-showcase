package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import com.vmware.tanzu.data.IoT.vehicles.sink.vehicle.VehicleRepositorySink
import org.springframework.amqp.rabbit.connection.Connection
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync

/**
 * @author Gregory Green
 */
@Configuration
class RabbitConfig {

    @Value("\${spring.application.name}")
    private val applicationName: String? = null

    @Bean("vehicleRepositorySink")
    fun vehicleRepositorySink(repository: VehicleRepository): VehicleRepositorySink {
        return VehicleRepositorySink(repository);
    }

    @Bean
    fun connectionNameStrategy(): ConnectionNameStrategy? {
        return ConnectionNameStrategy { applicationName!! }
    }

    @Bean
    fun delete(connection: ConnectionFactory): String {
        return ""
    }
}