package com.vmware.tanzu.data.IoT.vehicles.mqtt.geode.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.sink.VehicleRepositorySink
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
@ClientCacheApplication
@EnableSecurity
@EnablePdx
class GeodeConfig {
}