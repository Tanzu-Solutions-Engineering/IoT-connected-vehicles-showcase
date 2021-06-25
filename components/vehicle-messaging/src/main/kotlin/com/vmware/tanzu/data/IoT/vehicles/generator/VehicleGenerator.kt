package com.vmware.tanzu.data.IoT.vehicles.generator

import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.random.Random

/**
 * Generated vehicle data
 * @author Gregory Green
 */
class VehicleGenerator(
    private var distanceIncrements: Double,
    var vin: String = "",
    )
{

    /**
     * Constants
     */
    companion object {
        const val maxLatitude: Double = 40.0;
        const val maxLongitude: Double = -87.0;
        const val minLatitude: Double = 32.0;
        const val minLongitude: Double = -89.470;
    }

    private val odometer: Long = Random.nextLong(15000,500000);
    // United States are in range: Latitude from 19.50139 to 64.85694 and longitude from -161.75583 to -68.01197.
     private val angle: Double = Random.nextDouble(1.0,179.0);
    private val radius = 6378137; // Radius of the earth in km

    private fun deg2rad(deg : Double) : Double { return deg * (Math.PI / 180.0) }
    private fun rad2deg(rad : Double) : Double { return rad * (180.0 / Math.PI) }

    private fun getFinalLatLong(latitude : Double,
                                longitude : Double,
                                distanceMeters : Double): Array<Double>
    {


        //Coordinate offsets in radians
        val dLat = distanceMeters/radius;
        val dLon = distanceMeters/(radius* cos(PI*latitude/180))

        //OffsetPosition, decimal degrees
        val latO = latitude + dLat * 180/ PI;
        val lonO = longitude + dLon * 180/PI;

        return arrayOf(latO,lonO);
    }

    /**
     * Create an instance of vehicle
     * @return the generate vehicle details
     */
    fun create(): Vehicle {

        val latitude: Double = Random.nextDouble(Companion.minLatitude, Companion.maxLatitude);
        val longitude: Double = Random.nextDouble(Companion.minLongitude, Companion.maxLongitude)

        val latLong = getFinalLatLong(latitude,longitude, distanceIncrements);

//        distanceIncrements += distanceIncrements;

        return Vehicle( vin = vin,
            speed = Random.nextInt(15, 100),
            temperature = Random.nextInt(100,300),
            odometer = this.odometer + distanceIncrements.toLong(),
            gpsLocation = GpsLocation(latLong[0],latLong[1]));
    }

    fun move(vehicle: Vehicle, distanceIncrements: Double) : Vehicle{
        val latLong = getFinalLatLong(vehicle.gpsLocation!!.latitude,vehicle.gpsLocation!!.longitude, distanceIncrements);
        val newOdometer = vehicle.odometer + distanceIncrements.toLong();

        vehicle.speed = Random.nextInt(15, 100)
        vehicle.odometer = newOdometer;
        if(vehicle.gpsLocation == null)
        {
            vehicle.gpsLocation = GpsLocation(latLong[0], latLong[1]);
        }
        else
        {
            vehicle.gpsLocation!!.latitude = latLong[0]
            vehicle.gpsLocation!!.longitude = latLong[1]
        }

        return vehicle;
    }


}