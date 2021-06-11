package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.generator.BytesToVehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.RabbitStreamingConsumerHandler
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import com.vmware.tanzu.data.IoT.vehicles.sink.vehicle.VehicleRepositorySink
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author Gregory Green
 */
@Configuration
@ComponentScan(basePackageClasses = [RabbitStreamingConsumerHandler::class, BytesToVehicle::class])
@EnableAsync
class RabbitStreamingConsumerConfig {
    @Bean
    fun executor() : ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor();
    }
}