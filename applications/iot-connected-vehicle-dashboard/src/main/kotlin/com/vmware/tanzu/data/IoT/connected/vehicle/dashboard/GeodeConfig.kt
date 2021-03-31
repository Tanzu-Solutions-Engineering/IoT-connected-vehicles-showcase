package com.vmware.tanzu.data.IoT.connected.vehicle.dashboard

import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnableClusterDefinedRegions

@Configuration
@ClientCacheApplication
@EnableClusterDefinedRegions
class GeodeConfig {

//    @Bean
//    fun userDetailsService(gemFireCache: GemFireCache): SpringSecurityUserService {
//        val users: Region<String, UserProfileDetails> = GeodeClient.getRegion(ClientCacheFactory.getAnyInstance(),"users")
//        return GeodeUserDetailsService(users)
//    } //------------------------------------------------


}