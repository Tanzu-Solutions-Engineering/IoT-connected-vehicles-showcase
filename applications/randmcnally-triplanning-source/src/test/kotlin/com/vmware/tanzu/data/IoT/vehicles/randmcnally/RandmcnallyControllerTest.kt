package com.vmware.tanzu.data.IoT.vehicles.randmcnally

import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.io.IO
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.w3c.dom.Node
import java.util.*
import java.util.function.Function

internal class RandmcnallyControllerTest{

    private var xml: String = "";

    private lateinit var mockQueue: Queue<Vehicle>;
    private lateinit var mockTransformer: Function<Node, Vehicle>;
    private lateinit var subject : RandmcnallyController;
    private val vehicle = Vehicle();

    @BeforeEach
    internal fun setUp() {

        mockTransformer= mock(Function::class.java) as Function<Node, Vehicle>;
        `when`(mockTransformer.apply(any())).thenReturn(vehicle);
        mockQueue= mock(Queue::class.java) as Queue<Vehicle>;
        xml = IO.readFile("src/test/resources/xml/Randmcnally-test.xml");
        subject = RandmcnallyController(mockQueue,mockTransformer);
    }

    @Test
    internal fun load() {
        subject.load(xml);
        verify(mockTransformer, times(2)).apply(any(Node::class.java));
        verify(mockQueue,times(2)).add(vehicle);
    }
}