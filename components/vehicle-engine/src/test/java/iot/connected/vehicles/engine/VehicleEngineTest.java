package iot.connected.vehicles.engine;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehicleEngineTest {

    private VehicleEngine subject;
    private Double distanceIncrements = 0.2;
    private String vin = "junit";

    @BeforeEach
    void setUp() {
        subject = new VehicleEngine(distanceIncrements,vin);
    }

    @Test
    void create() {
        Vehicle actual = subject.create();


        assertThat(actual).isNotNull();

        assertThat(actual).isNotEqualTo(subject.create());
    }
}