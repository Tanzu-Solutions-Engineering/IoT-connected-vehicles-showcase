package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.nio.charset.StandardCharsets

/**
 * Test for ShovelFunction
 * @author Gregory Green
 */
internal class ShovelFunctionTest {

    @Test
    fun apply() {
        var subject = ShovelFunction();
        val expected = "expected".toByteArray(StandardCharsets.UTF_8);
        val actual = subject.apply(expected);
        assertEquals(expected,actual);
    }
}