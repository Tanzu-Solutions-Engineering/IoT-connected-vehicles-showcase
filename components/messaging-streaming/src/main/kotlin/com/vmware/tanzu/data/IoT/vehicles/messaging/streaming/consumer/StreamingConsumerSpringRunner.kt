package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.consumer

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.MessageHandler
import com.rabbitmq.stream.OffsetSpecification
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.StreamSetup
import nyla.solutions.core.patterns.creational.Creator
import java.time.Duration

/**
 * Spring command line wrapper to registering a streaming consumer
 * @author Gregory Green
 */
//@Component
class StreamingConsumerSpringRunner(
//    @Value("\${rabbitmq.streaming.replay}")
    private val replay: Boolean,
//    @Value("\${rabbitmq.streaming.name}")
    private val streamName: String,
//    @Value("\${spring.application.name}")
    private val applicationName: String,
    private val messageHandler: MessageHandler,
    private val envCreator: Creator<Environment>,
    private val streamSetup: StreamSetup,
    private val offset: Long = 0,
//    @Value("\${rabbitmq.streaming.messageCountBeforeStorage:50000}")
    private var messageCountBeforeStorage: Int = 50_000,
//    @Value("\${rabbitmq.streaming.flushIntervalDurationSecs:10}")
    private var  flushIntervalDurationSecs: Long = 10,

    ) : Runnable {




    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    override fun run() {
        val environment = envCreator.create();

        this.streamSetup.initialize(streamName);

        if (replay) {
            println("=========== REPLAYING ALL STREAM  MESSAGES ======================");

            var consumer = environment.consumerBuilder()
                .stream(streamName)
                .offset(OffsetSpecification.offset(offset))
                .messageHandler(messageHandler)
                .build();
        } else {
            var consumer = environment.consumerBuilder()
                .stream(streamName)
                .name(applicationName).autoTrackingStrategy()
                .messageCountBeforeStorage(messageCountBeforeStorage)
                .flushInterval(Duration.ofSeconds(flushIntervalDurationSecs))
                //.messageCountBeforeCommit(50_000)
                //.flushInterval(Duration.ofSeconds(10))
                .builder()
                .messageHandler(messageHandler)
                .build();
        }
    }
}