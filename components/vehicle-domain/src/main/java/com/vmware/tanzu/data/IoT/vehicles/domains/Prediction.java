package com.vmware.tanzu.data.IoT.vehicles.domains;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Prediction{
    private String vin;
    private boolean prediction;
    private double probability;
}
