package xyz.malkki.gtfsroutefinder.common.model;

import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Simplified geohash, used for indexing location data
 */
public class Geohash {
    private String geohash;
    private int geohashLevel;

    /**
     * Creates a geohash with specified level from given coordinates
     * @param latitude Latitude
     * @param longitude Longitude
     * @param geohashLevel Geohash level
     */
    public Geohash(BigDecimal latitude, BigDecimal longitude, int geohashLevel) {
        String[] latitudeString = latitude.setScale(geohashLevel, RoundingMode.DOWN).toPlainString().split("\\.");
        String[] longitudeString = longitude.setScale(geohashLevel, RoundingMode.DOWN).toPlainString().split("\\.");

        StringBuilder sb = new StringBuilder();
        sb.append(latitudeString[0]);
        sb.append(";");
        sb.append(longitudeString[0]);

        for (int i = 0; i < geohashLevel; i++) {
            sb.append("/");
            sb.append(latitudeString[1].charAt(i));
            sb.append(longitudeString[1].charAt(i));
        }

        geohash = sb.toString();
        this.geohashLevel = geohashLevel;
    }

    /**
     * See {@link Geohash#Geohash(BigDecimal, BigDecimal, int)}
     * @param latitude
     * @param longitude
     * @param geohashLevel
     */
    public Geohash(double latitude, double longitude, int geohashLevel) {
        this(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude), geohashLevel);
    }

    /**
     * @return Geohash as string
     */
    public String getGeohash() {
        return geohash;
    }

    /**
     * @return Geohash level
     */
    public int getGeohashLevel() {
        return geohashLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Geohash geohash1 = (Geohash) o;
        return geohashLevel == geohash1.geohashLevel &&
                Objects.equals(geohash, geohash1.geohash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(geohash, geohashLevel);
    }

    @Override
    public String toString() {
        return getGeohash();
    }

    /**
     * Helper function for calculating 9 geohashes around the specified location
     * @param latitude Latitude of the location
     * @param longitude Location of the location
     * @param geohashLevel Geohash level
     * @return List of geohashes
     */
    public static List<Geohash> getSurroundingGeohashes(BigDecimal latitude, BigDecimal longitude, int geohashLevel) {
        List<Geohash> geohashes = new TiraArrayList<>(9);

        BigDecimal delta = calculateDelta(geohashLevel);

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                geohashes.add(new Geohash(
                        latitude.add(delta.multiply(BigDecimal.valueOf(i))),
                        longitude.add(delta.multiply(BigDecimal.valueOf(j))), geohashLevel));
            }
        }

        return geohashes;
    }

    private static BigDecimal calculateDelta(int geohashLevel) {
        BigDecimal delta = BigDecimal.ONE;
        for (int i = 1; i <= geohashLevel; i++) {
            delta = delta.divide(BigDecimal.TEN, i, RoundingMode.HALF_UP);
        }

        return delta;
    }
}