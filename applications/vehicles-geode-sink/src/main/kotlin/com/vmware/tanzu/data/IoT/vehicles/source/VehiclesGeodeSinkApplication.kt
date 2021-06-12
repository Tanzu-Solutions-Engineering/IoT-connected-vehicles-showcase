package com.vmware.tanzu.data.IoT.vehicles.source

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VehiclesGeodeSinkApplication

fun main(args: Array<String>) {
	runApplication<VehiclesGeodeSinkApplication>(*args)
}
