package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitTemplateVehicleSender(
    private val rabbitTemplate: RabbitTemplate,
    private val exchange: String) : VehicleSender {
    override fun send(vehicle: Vehicle) {
        rabbitTemplate.convertAndSend(exchange,vehicle)
    }
}