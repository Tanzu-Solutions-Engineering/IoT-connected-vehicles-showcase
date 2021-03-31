package com.vmware.tanzu.data.IoT.vehicles.geotab.transformer

import com.google.gson.JsonObject
import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * Convert from Json Object to Vehicle
 * @author Gregory Green
 */
@Component("geotabTransformer")
class GeotabVehicleGsonTransformer : Function<JsonObject,Vehicle> {

    /**
     * @param jsonOject the json object to convert
     */
    override fun apply(jsonOject: JsonObject): Vehicle {
        var speed = 0;
        var speedElement= jsonOject.get("speed");
        if(speedElement != null)
            speed = speedElement.asInt;

        var vehicle = Vehicle(speed = speed);

        vehicle.vin = jsonOject.get("device").asJsonObject.get("vin").asString;

        val latitude = jsonOject.get("latitude")
        val longitude = jsonOject.get("longitude")

        if(latitude != null && longitude != null)
        {
            vehicle.gpsLocation = GpsLocation(latitude = latitude.asDouble,
                longitude = longitude.asDouble);
        }

        return vehicle;
    }
}