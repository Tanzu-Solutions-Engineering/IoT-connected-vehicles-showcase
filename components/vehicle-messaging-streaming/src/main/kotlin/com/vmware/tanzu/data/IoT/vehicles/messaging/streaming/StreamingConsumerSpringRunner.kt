package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.MessageHandler
import com.rabbitmq.stream.OffsetSpecification
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * @author Gregory Green
 */
@Component
class StreamingConsumerSpringRunner(
    private val replay: Boolean,
    private val host: String,
    private val port: Int,
    private val streamName: String,
    private val applicationName: String,
    val messageHandler: MessageHandler,
    private val offset: Long = 0,
    private val environment: Environment = Environment.builder()
        .host(host)
        .port(port).build()) : CommandLineRunner {

    /**
     * Callback used to run the bean.
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    override fun run(vararg args: String?) {

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