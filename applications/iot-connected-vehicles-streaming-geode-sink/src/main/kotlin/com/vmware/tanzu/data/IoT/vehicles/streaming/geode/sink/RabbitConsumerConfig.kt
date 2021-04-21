package com.vmware.tanzu.data.IoT.vehicles.streaming.geode.sink

import com.rabbitmq.stream.*
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import com.vmware.tanzu.data.IoT.vehicles.generator.BytesToVehicle
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.RabbitStreamingMessageHandler
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

/**
 * @author Gregory Green
 */
@Configuration
class RabbitConsumerConfig {

    @Value("\${spring.application.name}")
    private val applicationName: String = "VehicleStreamingGeodeSink";

    @Value("\${streamName}")
    private var streamName = "vehicleStream";


    @Value("\${replay}")
    private var replay : Boolean = false;

    @Bean
    fun messageHandler(@Qualifier("vehicleGemFireSink")consumer: java.util.function.Consumer<Vehicle>): MessageHandler
    {
        return RabbitStreamingMessageHandler(consumer,BytesToVehicle());
    }
    //-----------------------------------------------
    @Bean
    fun commandLineRunner(messageHandler: MessageHandler): CommandLineRunner
    {
        val environment: Environment = Environment.builder().build()
        var creator = environment.streamCreator().stream(streamName);

        try{
            creator.create()
        }
        catch(e : StreamException)
        {
            if(e.message == null)
                throw e;

            if(!e.message!!.contains("STREAM_ALREADY_EXISTS"))
                throw e;
        }

        return  CommandLineRunner {

            if(replay)
            {
                println("=========== REPLAYING ALL STREAM  MESSAGES ======================");

                var consumerBuilder = environment.consumerBuilder()
                    .stream(streamName)
                    .name(applicationName).
                     offset(OffsetSpecification.offset(0L))
                    .messageHandler(messageHandler)
                    .build();
            }
            else
            {
                var consumerBuilder = environment.consumerBuilder()
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
}