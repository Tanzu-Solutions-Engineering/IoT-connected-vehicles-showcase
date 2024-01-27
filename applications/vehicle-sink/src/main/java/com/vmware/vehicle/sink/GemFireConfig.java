package com.vmware.vehicle.sink;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.vehicle.sink.repository.VehicleRepository;
import org.apache.geode.cache.GemFireCache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.config.annotation.EnableSecurity;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
@EnablePdx
@EnableGemfireRepositories(basePackageClasses = {VehicleRepository.class})
public class GemFireConfig {

    @Value("${spring.data.gemfire.pool.default.locators:localhost[10334]}")
    private String locators = "localhost[10334]";

    @Bean
    public ClientRegionFactoryBean<String,Vehicle> vehicle(GemFireCache gemfireCache ) {

        var factory = new ClientRegionFactoryBean<String, Vehicle>();
        factory.setName("Vehicle");
        factory.setCache(gemfireCache);
        return factory;
    }
}
