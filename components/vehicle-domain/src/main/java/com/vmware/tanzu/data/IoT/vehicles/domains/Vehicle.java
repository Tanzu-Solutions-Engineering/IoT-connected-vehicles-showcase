package com.vmware.tanzu.data.IoT.vehicles.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
    private String vin;
    private Long odometer;
    private int  speed;
    private int temperature;
    private boolean checkEngine;
    private GpsLocation gpsLocation;

    public String getId() {
        return vin;
    }

    public void setId(String id) {
        this.vin = id;
    }
}
