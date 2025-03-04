package iot.connected.vehicles.engine;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.util.JavaBean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(false).isEqualTo(actual.isCheckEngine());
    }


    @Test
    void move() {

        var input = JavaBeanGeneratorCreator.of(Vehicle.class).create();

        var latitude = input.getGpsLocation().getLatitude();
        var longitude = input.getGpsLocation().getLongitude();

        var actual = subject.move(input);

        assertThat(actual).isNotNull();
        assertThat(input.getVin()).isEqualTo(actual.getVin());
        assertThat(latitude).isNotEqualTo(actual.getGpsLocation().getLatitude());
        assertThat(latitude).isNotEqualTo(actual.getGpsLocation().getLongitude());

    }
}