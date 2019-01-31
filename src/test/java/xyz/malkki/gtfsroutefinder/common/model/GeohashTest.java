package xyz.malkki.gtfsroutefinder.common.model;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class GeohashTest {
    @Test
    public void testGeohashWithLevel0() {
        Geohash geohash = new Geohash(60.29381, 24.598151, 0);

        assertEquals("60;24", geohash.getGeohash());
        assertEquals(0, geohash.getGeohashLevel());
    }

    @Test
    public void testGeohashWithLevel4() {
        Geohash geohash = new Geohash(60.29381, 24.598151, 4);

        assertEquals("60;24/25/99/38/81", geohash.getGeohash());
        assertEquals(4, geohash.getGeohashLevel());
    }

    @Test
    public void testGetSurroundingGeohashesWithLevel1() {
        List<Geohash> geohashes = Geohash.getSurroundingGeohashes(new BigDecimal("10"), new BigDecimal("10"), 1);

        assertTrue(geohashes.contains(new Geohash(new BigDecimal("9.9"), new BigDecimal("9.9"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("9.9"), new BigDecimal("10.0"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("9.9"), new BigDecimal("10.1"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.0"), new BigDecimal("9.9"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.0"), new BigDecimal("10.0"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.0"), new BigDecimal("10.1"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.1"), new BigDecimal("9.9"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.1"), new BigDecimal("10.0"), 1)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.1"), new BigDecimal("10.1"), 1)));
    }

    @Test
    public void testGetSurroundingGeohashesWithLevel2() {
        List<Geohash> geohashes = Geohash.getSurroundingGeohashes(new BigDecimal("10"), new BigDecimal("10"), 2);

        assertTrue(geohashes.contains(new Geohash(new BigDecimal("9.99"), new BigDecimal("9.99"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("9.99"), new BigDecimal("10.00"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("9.99"), new BigDecimal("10.01"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.0"), new BigDecimal("9.99"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.0"), new BigDecimal("10.00"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.0"), new BigDecimal("10.01"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.01"), new BigDecimal("9.99"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.01"), new BigDecimal("10.00"), 2)));
        assertTrue(geohashes.contains(new Geohash(new BigDecimal("10.01"), new BigDecimal("10.01"), 2)));
    }
}
