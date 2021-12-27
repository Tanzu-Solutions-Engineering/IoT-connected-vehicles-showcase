package com.vmware.tanzu.data.IoT.vehicles.geode.listener

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.apache.geode.cache.EntryEvent
import org.apache.geode.cache.util.CacheListenerAdapter
import java.util.function.Consumer

/**
 * @author Gregory Green
 */
class CacheListerConsumerBridge<K,V>(private val consumer: Consumer<V>) : CacheListenerAdapter<K, V>() {

    override fun afterCreate(event: EntryEvent<K, V>?) {
        processEvent(event)
    }

    override fun afterUpdate(event: EntryEvent<K, V>?) {
        processEvent(event)
    }

    private fun processEvent(event: EntryEvent<K, V>?) {

        if(event == null)
            return

        var newValue: V = event.newValue ?: return

        consumer.accept(event.newValue)
    }

}