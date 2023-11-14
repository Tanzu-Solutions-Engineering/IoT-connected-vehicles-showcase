package com.vmware.tanzu.data.IoT.vehicles.domains

import java.time.LocalDateTime

interface IVehicle {
    var vin: String
    var odometer: Long
    var speed: Int
    var temperature: Int
    var gpsLocation: GpsLocation?
}