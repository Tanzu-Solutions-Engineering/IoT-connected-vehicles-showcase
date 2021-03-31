package com.vmware.tanzu.data.IoT.vehicles.randmcnally

import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.xml.XML
import org.springframework.stereotype.Component
import org.w3c.dom.Node
import java.util.function.Function


/**
 * @author Gregory Green
 */
@Component
class InMsgToVehicleTransformer : Function<Node,Vehicle> {
    override fun apply(inboundMessageNode: Node): Vehicle {

        var v= Vehicle()

        v.vin = XML.searchNodeTextByXPath("MessageHeader/Truck/VIN",inboundMessageNode);
        v.odometer = XML.searchNodeTextByXPath("MessageHeader/VehicleContext/@Odometer",inboundMessageNode).toLong();
        v.speed = XML.searchNodeTextByXPath("MessageHeader/VehicleContext/@Speed",inboundMessageNode).toInt();
        v.gpsLocation = GpsLocation(latitude =
                        XML.searchNodeTextByXPath(
                            "MessageHeader/Position/Coordinate/Latitude",
                            inboundMessageNode).toDouble(),
                        XML.searchNodeTextByXPath(
                            "MessageHeader/Position/Coordinate/Longitude",
                            inboundMessageNode).toDouble())
        return v;
    }


}