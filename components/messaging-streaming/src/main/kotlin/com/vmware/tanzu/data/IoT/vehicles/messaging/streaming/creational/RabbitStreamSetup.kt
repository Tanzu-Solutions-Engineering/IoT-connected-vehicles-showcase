package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational

import com.rabbitmq.stream.ByteCapacity
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.StreamException
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Create an instance of a stream
 * @author Gregory Green
 */
@Component
class RabbitStreamSetup(
    private val envCreator: Creator<Environment>,

    @Value("\${rabbitmq.streaming.stream.maxAgeHours}")
    private val maxAgeHours: Long,
    @Value("\${rabbitmq.streaming.stream.maxLengthGb}")
    private val maxLengthGb: Long,
    @Value("\${rabbitmq.streaming.stream.maxSegmentSizeMb}")
    private val maxSegmentSizeMb: Long
) : StreamSetup {
    /**
     * Initialize the stream name
     * @param streamName the stream name
     */
    override fun initialize(streamName: String) {
        val environment = envCreator.create();
        val streamNameCreator = environment.streamCreator()
            .stream(streamName)
            .maxAge(Duration.ofHours(maxAgeHours))
            .maxLengthBytes(ByteCapacity.GB(maxLengthGb))
            .maxSegmentSizeBytes(ByteCapacity.MB(maxSegmentSizeMb));
        try{
            streamNameCreator.create()
        }
        catch(e : StreamException)
        {
            if(e.message == null)
                throw e;

            if(!e.message!!.contains("STREAM_ALREADY_EXISTS")
                && !e.message!!.contains("PRECONDITION_FAILED"))
                throw e;
        }
    }
}