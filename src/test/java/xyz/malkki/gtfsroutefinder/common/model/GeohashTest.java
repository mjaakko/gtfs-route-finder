package xyz.malkki.gtfsroutefinder.common.model;

import org.junit.Test;

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
}
