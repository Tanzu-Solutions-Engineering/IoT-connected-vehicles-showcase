package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.EnvironmentBuilder
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*

/**
 * Test for RabbitStreamEnvironmentCreator
 * @author Gregory Green
 */
internal class RabbitStreamEnvironmentCreatorTest {

    private val rpcTimeout: Long = 15;
    private lateinit var mockEnvironment: Environment;
    private lateinit var environmentBuilder: EnvironmentBuilder;
    private val uris = "rabbitmq-stream://guest:guest@localhost:5552,rabbitmq-stream://guest:guest@localhost:5552";
    private lateinit var subject : RabbitStreamEnvironmentCreator;

    @BeforeEach
    internal fun setUp() {
        mockEnvironment = mock<Environment>{};

        environmentBuilder = mock<EnvironmentBuilder>{
            on{uris(any())} doReturn it;
            on{rpcTimeout(any())} doReturn it;
            on{build();} doReturn mockEnvironment;
        };


        subject = RabbitStreamEnvironmentCreator(uris,rpcTimeout,environmentBuilder);
    }

    @Test
    fun create() {

        var actual = subject.create();
        assertNotNull(actual);
        verify(environmentBuilder).uris(any());
        verify(environmentBuilder).rpcTimeout(any());

    }

    @Test
    internal fun create_impotent() {
        assertSame(subject.create(),subject.create());
        verify(environmentBuilder, times(1)).build()
    }
}