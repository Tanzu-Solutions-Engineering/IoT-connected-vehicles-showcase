package com.vmware.tanzu.data.IoT.vehicles.geotab.transformer

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

/**
 * Testing for GeotabVehicleGsonTransformer
 * @author Gregory Green
 */
internal class GeotabVehicleGsonTransformerTest {

    private val expectedGps: GpsLocation? = GpsLocation(25.23, 34.5);
    private val expectedSpeed: Int = 99;
    private val expectedVin: String = "hello";
    private lateinit var jsonObject: JsonObject;
    private lateinit var jsonObjectSpeedNull: JsonObject;
    private val subject: GeotabVehicleGsonTransformer = GeotabVehicleGsonTransformer();
    private lateinit var expected: Vehicle;
    private val gson = Gson();

    @BeforeEach
    internal fun setUp() {

        expected = Vehicle(vin = expectedVin, speed = expectedSpeed, gpsLocation = expectedGps)

        jsonObject = gson.fromJson(
            "{\n" +
                    "          \"latitude\": ${expected.gpsLocation!!.latitude},\n" +
                    "          \"longitude\": ${expected.gpsLocation!!.longitude},\n" +
                    "          \"speed\": ${expected.speed},\n" +
                    "          \"dateTime\": \"2018-02-14T00:01:32.000Z\",\n" +
                    "          \"device\": {\n" +
                    "            \"id\": \"b1197\",\n" +
                    "            \"vin\": \"${expected.vin}\"\n" +
                    "          },\n" +
                    "          \"id\": \"bDA808B3D\"\n" +
                    "        }", JsonObject::class.java
        );


        jsonObjectSpeedNull = gson.fromJson(
            "{\n" +
                    "          \"latitude\": ${expected.gpsLocation!!.latitude},\n" +
                    "          \"longitude\": ${expected.gpsLocation!!.longitude},\n" +
                    "          \"dateTime\": \"2018-02-14T00:01:32.000Z\",\n" +
                    "          \"device\": {\n" +
                    "            \"id\": \"b1197\",\n" +
                    "            \"vin\": \"${expected.vin}\"\n" +
                    "          },\n" +
                    "          \"id\": \"bDA808B3D\"\n" +
                    "        }", JsonObject::class.java
        );
    }

    @Test
    internal fun apply() {

        var actual = subject.apply(jsonObject);
        assertEquals(expected, actual);
    }

    @Test
    internal fun apply_whenSpeedNull_ThenValue0() {

        var actual = subject.apply(jsonObjectSpeedNull);
        assertEquals(0, actual.speed);
    }

    @Nested
    class GivenMissingGps
    {

    }
    @Test
    internal fun apply_whenLongitudeNull_ThenGpsLocationNull() {

        val jsonObjectCondition = gson.fromJson(
            "{\n" +
                    "          \"latitude\": ${expected.gpsLocation!!.latitude},\n" +
                    "          \"dateTime\": \"2018-02-14T00:01:32.000Z\",\n" +
                    "          \"device\": {\n" +
                    "            \"id\": \"b1197\",\n" +
                    "            \"vin\": \"${expected.vin}\"\n" +
                    "          },\n" +
                    "          \"id\": \"bDA808B3D\"\n" +
                    "        }", JsonObject::class.java
        );

        var actual = subject.apply(jsonObjectCondition);
        assertNull(actual.gpsLocation);
    }

    @Test
    internal fun apply_whenLatitudeNull_ThenGpsLocationNull() {

        val jsonObjectCondition = gson.fromJson(
            "{\n" +
                    "          \"longitude\": ${expected.gpsLocation!!.longitude},\n" +
                    "          \"dateTime\": \"2018-02-14T00:01:32.000Z\",\n" +
                    "          \"device\": {\n" +
                    "            \"id\": \"b1197\",\n" +
                    "            \"vin\": \"${expected.vin}\"\n" +
                    "          },\n" +
                    "          \"id\": \"bDA808B3D\"\n" +
                    "        }", JsonObject::class.java
        );

        var actual = subject.apply(jsonObjectCondition);
        assertNull(actual.gpsLocation);
    }

    @Test
    internal fun apply_whenLatitude_and_longitude_Null_ThenGpsLocationNull() {

        val jsonObjectCondition = gson.fromJson(
            "{\n" +
                    "          \"dateTime\": \"2018-02-14T00:01:32.000Z\",\n" +
                    "          \"device\": {\n" +
                    "            \"id\": \"b1197\",\n" +
                    "            \"vin\": \"${expected.vin}\"\n" +
                    "          },\n" +
                    "          \"id\": \"bDA808B3D\"\n" +
                    "        }", JsonObject::class.java
        );

        var actual = subject.apply(jsonObjectCondition);
        assertNull(actual.gpsLocation);
    }
}