package xyz.malkki.gtfsroutefinder.gtfs.utils;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ServiceDatesTest {
    private ServiceDates serviceDates;

    @Before
    public void setup() {
        serviceDates = new ServiceDates("test", LocalDate.of(2019, 1, 1), LocalDate.of(2019, 1, 7),
                false, false, false, true, true, false, false,
                Arrays.asList(LocalDate.of(2019, 1, 15)), Arrays.asList(LocalDate.of(2019, 1, 4)));
    }

    @Test
    public void testServiceRunsOnAdditionalDay() {
        assertTrue(serviceDates.runsOn(LocalDate.of(2019, 1, 15)));
    }

    @Test
    public void testServiceDoesNotRunBeforeFromDate() {
        assertFalse(serviceDates.runsOn(LocalDate.of(2018, 1, 15)));
    }

    @Test
    public void testServiceDoesNotRunAfterToDate() {
        assertFalse(serviceDates.runsOn(LocalDate.of(2020, 1, 15)));
    }

    @Test
    public void testServiceDoesNotRunOnExceptionDay() {
        assertFalse(serviceDates.runsOn(LocalDate.of(2019, 1, 4)));
    }

    @Test
    public void testServiceDoesNotRunOnUnavailableDayOfWeek() {
        assertFalse(serviceDates.runsOn(LocalDate.of(2019, 1, 2)));
    }

    @Test
    public void testServiceRunsOnAvailableDayOfWeek() {
        assertTrue(serviceDates.runsOn(LocalDate.of(2019, 1, 3)));
    }
}
