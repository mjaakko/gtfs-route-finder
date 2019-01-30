package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Route;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class RouteTest {
    private Route route;

    @Before
    public void setup() {
        route = new Route("test", "test_name");
    }

    @Test
    public void testHashCodeIsSame() {
        Route other = new Route("test", "test_name");

        assertEquals(route.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        Route other = new Route("test", "test_name");

        assertTrue(route.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        Route other = new Route("fsdaoifnsaf", "test_name");

        assertNotEquals(route.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        Route other = new Route("fsdaoifnsaf", "gfdsgsdfg");

        assertFalse(route.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("test", route.getId());
        assertEquals("test_name", route.getName());
    }
}
