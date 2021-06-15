package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.EnvironmentBuilder
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Test for RabbitStreamEnvironmentCreator
 * @author Gregory Green
 */
internal class RabbitStreamEnvironmentCreatorTest {

    private lateinit var mockEnvironment: Environment;
    private lateinit var environmentBuilder: EnvironmentBuilder;
    private val uris = "rabbitmq-stream://guest:guest@localhost:5552/,rabbitmq-stream://guest:guest@localhost:5552/";
    private lateinit var subject : RabbitStreamEnvironmentCreator;

    @BeforeEach
    internal fun setUp() {
        mockEnvironment = mock<Environment>{};

        environmentBuilder = mock<EnvironmentBuilder>{
            on{uris(any())} doReturn it;
            on{build();} doReturn mockEnvironment;
        };


        subject = RabbitStreamEnvironmentCreator(uris,environmentBuilder);
    }

    @Test
    fun create() {

        var actual = subject.create();
        assertNotNull(actual);
        verify(environmentBuilder).uris(any());

    }

}