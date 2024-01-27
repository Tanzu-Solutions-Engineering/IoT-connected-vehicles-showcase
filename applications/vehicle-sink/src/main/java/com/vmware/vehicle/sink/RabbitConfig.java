package com.vmware.vehicle.sink;

import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    ConnectionNameStrategy connectionNameStrategy(){
        return (connection) ->  applicationName;
    }

}
