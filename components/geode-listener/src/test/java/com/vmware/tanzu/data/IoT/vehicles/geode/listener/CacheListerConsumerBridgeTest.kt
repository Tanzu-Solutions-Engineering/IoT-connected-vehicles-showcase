package com.vmware.tanzu.data.IoT.vehicles.geode.listener

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator
import org.apache.geode.cache.EntryEvent
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.function.Consumer

/**
 * Test for CacheListerConsumerBridge
 * @author Gregory Green
 */
internal class CacheListerConsumerBridgeTest{

    private lateinit var  consumer: Consumer<Vehicle>
    private var event: EntryEvent<String, Vehicle>? = null
    private var vehicle: Vehicle? = null


    @BeforeEach
    fun setUp() {
        event = mock<EntryEvent<String, Vehicle>>()
        consumer = mock<Consumer<Vehicle>>()
        vehicle = JavaBeanGeneratorCreator.of(Vehicle::class.java).create()
    }

    @Test
    fun afterCreate_When_HasNewValue_Then_ConvertAndSend() {
        whenever(event!!.newValue).thenReturn(vehicle)
        val subject = CacheListerConsumerBridge<String,Vehicle>(consumer)
        subject.afterCreate(event)
        verify(consumer).accept(any())

    }

    @Test
    fun afterCreate_WhenValueIsNull_Then_DoNotConvertAndSend() {
        val subject = CacheListerConsumerBridge<String,Vehicle>(consumer)
        subject.afterCreate(event)
        verify(consumer, never()).accept(any())
    }


    @Test
    fun afterUpdate_When_HasNewValue_Then_ConvertAndSend() {
        whenever(event!!.newValue).thenReturn(vehicle)
        val subject = CacheListerConsumerBridge<String,Vehicle>(consumer)
        subject.afterUpdate(event)
        verify(consumer).accept(any())
    }

    @Test
    fun afterUpdate_WhenValueIsNull_Then_DoNotConvertAndSend() {
        val subject = CacheListerConsumerBridge<String,Vehicle>(consumer)
        subject.afterUpdate(event)
        verify(
            consumer,
            never()
        ).accept(any())
    }
}