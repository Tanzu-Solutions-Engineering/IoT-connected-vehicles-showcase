package com.vmware.tanzu.data.IoT.vehicles.vehicleController

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.function.Consumer

@RestController
class XmlController(
    private val consumer: Consumer<String>)
{

    @PostMapping("load", consumes = ["text/xml"])
    fun load(@RequestBody xmlText: String) {
        consumer.accept(xmlText);

    }
}