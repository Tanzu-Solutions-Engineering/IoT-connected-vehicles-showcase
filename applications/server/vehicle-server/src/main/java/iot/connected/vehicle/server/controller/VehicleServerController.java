package iot.connected.vehicle.server.controller;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.service.SelfDrivingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.ThreadFactory;

@RestController
@RequestMapping("vehicle")
@Slf4j
public class VehicleServerController {

    private final SelfDrivingService service;
    private final ThreadFactory factory;

    @Value("${vehicle.refresh.rateSeconds:5}")
    private long refreshRateSecs;

    public VehicleServerController(SelfDrivingService service,
                                   @Qualifier("reactiveThreadFactory")
                                   ThreadFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @GetMapping
    public Flux<ServerSentEvent<Vehicle>> getVehicle() {

        var scheduler = Schedulers.newParallel(5,factory);
        return Flux.interval(Duration.ofSeconds(refreshRateSecs),scheduler)
                .map(sequence -> ServerSentEvent.<Vehicle> builder()
                        .data(service.getVehicle())
                        .build());
    }

    @PostMapping
    public void start() {
        service.start();
    }

    @PutMapping("check/engine/{checkEngine}")
    public void updateCheckEngine(@PathVariable boolean checkEngine) {
        service.updateCheckEngine(checkEngine);

    }

}
