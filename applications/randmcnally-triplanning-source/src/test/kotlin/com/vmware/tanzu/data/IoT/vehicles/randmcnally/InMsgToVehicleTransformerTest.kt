package com.vmware.tanzu.data.IoT.vehicles.randmcnally

import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.xml.XML
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.w3c.dom.Node
import java.io.File

internal class InMsgToVehicleTransformerTest{

    private val expectedGpsLocation: GpsLocation? = GpsLocation(latitude = 34.03074,
                                                        longitude = -117.596786)

    private lateinit var subject: InMsgToVehicleTransformer;
    private lateinit var node: Node;

    @BeforeEach
    internal fun setUp() {

        val doc = XML.toDocument(File("src/test/resources/xml/Randmcnally-test.xml"));
        val nl = doc.getElementsByTagName("InboundMessage");
        node = nl.item(0);

        subject = InMsgToVehicleTransformer();
    }

    @Test
    internal fun transform() {
        val expected = Vehicle(vin = "3GGGGGGGGGGSGV9557"
            ,odometer = 232,
            speed = 46,
            gpsLocation = expectedGpsLocation
            )
        val actual = subject.apply(node);
        assertEquals(expected,actual);
    }
}