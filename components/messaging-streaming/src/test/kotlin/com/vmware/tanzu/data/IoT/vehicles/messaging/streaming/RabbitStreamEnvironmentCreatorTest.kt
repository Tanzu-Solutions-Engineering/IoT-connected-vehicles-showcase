package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.EnvironmentBuilder
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamEnvironmentCreator
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
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
    private val port = 5552;
    private val host = "myHost";

    @BeforeEach
    internal fun setUp() {
        mockEnvironment = mock<Environment>{};

        environmentBuilder = mock<EnvironmentBuilder>{
            on{host(anyString())} doReturn it;
            on{port(anyInt())} doReturn it;
            on{build();} doReturn mockEnvironment;
        };


    }

    @Test
    fun create() {

        var subject = RabbitStreamEnvironmentCreator(host,port,environmentBuilder);
        var actual = subject.create();
        assertNotNull(actual);
        verify(environmentBuilder).host(anyString());
        verify(environmentBuilder).port(anyInt());

    }
}