package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Trip;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class TripTest {
    private Trip trip;

    @Before
    public void setup() {
        trip = new Trip("route", "service", "id");
    }

    @Test
    public void testHashCodeIsSame() {
        Trip other = new Trip("route", "service", "id");

        assertEquals(trip.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        Trip other = new Trip("route", "service", "id");

        assertTrue(trip.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        Trip other = new Trip("fsdaoifnsaf", "fsadfr", "fosaindf");

        assertNotEquals(trip.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        Trip other = new Trip("fsdaoifnsaf", "gfdsgsdfg", "fosaidnfas");

        assertFalse(trip.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("route", trip.getRouteId());
        assertEquals("service", trip.getServiceId());
        assertEquals("id", trip.getId());
    }

    @Test
    public void testParseFromFile() throws IOException {
        List<Trip> trips = Trip.parseFromFile(getClass().getClassLoader().getResource("gtfs/trips.txt").getFile());

        assertEquals(1, trips.size());
        assertEquals("1001", trips.get(0).getRouteId());
    }
}
