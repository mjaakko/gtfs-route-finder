package xyz.malkki.gtfsroutefinder.common.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Simplified geohash, used for indexing location data
 */
public class Geohash {
    private String geohash;
    private int geohashLevel;

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

    public Geohash(double latitude, double longitude, int geohashLevel) {
        this(BigDecimal.valueOf(latitude), BigDecimal.valueOf(longitude), geohashLevel);
    }

    public String getGeohash() {
        return geohash;
    }

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
}
