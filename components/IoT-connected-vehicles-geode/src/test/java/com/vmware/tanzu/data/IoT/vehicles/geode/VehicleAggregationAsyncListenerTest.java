package com.vmware.tanzu.data.IoT.vehicles.geode;

import org.apache.geode.cache.Operation;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.pdx.PdxInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class VehicleAggregationAsyncListenerTest
{
    private   Double expectedAvg = 3.0;
    private VehicleAggregationAsyncListener subject;

    @Mock
    private PdxInstance record;

    @Mock
    private AsyncEvent event;

    @Mock
    private Region<String,Double> mockRegion;

    @Mock
    private PdxInstance pdx;
    private Integer speed = 3;

    private Properties properties;

    @Mock
    private Supplier<Region<String,Double>> mockRegionGetter;


    @BeforeEach
    void setUp()
    {
        subject = new VehicleAggregationAsyncListener(mockRegionGetter);
        properties = new Properties();
    }

    @Test
    void aggregation_average_score()
    {
        when(mockRegionGetter.get()).thenReturn((Region)mockRegion);
        when(event.getOperation()).thenReturn(Operation.CREATE);
        when(event.getDeserializedValue()).thenReturn(pdx);
        when(pdx.getField("speed")).thenReturn(speed);

        List<AsyncEvent> events = Arrays.asList(event);
        assertTrue(subject.processEvents(events));

        verify(mockRegion).put("avgSpeed",expectedAvg);

    }

    @Test
    void whenRegionNull_Then_ThrowException()
    {
        assertThrows(
                IllegalArgumentException.class,() ->
                        new VehicleAggregationAsyncListener(mockRegionGetter).processEvents(Collections.singletonList(event))
        );

    }
}