package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.StreamException
import nyla.solutions.core.patterns.creational.BuilderDirector
import org.springframework.stereotype.Component

/**
 * Create an instance of a stream
 * @author Gregory Green
 */
class RabbitStreamCreator(private val environment: Environment) : StreamCreation {
    /**
     * Initialize the stream name
     * @param streamName the stream name
     */
    override fun create(streamName: String) {
        val streamNameCreator = environment.streamCreator()
            .stream(streamName);

        try{
            streamNameCreator.create()
        }
        catch(e : StreamException)
        {
            if(e.message == null)
                throw e;

            if(!e.message!!.contains("STREAM_ALREADY_EXISTS"))
                throw e;
        }
    }
}