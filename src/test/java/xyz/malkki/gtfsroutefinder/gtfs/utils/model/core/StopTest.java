package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class StopTest {
    private Stop stop;

    @Before
    public void setup() {
        stop = new Stop("test", "test_name", new LatLng(0, 0));
    }

    @Test
    public void testHashCodeIsSame() {
        Stop other = new Stop("test", "test_name", new LatLng(0, 0));

        assertEquals(stop.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        Stop other = new Stop("test", "test_name", new LatLng(0, 0));

        assertTrue(stop.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        Stop other = new Stop("fsdaoifnsaf", "fsadfr", new LatLng(36, 42));

        assertNotEquals(stop.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        Stop other = new Stop("fsdaoifnsaf", "gfdsgsdfg", new LatLng(1, 55));

        assertFalse(stop.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("test", stop.getId());
        assertEquals("test_name", stop.getName());
        assertEquals(new LatLng(0, 0), stop.getLocation());
    }
}
