package showcase.IoT.connected.vehicles.streaming.sink.repository;

import com.vmware.tanzu.data.IoT.vehicles.domains.VehicleTelemetry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleTelemetryRepository extends CrudRepository<VehicleTelemetry,String> {
}
