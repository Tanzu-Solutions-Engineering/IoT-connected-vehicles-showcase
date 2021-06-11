package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication
import org.springframework.data.gemfire.config.annotation.EnablePdx
import org.springframework.data.gemfire.config.annotation.EnableSecurity

@Configuration
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
@EnableSecurity
@EnablePdx
class GeodeConfig {

}