package com.vmware.tanzu.data.IoT.vehicles.volvo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VolvoSafecarSourceApp

fun main(args: Array<String>) {
	runApplication<VolvoSafecarSourceApp>(*args)
}
