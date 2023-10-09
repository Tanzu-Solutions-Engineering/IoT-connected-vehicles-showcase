package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard

import org.apache.geode.pdx.ReflectionBasedAutoSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication
@EnableClusterDefinedRegions
@EnablePdx
@EnableGemfireRepositories(basePackages = ["com.vmware.tanzu.data.IoT.vehicles.repositories"])
class GeodeConfig {
    @Bean
     fun serializer(): ReflectionBasedAutoSerializer
     {
        return ReflectionBasedAutoSerializer(".*")
     }
}