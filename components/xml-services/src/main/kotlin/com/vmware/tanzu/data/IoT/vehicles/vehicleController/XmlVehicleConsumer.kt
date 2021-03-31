package com.vmware.tanzu.data.IoT.vehicles.vehicleController

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.xml.XML
import org.springframework.web.bind.annotation.RestController
import org.w3c.dom.Node
import java.util.*
import java.util.function.Consumer
import java.util.function.Function

@RestController
class XmlVehicleConsumer(
    private val consumer: Queue<Vehicle>,
    private val transformer: Function<Node, Vehicle>,
    private val xpath : String) : Consumer<String>
{
    override fun accept(xmlText: String) {
        val xml = XML(xmlText);
        var inboundMessages = xml.searchNodesXPath(xpath);
        var len = inboundMessages.length;
        for (i in 0 until len)
        {
            var node = inboundMessages.item(i);
            consumer.add(transformer.apply(node));
        }
    }
}