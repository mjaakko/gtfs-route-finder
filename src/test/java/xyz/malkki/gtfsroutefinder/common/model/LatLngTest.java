package xyz.malkki.gtfsroutefinder.common.model;

import org.junit.Assert;
import org.junit.Test;

public class LatLngTest {
    @Test(expected = IllegalArgumentException.class)
    public void exceptionWithTooSmallLatitude() {
        new LatLng(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWithTooLargeLatitude() {
        new LatLng(91, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWithTooSmallLongitude() {
        new LatLng(0, -181);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exceptionWithTooLargeLongitude() {
        new LatLng(0, 181);
    }

    @Test
    public void testDistanceLarge() {
        LatLng helsinki = new LatLng(60.3188566,24.9679443);
        LatLng hongKong = new LatLng(22.3152221,113.9342275);

        //100m delta
        Assert.assertEquals(7809.88*1000, helsinki.distanceTo(hongKong), 100);
    }

    @Test
    public void testDistanceSmall() {
        LatLng rautatientori = new LatLng(60.1709177,24.9390753);
        LatLng kamppi = new LatLng(60.1694297,24.9310702);

        //1m delta
        Assert.assertEquals(472.39, rautatientori.distanceTo(kamppi), 1);
    }
}
