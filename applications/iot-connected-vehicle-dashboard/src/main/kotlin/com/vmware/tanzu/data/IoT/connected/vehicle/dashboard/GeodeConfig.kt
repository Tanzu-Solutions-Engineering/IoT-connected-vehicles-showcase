package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard

import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories

@Configuration
@ClientCacheApplication
@EnableClusterDefinedRegions
@EnableGemfireRepositories(basePackages = ["com.vmware.tanzu.data.IoT.vehicles.repositories"])
class GeodeConfig {

//    @Bean
//    fun vehicleRepository(gemFireCache: GemFireCache): VehicleGeodeRepository {
//        val region: Region<String, Vehicle> = GeodeClient.getRegion(ClientCacheFactory.getAnyInstance(),"Vehicle")
//        return VehicleGeodeRepository(region);
//    } //------------------------------------------------


}