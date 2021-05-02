package com.vmware.tanzu.data.IoT.vehicles.mqtt.geode.sink

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IoTConnectedVehicleStreamingGeodeSinkApplication

fun main(args: Array<String>) {
	runApplication<IoTConnectedVehicleStreamingGeodeSinkApplication>(*args)
}
