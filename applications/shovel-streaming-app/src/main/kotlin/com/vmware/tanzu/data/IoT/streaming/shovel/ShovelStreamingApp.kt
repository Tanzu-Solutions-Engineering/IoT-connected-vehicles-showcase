package com.vmware.tanzu.data.IoT.streaming.shovel

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ShovelStreamingApp


fun main(args: Array<String>) {
	runApplication<ShovelStreamingApp>(*args)
}
