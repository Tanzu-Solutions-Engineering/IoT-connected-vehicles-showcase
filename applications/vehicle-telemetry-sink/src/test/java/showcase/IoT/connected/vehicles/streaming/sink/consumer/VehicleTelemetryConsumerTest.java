package showcase.IoT.connected.vehicles.streaming.sink.consumer;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.IoT.connected.vehicles.streaming.sink.repository.VehicleTelemetryRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VehicleTelemetryConsumerTest {

    private VehicleTelemetryConsumer subject;
    @Mock
    private VehicleTelemetryRepository repository;

    @BeforeEach
    void setUp() {
        subject = new VehicleTelemetryConsumer(repository);
    }

    @Test
    void accept() {
        subject.accept(Vehicle.builder().build());

        verify(repository).save(any());
    }
}