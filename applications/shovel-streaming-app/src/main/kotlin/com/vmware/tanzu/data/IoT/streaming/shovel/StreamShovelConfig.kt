package com.vmware.tanzu.data.IoT.streaming.shovel

import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing.RabbitStreamingRoutingFunctionHandler
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing.ShovelFunction
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.util.function.Function

/**
 * @author Gregory Green
 */
@Configuration
@ComponentScan(basePackageClasses = [ RabbitStreamingRoutingFunctionHandler::class,
    RabbitStreamSetup::class])
class StreamShovelConfig {

    @Bean
    fun shovelFunc() : Function<ByteArray,ByteArray>
    {
        return ShovelFunction();
    }
}