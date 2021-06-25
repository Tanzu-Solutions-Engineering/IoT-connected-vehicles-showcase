package com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.amqp

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class RabbitTemplateVehicleSender(
    private val rabbitTemplate: RabbitTemplate,
    @Value("\${spring.cloud.stream.bindings.generateVehicles-out-0.destination}")
    private val exchange: String,
    private val routingKey: String = ""
) : VehicleSender {

    override fun send(vehicle: Vehicle) {
      //  println("SENDING $vehicle")
        rabbitTemplate.convertAndSend(exchange,routingKey,vehicle)
    }
}