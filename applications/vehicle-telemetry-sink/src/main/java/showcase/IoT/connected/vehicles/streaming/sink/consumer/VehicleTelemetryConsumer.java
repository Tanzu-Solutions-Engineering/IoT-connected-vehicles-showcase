package showcase.IoT.connected.vehicles.streaming.sink.consumer;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import showcase.IoT.connected.vehicles.streaming.sink.repository.VehicleTelemetryRepository;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class VehicleTelemetryConsumer implements Consumer<Vehicle> {

    private final VehicleTelemetryRepository repository;
    @Override
    public void accept(Vehicle vehicle) {

        var vehicleTelemetry = new VehicleTelemetry(vehicle);
        repository.save(vehicleTelemetry);


    }
}
