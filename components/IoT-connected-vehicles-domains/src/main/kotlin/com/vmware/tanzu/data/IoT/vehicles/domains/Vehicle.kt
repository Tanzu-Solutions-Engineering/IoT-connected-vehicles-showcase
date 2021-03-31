package com.vmware.tanzu.data.IoT.vehicles.domains

data class Vehicle(
    var vin: String = "",
    var odometer: Long = 0,
    var speed : Int = 0,
    var temperature: Int = 0,
    var gpsLocation: GpsLocation? = null
)