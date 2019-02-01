package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Route;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class RouteTest {
    private Route route;

    @Before
    public void setup() {
        route = new Route("test", "test_name", TransportMode.BUS);
    }

    @Test
    public void testHashCodeIsSame() {
        Route other = new Route("test", "test_name", TransportMode.BUS);

        assertEquals(route.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        Route other = new Route("test", "test_name", TransportMode.BUS);

        assertTrue(route.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        Route other = new Route("fsdaoifnsaf", "test_name", TransportMode.TRAM);

        assertNotEquals(route.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        Route other = new Route("fsdaoifnsaf", "gfdsgsdfg", TransportMode.TRAM);

        assertFalse(route.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("test", route.getId());
        assertEquals("test_name", route.getName());
        assertEquals(TransportMode.BUS, route.getMode());
    }
}
