package com.vmware.tanzu.data.IoT.vehicles.generator.streaming

import com.vmware.tanzu.data.IoT.vehicles.generator.VehicleLoadSimulator
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.publisher.RabbitStreamingVehicleSender
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.converter.VehicleToBytes
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


/**
 * @author Gregory Green
 */
@Configuration
@ComponentScan(basePackageClasses = [
    RabbitStreamEnvironmentCreator::class,
    RabbitStreamingVehicleSender::class,
    VehicleLoadSimulator::class])
class RabbitmqStreamConfig {

    /**
     * Conversion strategy used by the sender
     */
    @Bean
    fun vehicleToBytes() : VehicleToBytes
    {
        return VehicleToBytes();
    }
}