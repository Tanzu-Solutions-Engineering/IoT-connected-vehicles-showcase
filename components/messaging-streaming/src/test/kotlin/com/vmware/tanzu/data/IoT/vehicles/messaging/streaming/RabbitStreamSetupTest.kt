package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.StreamCreator
import com.rabbitmq.stream.StreamException
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamSetup
import nyla.solutions.core.patterns.creational.Creator
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

/**
 * Test for RabbitStreamCreator
 * @author Gregory Green
 */
internal class RabbitStreamSetupTest {
    private val maxSegmentSizeMb = 500L;
    private val maxLengthGb = 10L;
    private val maxAgeHours = 24*5L;

    @BeforeEach
    internal fun setUp() {
    }

    @Test
    internal fun createStream() {

        val streamCreator = mock<StreamCreator>() {
            on { maxSegmentSizeBytes(any())} doReturn it;
            on { maxLengthBytes(any())} doReturn it;
            on { maxAge(any())} doReturn it;
            on { stream(anyString()) } doReturn it;
        }

        val environment = mock<Environment> {
            on { streamCreator() } doReturn streamCreator;
        }

        val environmentCreator : Creator<Environment> = mock<Creator<Environment>>{
            on{ create()} doReturn environment;

        }

        var subject = RabbitStreamSetup(environmentCreator,
            maxAgeHours,
            maxLengthGb,
            maxSegmentSizeMb
        );

        val streamName = "stream";
        subject.initialize(streamName);
        verify(streamCreator).maxAge(any());
        verify(streamCreator).maxLengthBytes(any());
        verify(streamCreator).maxSegmentSizeBytes(any())
        verify(environmentCreator).create();
        verify(environment).streamCreator();
        verify(streamCreator).stream(streamName);
        verify(streamCreator).create();
    }

    @Test
    internal fun precondition() {
//        fail("TODO");
    }
}