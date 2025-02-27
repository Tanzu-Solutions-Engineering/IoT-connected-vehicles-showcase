package iot.connected.vehicle.server.controller;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("vehicle")
public class VehicleServerController {
    private final VehicleServerRepository repository;
    private final String vehicle;

    public VehicleServerController(VehicleServerRepository repository,
                                   @Value("vehicle.id") String vehicle) {
        this.repository = repository;
        this.vehicle = vehicle;
    }

    @GetMapping
    public Mono<Vehicle> getVehicle() {
        return Mono.just(repository.findById(vehicle).orElse(null));
    }


}
