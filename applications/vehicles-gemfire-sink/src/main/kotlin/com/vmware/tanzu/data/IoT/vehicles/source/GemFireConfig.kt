package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.apache.geode.cache.GemFireCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.config.annotation.EnableSecurity
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
@EnablePdx
@EnableGemfireRepositories(basePackageClasses = [VehicleRepository::class])
class GemFireConfig {

    @Value("\${spring.data.gemfire.pool.default.locators:localhost[10334]}")
    private val locators : String = "localhost[10334]"
    @Bean
    public fun vehicle(gemfireCache : GemFireCache) : ClientRegionFactoryBean<String,Vehicle>{

        var factory = ClientRegionFactoryBean<String,Vehicle>()
        factory.setName("Vehicle")
        factory.cache = gemfireCache

        return factory
    }
}

