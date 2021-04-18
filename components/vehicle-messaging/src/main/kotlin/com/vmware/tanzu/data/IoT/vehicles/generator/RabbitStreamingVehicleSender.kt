package com.vmware.tanzu.data.IoT.vehicles.generator

//import com.rabbitmq.stream.Producer
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle

class RabbitStreamingVehicleSender(
//    private val producer: Producer
    ) :
    VehicleSender
{
    override fun send(vehicle: Vehicle)
    {
    }
}