package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing

import com.rabbitmq.stream.*
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import nyla.solutions.core.patterns.creational.Creator
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.function.Function

/**
 * Message handler to delegate input to a function and returns the output
 * binary.
 *
 * @author Gregory Green
 */
@Component
class RabbitStreamingRoutingFunctionHandler(
    @Value("\${rabbitmq.streaming.routing.input.stream.name}")
    private val inputStreamName: String,
    private val inputStreamSetup: StreamSetup,
    private val inputEnvCreator: Creator<Environment>,
    @Value("\${rabbitmq.streaming.routing.output.stream.name}")
    private val outputStreamName: String,
    private val outputEnvCreator: Creator<Environment>,
    private val outputStreamSetup: StreamSetup,
    private val routingFunction: Function<ByteArray, ByteArray>,
    @Value("\${spring.application.name}")
    private val applicationName: String,
    @Value("\${rabbitmq.streaming.routing.input.replay.offset}")
    private val offset: Long = 0,
    @Value("\${rabbitmq.streaming.routing.input.replay.bool}")
    private val replay: Boolean = false,
    private val handler: ConfirmationHandler = ConfirmationHandler { status ->
        if (!status.isConfirmed)
            println("ERROR: $status.code NOT confirmed}")
    },
    @Value("\${rabbitmq.streaming.routing.input.messageCountBeforeStorage:50000}")
    private var messageCountBeforeStorage: Int = 50_000,
    @Value("\${rabbitmq.streaming.routing.input.flushIntervalDurationSecs:10}")
    private var flushIntervalDurationSecs: Long = 10
) : MessageHandler, AutoCloseable {


    private var consumer: Consumer;
    private var producer: Producer;

    init {
        var inEnv = inputEnvCreator.create();
        inputStreamSetup.initialize(inputStreamName);
        outputStreamSetup.initialize(outputStreamName);

        if (replay) {
            println("=========== REPLAYING ALL STREAM  MESSAGES ======================");

            consumer = inEnv.consumerBuilder()
                .stream(inputStreamName)
                .offset(OffsetSpecification.offset(offset))
                .messageHandler(this)
                .build();
        } else {
            var builder = inEnv.consumerBuilder()
                .stream(inputStreamName)
                .name(applicationName).autoTrackingStrategy()
                .messageCountBeforeStorage(messageCountBeforeStorage)
                .flushInterval(Duration.ofSeconds(flushIntervalDurationSecs))
                .builder()
                .messageHandler(this);

            consumer = builder.build();
        }

        Thread.sleep(1000);

        producer = outputEnvCreator.create().producerBuilder().stream(outputStreamName).build()
    }
    /**
     * Callback for an inbound message.
     *
     * @param context context on the message
     * @param message the message
     */
    override fun handle(context: MessageHandler.Context?, message: Message?) {

        var output = routingFunction.apply(message!!.bodyAsBinary);
        val msg = producer.messageBuilder().addData(output).build();

        producer.send(msg, handler );
    }

    override fun close() {
        try{producer.close()} finally{}
        try{consumer.close()} finally {}
    }
}