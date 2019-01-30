package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.StopTime;

import static org.junit.Assert.*;

public class StopTimeTest {
    private StopTime stopTime;

    @Before
    public void setup() {
        stopTime = new StopTime("trip", 0, 1, "stop", 1);
    }

    @Test
    public void testHashCodeIsSame() {
        StopTime other = new StopTime("trip", 0, 1, "stop", 1);

        assertEquals(stopTime.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        StopTime other = new StopTime("trip", 0, 1, "stop", 1);

        assertTrue(stopTime.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        StopTime other = new StopTime("fdsaf", 3, 5, "gsadgas", 4);

        assertNotEquals(stopTime.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        StopTime other = new StopTime("fdsaf", 4, 6, "gdfsgs", 5);

        assertFalse(stopTime.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("trip", stopTime.getTripId());
        assertEquals(0, stopTime.getArrivalTime());
        assertEquals(1, stopTime.getDepartureTime());
        assertEquals("stop", stopTime.getStopId());
        assertEquals(1, stopTime.getStopSequence());
    }
}
