package com.vmware.tanzu.data.IoT.connected.portforward

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author Gregory Green
 */
@SpringBootApplication
class PortForwardGatewayApp

fun main(args: Array<String>) {
    runApplication<PortForwardGatewayApp>(*args)
}