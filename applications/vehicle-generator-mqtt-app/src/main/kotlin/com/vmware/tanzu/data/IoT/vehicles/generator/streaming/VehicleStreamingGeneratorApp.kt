package com.vmware.tanzu.data.IoT.vehicles.generator.streaming

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VehicleStreamingGeneratorApp

fun main(args: Array<String>) {
	runApplication<VehicleStreamingGeneratorApp>(*args)
}
