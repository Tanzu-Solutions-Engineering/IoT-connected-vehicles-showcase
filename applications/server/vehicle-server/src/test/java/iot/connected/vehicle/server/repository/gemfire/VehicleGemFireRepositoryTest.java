package iot.connected.vehicle.server.repository.gemfire;

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.apache.geode.cache.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleGemFireRepositoryTest {

    private VehicleGemFireRepository subject;

    @Mock
    private Region<String, Vehicle> region;
    private Vehicle vehicle = JavaBeanGeneratorCreator.of(Vehicle.class).create();


    @BeforeEach
    void setUp() {
        subject = new VehicleGemFireRepository(region);
    }


    @Test
    void findBy() {

        when(region.get(anyString())).thenReturn(vehicle);

        var actual = subject.findById(vehicle.getId());

        assertThat(Optional.of(vehicle)).isEqualTo(actual);
    }

    @Test
    void save() {
        subject.save(vehicle);

        verify(region).put(anyString(),any());
    }
}