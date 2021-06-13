package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.BytesToVehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer.RabbitStreamingConsumerHandler
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer.StreamingConsumerSpringRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author Gregory Green
 */
@Configuration
@ComponentScan(basePackageClasses = [
    RabbitStreamingConsumerHandler::class,
    BytesToVehicle::class,
    RabbitStreamEnvironmentCreator::class,
    StreamingConsumerSpringRunner::class])
@EnableAsync
class RabbitStreamingConsumerConfig {
    @Bean
    fun executor() : ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor();
    }
}