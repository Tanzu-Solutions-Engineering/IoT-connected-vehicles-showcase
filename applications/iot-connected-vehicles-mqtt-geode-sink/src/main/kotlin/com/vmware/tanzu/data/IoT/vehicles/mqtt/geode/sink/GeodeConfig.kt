package com.vmware.tanzu.data.IoT.vehicles.mqtt.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication
@EnableSecurity
@EnablePdx
@EnableEntityDefinedRegions
@EnableClusterDefinedRegions
@EnableGemfireRepositories(basePackageClasses = [VehicleRepository::class])
class GeodeConfig {
}