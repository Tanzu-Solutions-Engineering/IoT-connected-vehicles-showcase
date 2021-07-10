package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming

import com.rabbitmq.stream.Environment
import com.rabbitmq.stream.StreamCreator
import com.rabbitmq.stream.StreamException
import com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.creational.RabbitStreamSetup
import nyla.solutions.core.patterns.creational.Creator
import org.junit.jupiter.api.*
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*

/**
 * Test for RabbitStreamCreator
 * @author Gregory Green
 */
internal class RabbitStreamSetupTest {
    private val streamName = "stream";
    private lateinit var environmentCreator: Creator<Environment>;
    private lateinit var streamCreator: StreamCreator;
    private lateinit var environment: Environment;
    private val maxSegmentSizeMb = 500L;
    private val maxLengthGb = 10L;
    private val maxAgeHours = 24*5L;
    private lateinit var subject : RabbitStreamSetup;

    @BeforeEach
    internal fun setUp() {

        streamCreator = mock<StreamCreator>() {
            on { maxSegmentSizeBytes(any())} doReturn it;
            on { maxLengthBytes(any())} doReturn it;
            on { maxAge(any())} doReturn it;
            on { stream(anyString()) } doReturn it;
        }

        environment = mock<Environment> {
            on { streamCreator() } doReturn streamCreator;
        }

        environmentCreator = mock<Creator<Environment>>{
            on{ create()} doReturn environment;
        }

        subject = RabbitStreamSetup(environmentCreator,
            maxAgeHours,
            maxLengthGb,
            maxSegmentSizeMb
        );
    }

    @Test
    internal fun initialize() {

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
    @DisplayName("GIVEN streamCreator with precondition error WHEN create does PRECONDITION_FAILED exception Then Exception is ignored")
    internal fun precondition_throwsNoException() {

        streamCreator = mock<StreamCreator>() {
            on { maxSegmentSizeBytes(any())} doReturn it;
            on { maxLengthBytes(any())} doReturn it;
            on { maxAge(any())} doReturn it;
            on { stream(anyString()) } doReturn it;
            on {create() } doThrow (StreamException("PRECONDITION_FAILED"))
        }

        environment = mock<Environment> {
            on { streamCreator() } doReturn streamCreator;
        }

        environmentCreator = mock<Creator<Environment>>{
            on{ create()} doReturn environment;
        }


        subject = RabbitStreamSetup(environmentCreator,
            maxAgeHours,
            maxLengthGb,
            maxSegmentSizeMb
        );

        assertDoesNotThrow {  subject.initialize(streamName) }
    }
    @Test
    @DisplayName("GIVEN streamCreator with STREAM_ALREADY_EXISTS error WHEN create does STREAM_ALREADY_EXISTS exception Then Exception is ignored")
    internal fun alreadyExists_throwsNoException() {

        streamCreator = mock<StreamCreator>() {
            on { maxSegmentSizeBytes(any())} doReturn it;
            on { maxLengthBytes(any())} doReturn it;
            on { maxAge(any())} doReturn it;
            on { stream(anyString()) } doReturn it;
            on {create() } doThrow (StreamException("STREAM_ALREADY_EXISTS"))
        }

        environment = mock<Environment> {
            on { streamCreator() } doReturn streamCreator;
        }

        environmentCreator = mock<Creator<Environment>>{
            on{ create()} doReturn environment;
        }


        subject = RabbitStreamSetup(environmentCreator,
            maxAgeHours,
            maxLengthGb,
            maxSegmentSizeMb
        );

        assertDoesNotThrow {  subject.initialize(streamName) }
    }
}