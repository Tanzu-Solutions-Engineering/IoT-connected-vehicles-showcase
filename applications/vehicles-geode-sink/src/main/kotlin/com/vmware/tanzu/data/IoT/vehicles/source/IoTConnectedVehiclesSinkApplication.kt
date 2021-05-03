package com.vmware.tanzu.data.IoT.vehicles.source

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IoTConnectedVehiclesSinkApplication

fun main(args: Array<String>) {
	runApplication<IoTConnectedVehiclesSinkApplication>(*args)
}
