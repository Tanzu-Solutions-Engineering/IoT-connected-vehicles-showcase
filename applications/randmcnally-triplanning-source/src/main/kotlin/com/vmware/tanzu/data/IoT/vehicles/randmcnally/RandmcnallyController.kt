package com.vmware.tanzu.data.IoT.vehicles.randmcnally

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.xml.XML
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.w3c.dom.Node
import java.util.*
import java.util.function.Function


/**
 * @author Gregory Green
 */
@RestController("randmcnally")
class RandmcnallyController(private val consumer: Queue<Vehicle>,
                            private val transformer: Function<Node,Vehicle>) {

    @PostMapping("load", consumes = ["text/xml"])
    fun load(@RequestBody xmlText: String) {

        val xml = XML(xmlText);
        var inboundMessages = xml.document.getElementsByTagName("InboundMessage");
        var len = inboundMessages.length;
        for (i in 0 until len)
        {
            var node = inboundMessages.item(i);
           consumer.add(transformer.apply(node));
        }
    }


}