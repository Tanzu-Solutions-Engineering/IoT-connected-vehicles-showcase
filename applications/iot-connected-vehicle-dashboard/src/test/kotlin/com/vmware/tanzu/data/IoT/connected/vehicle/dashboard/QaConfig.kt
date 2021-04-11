package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.repositories.VehicleRepository
import org.apache.geode.cache.Region
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QaConfig {
    @Bean
    fun mockRepository() : VehicleRepository
    {
        return Mockito.mock(VehicleRepository::class.java);
    }

    @Bean("Vehicle")
    fun mockVehicleRegion() : Region<String, Vehicle> {
        return  Mockito.mock(Region::class.java) as Region<String, Vehicle>;
    }

}