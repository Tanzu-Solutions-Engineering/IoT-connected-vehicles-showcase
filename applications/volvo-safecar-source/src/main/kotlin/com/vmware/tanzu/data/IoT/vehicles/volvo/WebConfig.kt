package com.vmware.tanzu.data.IoT.vehicles.volvo

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.vehicleController.XmlController
import com.vmware.tanzu.data.IoT.vehicles.vehicleController.XmlVehicleConsumer
import com.vmware.tanzu.data.IoT.vehicles.volvo.transformer.VolvoXmlToVehicleTransformer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
class WebConfig {

    @Value("\${\"xpath://VehicleLocation}")
    private lateinit var xpath: String;

    @Bean
    fun consumer(queue: Queue<Vehicle>, vehicleTransformer: VolvoXmlToVehicleTransformer) : XmlVehicleConsumer
    {
        return XmlVehicleConsumer(queue,vehicleTransformer,xpath);
    }

    @Bean
    fun xmlController(consumer: XmlVehicleConsumer ) : XmlController
    {
        return XmlController(consumer);

    }
}