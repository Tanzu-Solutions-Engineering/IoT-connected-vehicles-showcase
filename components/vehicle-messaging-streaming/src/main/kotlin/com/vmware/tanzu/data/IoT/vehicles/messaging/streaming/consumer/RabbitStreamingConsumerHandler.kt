package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer

import com.rabbitmq.stream.Message
import com.rabbitmq.stream.MessageHandler
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.springframework.stereotype.Component
import java.util.function.Consumer
import java.util.function.Function

/**
 * Adapter message handler to a consumer
 * @author Gregory Green
 */
@Component
class RabbitStreamingConsumerHandler(
    private val consumer: Consumer<Vehicle>,
    private val converter: Function<ByteArray, Vehicle>) :
    MessageHandler
{
    override fun handle(context: MessageHandler.Context?, message: Message?) {
        if (message == null)
            return;

        consumer.accept(
            converter.apply(message.bodyAsBinary)
        );

        println("CONSUMED: publishingId: $message.publishingId properties: ${message.properties}")
    }
}