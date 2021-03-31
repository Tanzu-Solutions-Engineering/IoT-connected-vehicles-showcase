package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import io.pivotal.services.dataTx.geode.client.GeodeClient
import org.apache.geode.cache.Region
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GeodeConfig {

    @Bean
    fun vehicleRegion(): Region<String, Vehicle> {
        return GeodeClient.connect().getRegion("Vehicle");
    }

}