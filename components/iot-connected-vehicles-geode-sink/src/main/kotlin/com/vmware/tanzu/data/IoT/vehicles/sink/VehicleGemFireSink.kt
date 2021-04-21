package com.vmware.tanzu.data.IoT.vehicles.sink

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.apache.geode.cache.Region
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.function.Consumer


/**
 * @author Gregory Green
 */
class VehicleGemFireSink(
    private val vehicleRegion: Region<String, Vehicle>) : Consumer<Vehicle> {
    override fun accept(vehicleData: Vehicle) {
          vehicleRegion[vehicleData.id] = vehicleData;
    }
}