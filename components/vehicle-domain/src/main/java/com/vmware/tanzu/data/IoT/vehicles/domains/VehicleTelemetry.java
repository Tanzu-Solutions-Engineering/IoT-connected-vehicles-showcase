package com.vmware.tanzu.data.IoT.vehicles.domains;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleTelemetry extends Vehicle {

    private LocalDateTime captureTimestamp  = LocalDateTime.now();

    public VehicleTelemetry(Vehicle vehicle) {
        this.setId(vehicle.getId());
        this.setGpsLocation(vehicle.getGpsLocation());
        this.setVin(vehicle.getVin());
        this.setSpeed(vehicle.getSpeed());
        this.setOdometer(vehicle.getOdometer());
        this.setTemperature(vehicle.getTemperature());
    }
}
