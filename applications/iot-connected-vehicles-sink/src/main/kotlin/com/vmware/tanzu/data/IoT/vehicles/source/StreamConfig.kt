package com.vmware.tanzu.data.IoT.vehicles.source

import org.springframework.context.annotation.Configuration


/**
 * spring.cloud.stream.bindings.input-in-0.group=someGroup
spring.cloud.stream.bindings.input-in-0.consumer.batch-mode=true
spring.cloud.stream.rabbit.bindings.input-in-0.consumer.enable-batching=true
spring.cloud.stream.rabbit.bindings.input-in-0.consumer.batch-size=10
spring.cloud.stream.rabbit.bindings.input-in-0.consumer.receive-timeout=200

spring.cloud.function.definition=orders;beaconRequests
spring.cloud.stream.bindings.orders-in-0.destination=orders
spring.cloud.stream.bindings.beaconRequests-in-0.destination=beaconRequests

spring.rabbitmq.password=tanzu
spring.rabbitmq.port=5672
spring.rabbitmq.username=vmware
 */
@Configuration
class StreamConfig {
}