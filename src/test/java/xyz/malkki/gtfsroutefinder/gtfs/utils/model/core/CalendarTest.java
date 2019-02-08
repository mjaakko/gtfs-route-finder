package xyz.malkki.gtfsroutefinder.gtfs.utils.model.core;

import org.junit.Before;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Route;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class CalendarTest {
    private Calendar calendar;

    @Before
    public void setup() {
        calendar = new Calendar("test", true, true, true, true, true, true, true, LocalDate.of(2000, 1, 1), LocalDate.of(2100, 1, 1));
    }

    @Test
    public void testHashCodeIsSame() {
        Calendar other = new Calendar("test", true, true, true, true, true, true, true, LocalDate.of(2000, 1, 1), LocalDate.of(2100, 1, 1));

        assertEquals(calendar.hashCode(), other.hashCode());
    }

    @Test
    public void testEqualsWhenSame() {
        Calendar other = new Calendar("test", true, true, true, true, true, true, true, LocalDate.of(2000, 1, 1), LocalDate.of(2100, 1, 1));

        assertTrue(calendar.equals(other));
    }

    @Test
    public void testHashCodeIsNotSame() {
        Calendar other = new Calendar("oniotastasetrast", true, true, true, true, true, true, true, LocalDate.of(2000, 1, 1), LocalDate.of(2100, 1, 1));

        assertNotEquals(calendar.hashCode(), other.hashCode());
    }

    @Test
    public void testNotEqualsWhenNotSame() {
        Calendar other = new Calendar("fasghayha", false, false, false, false, false, false, false, LocalDate.of(525, 1, 1), LocalDate.of(65476, 1, 1));

        assertFalse(calendar.equals(other));
    }

    @Test
    public void testGetters() {
        assertEquals("test", calendar.getServiceId());
        assertTrue(calendar.isMonday());
        assertTrue(calendar.isTuesday());
        assertTrue(calendar.isWednesday());
        assertTrue(calendar.isThursday());
        assertTrue(calendar.isFriday());
        assertTrue(calendar.isSaturday());
        assertTrue(calendar.isSunday());
        assertEquals(LocalDate.of(2000, 1, 1), calendar.getStartDate());
        assertEquals(LocalDate.of(2100, 1, 1), calendar.getEndDate());
    }

    @Test
    public void testParseFromFile() throws IOException {
        List<Calendar> calendars = Calendar.parseFromFile(getClass().getClassLoader().getResource("gtfs/calendar.txt").getFile());

        assertEquals(1, calendars.size());
        assertEquals("1001_20190118_20190303_Ke", calendars.get(0).getServiceId());
    }
}
