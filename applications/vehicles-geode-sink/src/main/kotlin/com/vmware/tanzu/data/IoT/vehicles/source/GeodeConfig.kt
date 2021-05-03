package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.sink.VehicleGemFireSink
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.apache.geode.cache.Region
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.config.annotation.EnableSecurity

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
@EnablePdx
class GeodeConfig {
    @Bean("Vehicle")
    fun simpleRegion(gemfireCache: GemFireCache): ClientRegionFactoryBean<String, Vehicle> {

        val region = ClientRegionFactoryBean<String, Vehicle>()
        region.cache = gemfireCache
        region.beanName = "Vehicle"
        region.setDataPolicy(DataPolicy.EMPTY)
        return region
    }//------------------------------------------------
    @Bean("vehicleGemFireSink")
    fun sink(@Qualifier("Vehicle") region : Region<String, Vehicle>) : VehicleGemFireSink
    {
        return VehicleGemFireSink(region);
    }
}