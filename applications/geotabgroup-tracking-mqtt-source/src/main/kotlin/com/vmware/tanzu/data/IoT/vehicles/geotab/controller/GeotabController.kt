package com.vmware.tanzu.data.IoT.vehicles.geotab.controller

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.vehicle.publisher.VehicleSender
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.function.Function

/**
 * REST controller for Geotab JSON data
 * @author Gregory Green
 */
@RestController
class GeotabController(private val consumer: VehicleSender,
                       @Qualifier("geotabTransformer")private val transformer: Function<JsonObject,Vehicle>)
{
    private val gson  = Gson();

    @PostMapping("/load")
    fun load(@RequestBody json: String) {
        val jsonObject = gson.fromJson(json,JsonObject::class.java);

        val geotabDataArray : JsonArray =jsonObject.getAsJsonArray("GeotabData");
        for(geotabDataArrayItem : JsonElement in geotabDataArray)
        {
            val dataArray = geotabDataArrayItem.asJsonObject.getAsJsonArray("data");
            for(dataItem in dataArray)
            {
                consumer.send(transformer.apply(dataItem.asJsonObject));
            }
        }
    }
}