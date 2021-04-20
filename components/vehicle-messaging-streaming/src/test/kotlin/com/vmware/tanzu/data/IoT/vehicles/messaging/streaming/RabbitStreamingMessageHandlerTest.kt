package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Message
import com.rabbitmq.stream.MessageHandler
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.function.Consumer
import java.util.function.Function

/**
 * @author Gregory Green
 */
internal class RabbitStreamingMessageHandlerTest{
    private var message: Message? = null;
    private var context: MessageHandler.Context? = null;
    private lateinit var consumer: Consumer<Vehicle>;
    private lateinit var mockFunction: Function<ByteArray,Vehicle>;

    @BeforeEach
    internal fun setUp() {
        consumer = mock<Consumer<Vehicle>>(){};
        mockFunction = mock<Function<ByteArray,Vehicle>>();
    }

    @Test
    internal fun handle_nullMessages() {
        var subject = RabbitStreamingMessageHandler(consumer,mockFunction);
        subject.handle(context,message);
        verify(consumer, never()).accept(any());
        verify(mockFunction,never()).apply(any());
    }

    @Test
    internal fun handle() {
        message =  mock<Message>();
        var subject = RabbitStreamingMessageHandler(consumer,mockFunction);
        subject.handle(context,message);
        verify(mockFunction).apply(anyOrNull());
        verify(consumer).accept(anyOrNull());
    }
}