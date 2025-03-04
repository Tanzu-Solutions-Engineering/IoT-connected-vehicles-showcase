package iot.connected.vehicle.server.service;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import iot.connected.vehicle.server.repository.VehicleServerRepository;
import iot.connected.vehicles.engine.VehicleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SelfDrivingService extends Thread {
    private final VehicleEngine vehicleEngine;
    private final VehicleServerRepository repository;
    private final Vehicle vehicle;
    private final long SPEED_FACTOR = 3000;
    private long fixSpeed = 50;

    public SelfDrivingService(VehicleEngine vehicleEngine, VehicleServerRepository repository) {
        this.repository = repository;
        this.vehicleEngine = vehicleEngine;

        vehicle = this.vehicleEngine.create();
    }

    public void drive() {

        log.info("Driving: {} ",vehicle);

        vehicleEngine.move(vehicle);
        repository.save(vehicle);
    }


    @Override
    public void run() {
        while(true)
        {


            try {
                // log.info("START Processing  vehicle: ${vehicle.vin} messageCount:${messageCount}");
                drive();

                log.info("Sleeping");
                sleep( SPEED_FACTOR/ fixSpeed);
                log.info("Slept");

            } catch (Throwable e) {
                log.warn("Error: {}",e);
            }
        }
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void updateCheckEngine(boolean checkEngine) {

        vehicle.setCheckEngine(checkEngine);

        repository.save(vehicle);
    }
}
