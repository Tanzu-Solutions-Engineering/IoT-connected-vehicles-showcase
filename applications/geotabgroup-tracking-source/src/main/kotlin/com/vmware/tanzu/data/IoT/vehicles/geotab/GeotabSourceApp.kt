package com.vmware.tanzu.data.IoT.vehicles.geotab

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GeotabSourceApp

fun main(args: Array<String>) {
	runApplication<GeotabSourceApp>(*args)
}
