package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle

/**
 * @author Gregory Green
 */
interface VehicleSender {
     fun send(vehicle: Vehicle)
}