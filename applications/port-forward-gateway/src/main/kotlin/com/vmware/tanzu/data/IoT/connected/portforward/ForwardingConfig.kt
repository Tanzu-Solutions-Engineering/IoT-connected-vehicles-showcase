package com.vmware.tanzu.data.IoT.connected.portforward

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.PredicateSpec
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @author Gregory Green
 */
@Configuration
class ForwardingConfig {

    /**
     * -DforwardUri=http://localhost:7070/pulse
     */
    @Value("\${forwardUri}")
    private var forwardUri : String = "http://localhost:7070/pulse";

    @Bean
    fun forwardPort(builder: RouteLocatorBuilder): RouteLocator? {
        return builder.routes()
            .route("portForward")
            { r: PredicateSpec ->
                r.path("/portForward")
                    .uri("${forwardUri}")
            }
            .build();
    }
}