package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer

import com.rabbitmq.stream.Message
import com.rabbitmq.stream.MessageHandler
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.util.Debugger
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
    /**
     * Processing a messaging handling
     * @param context the message context
     * @param message  the message
     */
    override fun handle(context: MessageHandler.Context?, message: Message?) {
        if (message == null)
            return;

        try{
            consumer.accept(
                converter.apply(message.bodyAsBinary)
            );
        }
        catch(e : Exception)
        {
            var stackTrace = Debugger.stackTrace(e);
            println("ERROR: publishingId: $message.publishingId properties: ${message.properties} STACKTRACE:${stackTrace}")
            throw e;
        }
    }
}