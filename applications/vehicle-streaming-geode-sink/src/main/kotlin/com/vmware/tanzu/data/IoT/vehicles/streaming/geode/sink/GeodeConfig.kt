package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import com.vmware.tanzu.data.IoT.vehicles.sink.vehicle.VehicleRepositorySink
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.*
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
//@EnablePdx
@EnableGemfireRepositories(basePackageClasses = [VehicleRepository::class])
@EnableEntityDefinedRegions
@EnableClusterDefinedRegions
@ComponentScan(basePackageClasses = [VehicleRepositorySink::class])
class GeodeConfig {
}