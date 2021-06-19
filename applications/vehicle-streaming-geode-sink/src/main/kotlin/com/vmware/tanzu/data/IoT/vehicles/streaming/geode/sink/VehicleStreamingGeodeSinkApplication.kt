package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VehicleStreamingGeodeSinkApplication

fun main(args: Array<String>) {
	runApplication<VehicleStreamingGeodeSinkApplication>(*args)
}
