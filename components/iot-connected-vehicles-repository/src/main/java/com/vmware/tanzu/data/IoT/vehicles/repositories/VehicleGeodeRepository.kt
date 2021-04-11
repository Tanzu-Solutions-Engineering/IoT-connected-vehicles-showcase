//package com.vmware.tanzu.data.IoT.vehicles.repositories
//
//import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
//import org.apache.geode.cache.Region
//
//class VehicleGeodeRepository(private val region: Region<String, Vehicle>)
//    : VehicleRepository {
//    override fun save(vehicle: Vehicle): Vehicle {
//        region[vehicle.vin] = vehicle;
//        return vehicle;
//    }
//
//}