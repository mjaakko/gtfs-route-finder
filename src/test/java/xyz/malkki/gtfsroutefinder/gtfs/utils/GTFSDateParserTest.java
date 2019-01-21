package xyz.malkki.gtfsroutefinder.gtfs.utils;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class GTFSDateParserTest {
    @Test
    public void testParseDateInCorrectFormat() {
        assertEquals(LocalDate.of(2019, 1, 21), GTFSDateParser.parseDate("20190121"));
    }
}
