package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Consumer
import com.rabbitmq.stream.ConsumerBuilder
import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.MessageHandler
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer.StreamingConsumerSpringRunner
import nyla.solutions.core.patterns.creational.Creator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*

/**
 * Unit test for StreamingConsumerSpringRunner
 * @author Gregory Green
 */
internal class StreamingConsumerSpringRunnerTest {

    private lateinit var streamSetup: StreamSetup;

    private lateinit var consumer: Consumer;

    private lateinit var messageHandler: MessageHandler;

    private lateinit var consumerBuilder : ConsumerBuilder;

    private lateinit var environment: Environment;

    private lateinit var environmentCreator : Creator<Environment>;

    private lateinit var strategyConsumerBuilder : ConsumerBuilder;
    private lateinit var autoTrackingStrategy : ConsumerBuilder.AutoTrackingStrategy;

    private var applicationName: String = "myapp";
    private var offset: Long = 23;
    private var streamName = "mystream";
    private var port: Int = 32;
    private var host: String = "host";
    private var replay: Boolean = true;

    @BeforeEach
    internal fun setUp() {

        consumer = mock<Consumer>();
        messageHandler = mock<MessageHandler>();
        strategyConsumerBuilder = mock<ConsumerBuilder>(){
            on{ messageHandler(any())} doReturn it;
            on{ build()} doReturn consumer;
        }


        autoTrackingStrategy = mock<ConsumerBuilder.AutoTrackingStrategy>{
            on { messageCountBeforeStorage(any())} doReturn it;
            on { flushInterval(any())} doReturn it;

            on{ builder()} doReturn strategyConsumerBuilder;
        }


        consumerBuilder = mock<ConsumerBuilder>(){
            on{ stream(any())} doReturn it;

            on{ offset(any())} doReturn it;
            on{ messageHandler(any())} doReturn it;
            on{ build()} doReturn consumer;
            on{ name(anyString())} doReturn it;
            on { autoTrackingStrategy()} doReturn autoTrackingStrategy;
        }

        environment = mock<Environment>{
            on{ consumerBuilder()} doReturn consumerBuilder;
        }
        streamSetup = mock<StreamSetup>();

        environmentCreator = mock<Creator<Environment>>{
            on{create()} doReturn environment;
        }
    }

    @Test
    @DisplayName("Given replay=true when run then offset set")
    fun replay() {
        replay = true;

        var subject = StreamingConsumerSpringRunner(
            replay,
            streamName,
            applicationName,
            messageHandler = messageHandler,
            envCreator = environmentCreator,
            streamSetup = streamSetup,
            offset = offset,
        );
        subject.run("");
        verify(streamSetup).initialize(streamName);
        verify(consumerBuilder).stream(any());
        verify(consumerBuilder).offset(any());
        verify(consumerBuilder).messageHandler(any());
        verify(consumerBuilder).build();
    }

    @Test
    @DisplayName("Given replay=false when do not set offset")
    fun replayFalse() {
        replay = false;

        var subject = StreamingConsumerSpringRunner(
            replay,
            streamName,
            applicationName,
            messageHandler = messageHandler,
            envCreator= environmentCreator,
            streamSetup = streamSetup,
            offset = offset,
        );
        subject.run("");
        verify(streamSetup).initialize(streamName);
        verify(consumerBuilder, never()).offset(any());
        verify(consumerBuilder).stream(any());
        verify(consumerBuilder).name(any());
        verify(consumerBuilder).autoTrackingStrategy();
        verify(autoTrackingStrategy).messageCountBeforeStorage(any());
        verify(autoTrackingStrategy).builder();
        verify(strategyConsumerBuilder).messageHandler(any());
        verify(strategyConsumerBuilder).build();
    }
}