package com.vmware.tanzu.data.IoT.vehicles.volvo.transformer

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
class VolvoXmlToVehicleTransformer : Function<Node,Vehicle> {
    override fun apply(vehicleLocation: Node): Vehicle {

        var v= Vehicle()

        v.vin = XML.searchNodeTextByXPath("Vin",vehicleLocation);
        v.odometer = XML.searchNodeTextByXPath("Odometer",vehicleLocation).toDouble().toLong();

        val speedText = XML.searchNodeTextByXPath("RoadSpeed",vehicleLocation);
        if(speedText.isNotBlank()) {
             v.speed = speedText.toInt();
        }

        v.gpsLocation = GpsLocation(latitude =
                        XML.searchNodeTextByXPath(
                            "Latitude",
                            vehicleLocation).toDouble(),
                        XML.searchNodeTextByXPath(
                            "Longitude",
                            vehicleLocation).toDouble())
        return v;
    }

}