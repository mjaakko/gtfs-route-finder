package xyz.malkki.gtfsroutefinder.common.model;

import java.util.Objects;

/**
 * Describes a geographic point
 */
public class LatLng {
    private static final int EARTH_RADIUS_IN_METERS = 6371 * 1000;

    private double latitude;
    private double longitude;

    public LatLng(double latitude, double longitude) {
        if (latitude < 0 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be in range [0,90]");
        }
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be in range [-180,180]");
        }

        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     *
     * @return Latitude of the point
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return Longitude of the point
     */
    public double getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LatLng latLng = (LatLng) o;
        return Double.compare(latLng.latitude, latitude) == 0 &&
                Double.compare(latLng.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "("+latitude+", "+longitude+")";
    }

    /**
     * Calculates distance between two geographic points
     * @param other Other location
     * @return Distance between the points in meters
     */
    public double distanceTo(LatLng other) {
        double latDistance = Math.toRadians(other.getLatitude() - latitude);
        double lonDistance = Math.toRadians(other.getLongitude() - longitude);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(other.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_IN_METERS * c;
    }
}
