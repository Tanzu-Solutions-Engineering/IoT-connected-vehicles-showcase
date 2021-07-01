package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Message
import com.rabbitmq.stream.MessageHandler
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer.RabbitStreamingConsumerHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.function.Consumer
import java.util.function.Function
import kotlin.concurrent.thread

/**
 * @author Gregory Green
 */
internal class RabbitStreamingConsumerHandlerTest{
    private var message: Message? = null;
    private var mockContext : MessageHandler.Context? = null;
    private lateinit var consumer: Consumer<Vehicle>;
    private lateinit var mockFunction: Function<ByteArray,Vehicle>;

    @BeforeEach
    internal fun setUp() {
        consumer = mock<Consumer<Vehicle>>(){};
        mockContext = mock<MessageHandler.Context>(){};
        mockFunction = mock<Function<ByteArray,Vehicle>>();
    }

    @Test
    internal fun handle_nullMessages() {
        var subject = RabbitStreamingConsumerHandler(consumer,mockFunction);
        subject.handle(mockContext,message);
        verify(consumer, never()).accept(any());
        verify(mockFunction,never()).apply(any());
    }

    @Test
    internal fun handle() {
        message =  mock<Message>();
        var subject = RabbitStreamingConsumerHandler(consumer,mockFunction);
        subject.handle(mockContext,message);

        Thread.sleep(1000);
        verify(mockFunction).apply(anyOrNull());
        verify(consumer).accept(anyOrNull());
    }


}