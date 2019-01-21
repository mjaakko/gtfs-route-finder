package xyz.malkki.gtfsroutefinder.gtfs.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class GTFSTimeParserTest {
    @Test
    public void testCanParseTimeInCorrectFormat() {
        String time = "10:10:10";

        assertEquals(10 * 60 * 60 + 10 * 60 + 10, GTFSTimeParser.parseTime(time));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeFormatThrowsException1() {
        GTFSTimeParser.parseTime("öäöäöäöä");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimeFormatThrowsException2() {
        GTFSTimeParser.parseTime("a:b:c");
    }
}
