package com.vmware.tanzu.data.IoT.vehicles.domains

import java.time.LocalDateTime

/**
 * @author Gregory Green
 */
data class VehicleTelemetry  (
    override var vin: String = "",
    override var odometer: Long = 0,
    override var speed : Int = 0,
    override var temperature: Int = 0,
    override var gpsLocation: GpsLocation? = null,
    var captureTimestamp : LocalDateTime = LocalDateTime.now(),
    var id : Long = 0
) : IVehicle