package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.CalendarDate;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CalendarDateTest {
    private CalendarDate calendarDate;

    @Before
    public void setup() {
        calendarDate = new CalendarDate("test", LocalDate.of(2000, 1, 1), true);
    }

    @Test
    public void testHashCodeIsSame() {
        CalendarDate other = new CalendarDate("test", LocalDate.of(2000, 1, 1), true);

        assertEquals(calendarDate.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        CalendarDate other = new CalendarDate("test", LocalDate.of(2000, 1, 1), true);

        assertTrue(calendarDate.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        CalendarDate other = new CalendarDate("fsadfasga", LocalDate.of(2000, 1, 1), true);

        assertNotEquals(calendarDate.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        CalendarDate other = new CalendarDate("fdsagasgha", LocalDate.of(5346, 1, 1), false);

        assertFalse(calendarDate.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("test", calendarDate.getServiceId());
        assertEquals(LocalDate.of(2000, 1, 1), calendarDate.getDate());
        assertTrue(calendarDate.isAvailable());
    }
}
