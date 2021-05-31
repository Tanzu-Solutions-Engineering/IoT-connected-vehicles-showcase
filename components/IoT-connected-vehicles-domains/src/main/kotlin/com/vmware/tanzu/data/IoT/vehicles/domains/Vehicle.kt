package com.vmware.tanzu.data.IoT.vehicles.domains

import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Vehicle(
    var vin: String = "",
    var odometer: Long = 0,
    var speed : Int = 0,
    var temperature: Int = 0,
    @Embedded
    var gpsLocation: GpsLocation? = null
) {
    var id: String?
        @Id
        get() = vin;
        set(value)
        {
            if(value !=null)
                vin = value
        };
}