package com.vmware.tanzu.data.IoT.vehicles.streaming.jdbc.sink

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VehicleTelemetryStreamingJdbcSinkApplication

fun main(args: Array<String>) {
	runApplication<VehicleTelemetryStreamingJdbcSinkApplication>(*args)
}
