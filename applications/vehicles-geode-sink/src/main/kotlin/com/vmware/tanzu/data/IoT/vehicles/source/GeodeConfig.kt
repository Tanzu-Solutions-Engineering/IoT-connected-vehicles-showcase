package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
@EnablePdx
@EnableEntityDefinedRegions
@EnableClusterDefinedRegions
@EnableGemfireRepositories(basePackageClasses = [VehicleRepository::class])
class GeodeConfig {
}