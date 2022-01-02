package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import com.vmware.tanzu.data.IoT.vehicles.sink.vehicle.VehicleRepositorySink
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
//@EnablePdx
@EnableGemfireRepositories(basePackageClasses = [VehicleRepository::class])
@EnableEntityDefinedRegions
//@EnableClusterDefinedRegions
@ComponentScan(basePackageClasses = [VehicleRepositorySink::class])
class GeodeConfig {

    @Bean("Vehicle")
    fun vehicleRegion(gemFireCache: GemFireCache) : ClientRegionFactoryBean<String,Vehicle>
    {
        var region = ClientRegionFactoryBean<String,Vehicle>()
        region.cache = gemFireCache
        region.setDataPolicy(DataPolicy.EMPTY)
        region.setRegionName("Vehicle")
        return region;
    }
}