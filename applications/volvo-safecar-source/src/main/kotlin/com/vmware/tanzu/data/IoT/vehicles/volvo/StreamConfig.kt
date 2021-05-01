package com.vmware.tanzu.data.IoT.vehicles.volvo

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.data.collections.QueueSupplier
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy
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

    @Bean
    fun cns(): SimplePropertyValueConnectionNameStrategy? {
        return SimplePropertyValueConnectionNameStrategy("spring.application.name")
    }

    @Bean
    fun volvoVehicles()  :QueueSupplier<Vehicle>
    {
        return QueueSupplier<Vehicle>();
    }
}