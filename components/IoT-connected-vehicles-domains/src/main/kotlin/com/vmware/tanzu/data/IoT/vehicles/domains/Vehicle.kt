package com.vmware.tanzu.data.IoT.vehicles.domains

import java.time.LocalDateTime

data class Vehicle(
    override var vin: String = "",
    override var odometer: Long = 0,
    override var speed : Int = 0,
    override var temperature: Int = 0,
    override var gpsLocation: GpsLocation? = null,
    var predictions : Set<Prediction>? = null
) : IVehicle {
    var id: String?
        get() = vin;
        set(value)
        {
            if(value !=null)
                vin = value
        };
}