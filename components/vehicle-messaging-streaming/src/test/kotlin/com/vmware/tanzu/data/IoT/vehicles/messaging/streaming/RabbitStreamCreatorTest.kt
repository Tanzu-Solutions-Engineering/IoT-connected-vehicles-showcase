package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.StreamCreator
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Test for RabbitStreamCreator
 * @author Gregory Green
 */
internal class RabbitStreamCreatorTest {
    @Test
    internal fun createStream() {
        val streamCreator = mock<StreamCreator>() {
            on { stream(anyString()) } doReturn it;
        }

        val environment = mock<Environment> {
            on { streamCreator() } doReturn streamCreator;
        }

        var subject = RabbitStreamCreator(environment);

        val streamName = "stream";
        subject.create(streamName);
        verify(environment).streamCreator();
        verify(streamCreator).stream(streamName);
        verify(streamCreator).create();
    }
}