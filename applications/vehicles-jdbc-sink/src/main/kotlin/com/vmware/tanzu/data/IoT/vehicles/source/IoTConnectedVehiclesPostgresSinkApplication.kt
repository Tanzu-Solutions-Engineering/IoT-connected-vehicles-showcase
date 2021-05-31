package com.vmware.tanzu.data.IoT.vehicles.source

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IoTConnectedVehiclesPostgresSinkApplication

fun main(args: Array<String>) {
	runApplication<IoTConnectedVehiclesPostgresSinkApplication>(*args)
}
