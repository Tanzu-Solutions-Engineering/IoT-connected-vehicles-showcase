@file:JvmName("GpsLocation")
package com.vmware.tanzu.data.IoT.vehicles.domains

import javax.persistence.Embeddable

@Embeddable
data class GpsLocation(
    var latitude: Double =0.0,
    var longitude: Double = 0.0)