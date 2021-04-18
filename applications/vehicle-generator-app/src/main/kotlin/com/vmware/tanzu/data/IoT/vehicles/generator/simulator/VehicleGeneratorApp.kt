package com.vmware.tanzu.data.IoT.vehicles.generator.simulator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VehicleGeneratorApp

fun main(args: Array<String>) {
	runApplication<VehicleGeneratorApp>(*args)
}
