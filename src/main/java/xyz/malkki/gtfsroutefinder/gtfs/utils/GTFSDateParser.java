package xyz.malkki.gtfsroutefinder.gtfs.utils;

import java.time.LocalDate;

public class GTFSDateParser {
    private GTFSDateParser() {}

    /**
     * Parses date values that are in format "YYYYMMDD"
     * @param time String that has date value in format "YYYYMMDD"
     * @return Local date
     */
    public static LocalDate parseDate(String time) {
        int year = Integer.parseInt(time.substring(0, 4));
        int month = Integer.parseInt(time.substring(4, 6));
        int date = Integer.parseInt(time.substring(6, 8));

        return LocalDate.of(year, month, date);
    }
}
