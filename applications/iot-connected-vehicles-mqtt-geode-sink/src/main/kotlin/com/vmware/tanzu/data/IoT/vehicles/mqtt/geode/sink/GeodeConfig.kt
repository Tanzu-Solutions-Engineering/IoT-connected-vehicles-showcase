package com.vmware.tanzu.data.IoT.vehicles.mqtt.geode.sink

import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.config.annotation.EnableSecurity

@Configuration
@ClientCacheApplication
@EnableSecurity
@EnablePdx
class GeodeConfig {
}