package com.vmware.tanzu.data.IoT.vehicles.generator

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import org.junit.jupiter.api.Test

/**
 * @author Gregory Green
 */
class PerformanceTest {
    @Test
    internal fun throughput() {

        val registry: MeterRegistry = SimpleMeterRegistry()

        val counter: Counter = Counter
            .builder("counter")
            .baseUnit("beans") // optional
            .description("a description of what this counter does") // optional
            .tags("region", "test") // optional
            .register(registry);

        for (i in 0..100)
        {
            counter.increment();
        }


        println("summary:"+registry.summary("counter"));
    }
}