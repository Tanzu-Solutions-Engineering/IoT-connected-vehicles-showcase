package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing

import com.rabbitmq.stream.*
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import nyla.solutions.core.patterns.creational.Creator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.function.Function;

/**
 * Test for RabbitStreamingRoutingFunctionHandler
 * @author Gregory Green
 */
internal class RabbitStreamingRoutingFunctionHandlerTest{
    private var replay: Boolean = false;
    private val offset  = 0L;
    private var applicationName: String = "app";
    private var context: MessageHandler.Context? =  mock<MessageHandler.Context>();
    private var inputStreamName = "in";
    private var outputStreamName = "out";
    private lateinit var messageBuilder: MessageBuilder
    private lateinit var message: Message;
    private lateinit var autoConsumerBuilder: ConsumerBuilder;
    private lateinit var autoTrackingStrategy: ConsumerBuilder.AutoTrackingStrategy;
    private lateinit var consumer: Consumer;
    private lateinit var consumerBuilder: ConsumerBuilder;
    private lateinit var producerBuilder : ProducerBuilder;
    private lateinit var producer : Producer;
    private lateinit var inEnvironment: Environment;
    private lateinit var outEnvironment: Environment;
    private lateinit var mockFunction: Function<ByteArray,ByteArray>;
    private lateinit var inputEnvCreator: Creator<Environment>;
    private lateinit var outputEnvCreator: Creator<Environment>;
    private lateinit var inputStreamSetup: StreamSetup;
    private lateinit var outputStreamSetup: StreamSetup;
    private lateinit var subject : RabbitStreamingRoutingFunctionHandler;

    @BeforeEach
    internal fun setUp() {
        message = mock<Message>(){
            on{ bodyAsBinary} doReturn "test".toByteArray(Charset.defaultCharset())
        }

        messageBuilder = mock<MessageBuilder>{
            on{addData(any())} doReturn it;
            on{ build()} doReturn message;
        }
        consumer = mock<Consumer>{};
        producer = mock<Producer>{
            on{ messageBuilder()} doReturn messageBuilder;
        }

        autoConsumerBuilder = mock<ConsumerBuilder>{
            on{ messageHandler(any())} doReturn it;
            on{ build()} doReturn consumer;
        }
        autoTrackingStrategy = mock<ConsumerBuilder.AutoTrackingStrategy>(){
            on{ messageCountBeforeStorage(any())} doReturn it;
            on{ flushInterval(any())} doReturn it;
            on{ builder()} doReturn autoConsumerBuilder;
        }
        producerBuilder = mock<ProducerBuilder>(){
            on{stream(anyString())} doReturn it;
            on{build()} doReturn producer;
        }
        consumerBuilder = mock<ConsumerBuilder>(){
            on{ stream(anyString())} doReturn it;
            on { name(anyString()) } doReturn it;
            on{ offset(any())} doReturn it;
            on{ messageHandler(any())} doReturn it;
            on { autoTrackingStrategy()} doReturn autoTrackingStrategy;
            on{ build()} doReturn consumer;

        }
        inEnvironment = mock<Environment>(){
            on{consumerBuilder()} doReturn consumerBuilder;
        }
        outEnvironment = mock<Environment>(){
            on{producerBuilder()} doReturn producerBuilder;
        }

        mockFunction = mock<Function<ByteArray,ByteArray>>{
            on{apply(any())} doReturn "out".toByteArray(StandardCharsets.UTF_8);
        }
        inputEnvCreator = mock<Creator<Environment>>(){
            on{create()} doReturn inEnvironment;
        }
        outputEnvCreator = mock<Creator<Environment>>(){
            on { create()} doReturn outEnvironment;
        }

        inputStreamSetup = mock<StreamSetup>();
        outputStreamSetup = mock<StreamSetup>();

        subject = RabbitStreamingRoutingFunctionHandler(
            inputStreamName,
            inputStreamSetup,
            inputEnvCreator,
            outputStreamName,
            outputEnvCreator,
            outputStreamSetup,
            mockFunction,
            applicationName,
            offset,
            replay,
        );

    }

    @Test
    internal fun replayInit() {

        this.replay = true;
        this.setUp();

        verify(inEnvironment).consumerBuilder();
        verify(consumerBuilder).stream(anyString());
        verify(consumerBuilder, never()).name(anyString());
        verify(consumerBuilder).offset(any());
        verify(consumerBuilder).messageHandler(any());
        verify(consumerBuilder).build();
    }

    @Test
    @DisplayName("Given connect handler when close then consumer and producer closed")
    internal fun close() {
        subject.close();
        verify(producer).close();
        verify(consumer).close();

    }

    @Test
    @DisplayName("Given input and output setup when created then initialize called")
    internal fun init() {
        verify(inputEnvCreator).create();
        verify(outputEnvCreator).create();
        verify(inputStreamSetup).initialize(inputStreamName);
        verify(outputStreamSetup).initialize(outputStreamName);
        verify(inEnvironment).consumerBuilder();
        verify(consumerBuilder).stream(anyString());
        verify(consumerBuilder).name(anyString());
        verify(consumerBuilder).autoTrackingStrategy();
        verify(autoTrackingStrategy).messageCountBeforeStorage(any());
        verify(autoTrackingStrategy).flushInterval(any());
        verify(autoTrackingStrategy).builder();
        verify(autoConsumerBuilder).messageHandler(any())
        verify(autoConsumerBuilder).build();
    }

    @Test
    internal fun handle() {
        subject.handle(context,message);
        verify(mockFunction).apply(any());
    }
}