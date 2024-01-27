package com.vmware.tanzu.data.IoT.vehicles.generator.simulator

import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleGenerator
import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleLoadSimulator
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.amqp.RabbitTemplateVehicleSender
//import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleGenerator
//import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleLoadSimulator
//import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.amqp.RabbitTemplateVehicleSender
//import nyla.solutions.core.data.collections.QueueSupplier
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean
import org.springframework.amqp.rabbit.connection.ThreadChannelConnectionFactory
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync


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
@EnableAsync
class AmqpRabbitConfig {

    @Value("\${spring.rabbitmq.username:guest}")
    private var username: String = "guest";

    @Value("\${spring.rabbitmq.password:guest}")
    private var password:  String = "guest";

    @Value("\${spring.rabbitmq.host:localhost}")
    private var hostname: String = "localhost";

    @Value("\${spring.application.name}")
    private var applicationName: String? = null

    @Bean
    fun connectionNameStrategy(): ConnectionNameStrategy? {
        return ConnectionNameStrategy { connection: ConnectionFactory? -> applicationName!! }
    }

//    @Bean
//    fun generateVehicles()  :QueueSupplier<Vehicle>
//    {
//        return QueueSupplier<Vehicle>();
//    }

    @Bean
    fun connectionFactory( ) : ConnectionFactory
    {
        var factory = RabbitConnectionFactoryBean();
        factory.setHost(hostname);
        factory.setUsername(username);
        factory.setPassword(password);

        return ThreadChannelConnectionFactory(factory.rabbitConnectionFactory);
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