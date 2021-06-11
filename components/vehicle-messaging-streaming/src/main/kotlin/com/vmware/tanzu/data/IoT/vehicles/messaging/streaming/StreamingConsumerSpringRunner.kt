package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.MessageHandler
import com.rabbitmq.stream.OffsetSpecification
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Spring command line wrapper to registering a streaming consumer
 * @author Gregory Green
 */
@Component
class StreamingConsumerSpringRunner(
    @Value("\${rabbitmq.streaming.replay}")
    private val replay: Boolean,
    @Value("\${rabbitmq.streaming.host}")
    private val host: String,
    @Value("\${rabbitmq.streaming.port}")
    private val port: Int,
    @Value("\${rabbitmq.streaming.name}")
    private val streamName: String,
    @Value("\${spring.application.name}")
    private val applicationName: String,
    private val messageHandler: MessageHandler,
    private val creator: StreamCreation,
    private val offset: Long = 0,
    private val environment: Environment = Environment.builder()
        .host(host)
        .port(port).build()
) : CommandLineRunner {

    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    override fun run(vararg args: String?) {

        this.creator.create(streamName);

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
                .name(applicationName).autoCommitStrategy()
                .messageCountBeforeCommit(50_000)
                .flushInterval(Duration.ofSeconds(10))
                .builder()
                .messageHandler(messageHandler)
                .build();
        }
    }
}