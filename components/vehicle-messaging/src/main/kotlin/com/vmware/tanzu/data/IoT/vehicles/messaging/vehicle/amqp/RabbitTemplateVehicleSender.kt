package com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.amqp

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.springframework.amqp.rabbit.core.RabbitTemplate

class RabbitTemplateVehicleSender(
    private val rabbitTemplate: RabbitTemplate,
    private val exchange: String) : VehicleSender {
    override fun send(vehicle: Vehicle) {
        rabbitTemplate.convertAndSend(exchange,vehicle)
    }
}