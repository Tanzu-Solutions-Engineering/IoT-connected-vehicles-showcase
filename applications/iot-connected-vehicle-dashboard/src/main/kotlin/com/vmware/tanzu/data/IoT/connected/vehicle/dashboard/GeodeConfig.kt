package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.geode.listener.CacheListerConsumerBridge
import org.apache.geode.cache.DataPolicy
import org.apache.geode.cache.GemFireCache
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.client.ClientRegionFactoryBean
import org.springframework.data.gemfire.client.Interest
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories
import org.springframework.messaging.simp.SimpMessagingTemplate
import java.util.function.Consumer

@Configuration
@ClientCacheApplication(subscriptionEnabled = true)
@EnableGemfireRepositories(basePackages = ["com.vmware.tanzu.data.IoT.vehicles.repositories"])
class GeodeConfig {


    @Value("\${vehicle.register.interest.pattern:.*}")
    private var registerInterestPattern: String = ".*"

    @Value("\${vehicle.destination:/topic/vehicles}")
    private var destination: String = "/topic/vehicles"

    @Bean("Vehicle")
    fun vehicleRepository(gemFireCache: GemFireCache,
                          messageTemple : SimpMessagingTemplate): ClientRegionFactoryBean<String,Vehicle> {
        var region = ClientRegionFactoryBean<String,Vehicle>()
        region.cache = gemFireCache
        region.setDataPolicy(DataPolicy.EMPTY)
        region.setRegionName("Vehicle")

        var consumer = Consumer<Vehicle>{ v -> messageTemple.convertAndSend(destination,v)}

        var listenerConsumerBridge = CacheListerConsumerBridge<String,Vehicle>(consumer)
        region.setCacheListeners(arrayOf(listenerConsumerBridge))

        val interests : Interest<String> = Interest.newInterest<String>(registerInterestPattern) as Interest<String>
        region.setInterests(arrayOf(interests))

        return region
    }

}