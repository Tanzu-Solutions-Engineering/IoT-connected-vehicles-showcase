package com.vmware.tanzu.data.IoT.vehicles.generator.simulator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleGenerator
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleLoadSimulator
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.amqp.RabbitTemplateVehicleSender
import nyla.solutions.core.data.collections.QueueSupplier
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
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
@ComponentScan(basePackageClasses = [RabbitTemplateVehicleSender::class, VehicleLoadSimulator::class, VehicleGenerator::class])
class AmqpRabbitConfig {
    @Bean
    fun generateVehicles()  :QueueSupplier<Vehicle>
    {
        return QueueSupplier<Vehicle>();
    }

    @Bean
    fun convert() : MessageConverter
    {
        return Jackson2JsonMessageConverter();
    }

    @Bean
    fun rabbitListenerContainerFactory() :SimpleRabbitListenerContainerFactory {
        var factory = SimpleRabbitListenerContainerFactory();
        factory.setMessageConverter(Jackson2JsonMessageConverter());
        return factory;
    }


}