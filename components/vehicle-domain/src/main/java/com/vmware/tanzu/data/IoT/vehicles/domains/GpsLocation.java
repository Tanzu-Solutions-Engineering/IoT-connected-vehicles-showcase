package com.vmware.tanzu.data.IoT.vehicles.domains;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GpsLocation{
    private Double latitude;
    private Double longitude;
}
