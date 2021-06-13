package com.vmware.tanzu.data.IoT.vehicles.messaging.streaming.routing

import java.util.function.Function

/**
 * Shovel implementation to return input argument data
 * @author Gregory Green
 */
class ShovelFunction : Function<ByteArray,ByteArray> {
    /**
     * Applies this function to the given argument.
     *
     * @param input the function argument
     * @return the function result
     */
    override fun apply(input: ByteArray): ByteArray {
        return input;
    }
}