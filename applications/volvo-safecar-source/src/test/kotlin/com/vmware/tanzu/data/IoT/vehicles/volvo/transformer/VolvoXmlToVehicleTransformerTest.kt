package com.vmware.tanzu.data.IoT.vehicles.volvo.transformer

import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import nyla.solutions.core.xml.XML
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class VolvoXmlToVehicleTransformerTest{
    @Test
    internal fun apply() {
        var expected = Vehicle(vin = "1212", speed = 99, odometer = 88
        , gpsLocation = GpsLocation( longitude = 33.33, latitude = 44.44));

        val xmlText = "<volvo>\n" +
                "    <VehicleMessages xmlns=\"http://ps.volvo.com/services/VehicleMessages/1_0\">\n" +
                "        <Message id=\"370765528\">\n" +
                "            <VehicleLocation>\n" +
                "                <Vin>${expected.vin}</Vin>\n" +
                "                <Timestamp>2018-02-13T23:53: 50.000Z</Timestamp>\n" +
                "                <Latitude>${expected.gpsLocation!!.latitude}</Latitude>\n" +
                "                <Longitude>${expected.gpsLocation!!.longitude}</Longitude>\n" +
                "                <Altitude>105</Altitude>\n" +
                "                <Heading>12</Heading>\n" +
                "                <Odometer>${expected.odometer}</Odometer>\n" +
                "                <EngineSpeed>608</EngineSpeed>\n" +
                "                <RoadSpeed>${expected.speed}</RoadSpeed>\n" +
                "                <EngineRunning>true</EngineRunning>\n" +
                "                <Event>STOP</Event>\n" +
                "                <IdleEngineHours>4288.456</IdleEngineHours>\n" +
                "                <TotalFuelUsed>52526.385859</TotalFuelUsed>\n" +
                "            </VehicleLocation>\n" +
                "        </Message>\n" +
                "    </VehicleMessages></volvo>";

        verify(xmlText, expected)

    }
    @Test
    internal fun apply_odometer_double() {
        var expected = Vehicle(vin = "1212", speed = 99, odometer = 245732
            , gpsLocation = GpsLocation( longitude = 33.33, latitude = 44.44));

        val xmlText = "<volvo>\n" +
                "    <VehicleMessages xmlns=\"http://ps.volvo.com/services/VehicleMessages/1_0\">\n" +
                "        <Message id=\"370765528\">\n" +
                "            <VehicleLocation>\n" +
                "                <Vin>${expected.vin}</Vin>\n" +
                "                <Timestamp>2018-02-13T23:53: 50.000Z</Timestamp>\n" +
                "                <Latitude>${expected.gpsLocation!!.latitude}</Latitude>\n" +
                "                <Longitude>${expected.gpsLocation!!.longitude}</Longitude>\n" +
                "                <Altitude>105</Altitude>\n" +
                "                <Heading>12</Heading>\n" +
                "                <Odometer>245732.491</Odometer>\n" +
                "                <EngineSpeed>608</EngineSpeed>\n" +
                "                <RoadSpeed>${expected.speed}</RoadSpeed>\n" +
                "                <EngineRunning>true</EngineRunning>\n" +
                "                <Event>STOP</Event>\n" +
                "                <IdleEngineHours>4288.456</IdleEngineHours>\n" +
                "                <TotalFuelUsed>52526.385859</TotalFuelUsed>\n" +
                "            </VehicleLocation>\n" +
                "        </Message>\n" +
                "    </VehicleMessages></volvo>";

        verify(xmlText, expected)

    }

    @Test
    internal fun apply_whenSpeed_blank_then_zero() {
        var expected = Vehicle(vin = "1212", speed = 0, odometer = 245732
            , gpsLocation = GpsLocation( longitude = 33.33, latitude = 44.44));

        val xmlText = "<volvo>\n" +
                "    <VehicleMessages xmlns=\"http://ps.volvo.com/services/VehicleMessages/1_0\">\n" +
                "        <Message id=\"370765528\">\n" +
                "            <VehicleLocation>\n" +
                "                <Vin>${expected.vin}</Vin>\n" +
                "                <Timestamp>2018-02-13T23:53: 50.000Z</Timestamp>\n" +
                "                <Latitude>${expected.gpsLocation!!.latitude}</Latitude>\n" +
                "                <Longitude>${expected.gpsLocation!!.longitude}</Longitude>\n" +
                "                <Altitude>105</Altitude>\n" +
                "                <Heading>12</Heading>\n" +
                "                <Odometer>245732.491</Odometer>\n" +
                "                <EngineSpeed>608</EngineSpeed>\n" +
                "                <RoadSpeed></RoadSpeed>\n" +
                "                <EngineRunning>true</EngineRunning>\n" +
                "                <Event>STOP</Event>\n" +
                "                <IdleEngineHours>4288.456</IdleEngineHours>\n" +
                "                <TotalFuelUsed>52526.385859</TotalFuelUsed>\n" +
                "            </VehicleLocation>\n" +
                "        </Message>\n" +
                "    </VehicleMessages></volvo>";

        verify(xmlText, expected)

    }

    private fun verify(xmlText: String, expected: Vehicle) {
        val subject = VolvoXmlToVehicleTransformer();

        val xml = XML(xmlText);
        val nodes = xml.searchNodesXPath("//VehicleLocation");
        assertNotNull(nodes);
        assertThat(nodes.length).isGreaterThan(0);


        var actual = subject.apply(nodes.item(0));
        assertEquals(expected, actual);
    }
    //

}