package com.vmware.tanzu.data.IoT.vehicles.streaming.jdbc.sink

import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.consumer.BytesToVehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer.RabbitStreamingConsumerHandler
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.runner.StreamingConsumerSpringRunner
import com.vmware.tanzu.data.IoT.vehicles.sink.telemetry.VehicleTelemetryRepositorySink
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
    BytesToVehicle::class,
    RabbitStreamingConsumerHandler::class,
    RabbitStreamEnvironmentCreator::class,
    StreamingConsumerSpringRunner::class])
@EnableAsync
class RabbitStreamingConsumerConfig {
    @Bean
    fun executor() : ThreadPoolTaskExecutor {
        return ThreadPoolTaskExecutor();
    }
}