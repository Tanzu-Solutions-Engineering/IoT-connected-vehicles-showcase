package com.vmware.tanzu.data.IoT.vehicles.source

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.apache.geode.cache.Region
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import java.util.function.Consumer


/**
 * @author Gregory Green
 */
@Component("vehicleGemFireSink")
class VehicleGemFireSink(
    @Qualifier("vehicleRegion") private val vehicleRegion: Region<String, Vehicle>) : Consumer<Vehicle> {
    override fun accept(vehicleData: Vehicle) {
        vehicleRegion[vehicleData.vin] = vehicleData;
    }
}