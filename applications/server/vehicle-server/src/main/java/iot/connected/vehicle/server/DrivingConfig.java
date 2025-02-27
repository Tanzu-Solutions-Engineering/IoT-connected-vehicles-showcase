package iot.connected.vehicle.server;

import iot.connected.vehicle.server.service.SelfDrivingService;
import iot.connected.vehicles.engine.VehicleEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DrivingConfig {

    @Bean
    VehicleEngine vehicleEngine(@Value("${vehicle.distanceIncrements}") Double distanceIncrements, @Value("${vehicle.vin}") String vin)
    {
        return new VehicleEngine(distanceIncrements,vin);
    }
    @Bean
    Thread drivingThread(SelfDrivingService service){
        return new Thread(service);
    }
}
