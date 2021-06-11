package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

/**
 * Interface to initialize streams
 * @author Gregory Green
 */
interface StreamCreation{
    fun create(streamName: String);
}