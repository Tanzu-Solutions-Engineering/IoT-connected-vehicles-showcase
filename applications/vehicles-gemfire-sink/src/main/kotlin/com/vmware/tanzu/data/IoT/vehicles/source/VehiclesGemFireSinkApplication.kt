package com.vmware.tanzu.data.IoT.vehicles.source

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication

@SpringBootApplication
@ClientCacheApplication(name = "iot-connected-vehicles-sink")
class VehiclesGemFireSinkApplication

fun main(args: Array<String>) {
	runApplication<VehiclesGemFireSinkApplication>(*args)
}
