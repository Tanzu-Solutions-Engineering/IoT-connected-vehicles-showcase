package com.vmware.tanzu.data.IoT.vehicles.geode;

import org.apache.geode.cache.*;
import org.apache.geode.cache.asyncqueue.AsyncEvent;
import org.apache.geode.cache.asyncqueue.AsyncEventListener;
import org.apache.geode.pdx.PdxInstance;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.function.Supplier;

/**
 * Example aggregation
 * @author gregory green
 */
public class VehicleAggregationAsyncListener  implements AsyncEventListener, Declarable
{
    public static final String AGGREGATION_REGION = "VehicleAggregation";
    private    Logger logger = LogManager.getLogger();
    private final Supplier<Region<String, Double>> regionGetter;

    public VehicleAggregationAsyncListener()
    {
        this(()-> CacheFactory.getAnyInstance().getRegion(AGGREGATION_REGION));
    }

    public VehicleAggregationAsyncListener(Supplier<Region<String, Double>> regionGetter)
    {
        this.regionGetter = regionGetter;
    }


    @Override
    public boolean processEvents(List<AsyncEvent> events)
    {
        if(events == null || events.isEmpty())
        {
            return true;
        }

        Operation operation = null;
        PdxInstance pdx = null;
        Integer speed;
        double speedTotal = 0;
        long count =0;

        Region<String, Double> vehicleAggregationRegion = this.regionGetter.get();
        if(vehicleAggregationRegion == null)
            throw new IllegalArgumentException("Region:"+AGGREGATION_REGION+" not found");

        for (AsyncEvent<String,PdxInstance> event: events)
        {
            logger.info("event {}",event);
            operation = event.getOperation();

            if(operation.isDestroy() || operation.isDestroy())
                return true;

            pdx = event.getDeserializedValue();
            if(pdx == null)
                return true;

            speed = (Integer)pdx.getField("speed");
            speedTotal += speed;
            count++;
        }

        Double newSpeedAvg = speedTotal/count;

        vehicleAggregationRegion.put("avgSpeed",newSpeedAvg);

        return true;
    }
}