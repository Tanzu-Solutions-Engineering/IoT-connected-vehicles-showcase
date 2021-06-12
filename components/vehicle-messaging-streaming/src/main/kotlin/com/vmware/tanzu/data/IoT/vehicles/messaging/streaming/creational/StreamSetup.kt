package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational

/**
 * Interface to initialize streams
 * @author Gregory Green
 */
interface StreamSetup{
    fun initialize(streamName: String);
}   