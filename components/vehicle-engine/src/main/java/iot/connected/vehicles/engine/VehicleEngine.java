package iot.connected.vehicles.engine;

import com.vmware.tanzu.data.IoT.vehicles.domains.GpsLocation;
import com.vmware.tanzu.data.IoT.vehicles.domains.Vehicle;
import nyla.solutions.core.patterns.creational.Creator;

import java.util.Random;

import static java.lang.Math.PI;
import static java.lang.Math.cos;

/**
 * Generated vehicle data
 * @author Gregory Green
 */
public class VehicleEngine implements Creator<Vehicle> {

    // United States are in range: Latitude from 19.50139 to 64.85694 and longitude from -161.75583 to -68.01197.
    private final Double angle = new Random().nextDouble(1.0,179.0);
    private final int radius = 6378137; // Radius of the earth in km

    private static final Double maxLatitude = 40.0;
    private static final Double  maxLongitude = -87.0;
    //minLatitude = 32.0
    private static final Double  minLatitude = 32.0;

    //minLongitude = -89.470;
    private static final Double  minLongitude = -89.470;

    private final Long odometer = new Random().nextLong(15000,500000);
    private final Double distanceIncrements;
    private final String vin;

    public VehicleEngine(Double distanceIncrements, String vin) {
        this.distanceIncrements = distanceIncrements;
        this.vin = vin;
    }

    /**
     * Creates an initial instance of the Vehicle
     * @return the initialized vehicle
     */
    public Vehicle create() {
        var latitude = new Random().nextDouble(minLatitude,maxLatitude);
        var longitude = new Random().nextDouble(minLongitude, maxLongitude);

        var latLong = getFinalLatLong(latitude,longitude, distanceIncrements);

        return Vehicle.builder().vin(vin)
                .speed(new Random().nextInt(15, 100))
                .temperature(new Random().nextInt(100,300))
                .odometer(this.odometer + distanceIncrements.longValue())
                .checkEngine(false)
                .gpsLocation(
                        GpsLocation
                                .builder()
                                .latitude(latLong[0])
                                .longitude(latLong[1])
                                .build())
                .build();
    }


    private Double[] getFinalLatLong(Double latitude ,
                                         Double longitude ,
                                         Double distanceMeters )
    {
        //Coordinate offsets in radians
        var dLat = distanceMeters/radius;
        var dLon = distanceMeters/(radius* cos(PI*latitude/180));

        //OffsetPosition, decimal degrees
        var latO = latitude + dLat * 180/ PI;
        var lonO = longitude + dLon * 180/PI;

        return new Double[]{latO, lonO};
    }

    /**
     * Change position of the vehicle based on the discount increments
     * @param vehicle the vehicle to move
     * @return the moved vehicle
     */
    public Vehicle move(Vehicle vehicle) {

        var newOdometer = vehicle.getOdometer() + distanceIncrements.longValue();

        vehicle.setSpeed(new Random().nextInt(15, 100));
        vehicle.setOdometer(newOdometer);

        var gpsLocation = vehicle.getGpsLocation();
        if(gpsLocation == null)
        {
//            val latLong = getFinalLatLong(vehicle.gpsLocation!!.latitude,vehicle.gpsLocation!!.longitude, distanceIncrements);
            vehicle.setGpsLocation( new GpsLocation(minLatitude, maxLongitude));
        }
        else
        {
            var latLong = getFinalLatLong(gpsLocation.getLatitude(),gpsLocation.getLongitude(), distanceIncrements);

            vehicle.getGpsLocation().setLatitude(latLong[0]);
            vehicle.getGpsLocation().setLongitude(latLong[1]);
        }

        return vehicle;
    }
}
