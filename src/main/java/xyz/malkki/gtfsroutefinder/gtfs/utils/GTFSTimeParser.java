package xyz.malkki.gtfsroutefinder.gtfs.utils;

public class GTFSTimeParser {
    private GTFSTimeParser() {}

    /**
     * Parses time values that are in format "HH:MM:SS"
     * @param time String that has time value in format "HH:MM:SS"
     * @return Time in seconds
     */
    public static int parseTime(String time) {
        String[] timeSplit = time.split(":");
        if (timeSplit.length != 3) {
            throw new IllegalArgumentException("Time value was not in format HH:MM:SS");
        }

        try {
            int hours = Integer.parseInt(timeSplit[0]);
            int minutes = Integer.parseInt(timeSplit[1]);
            int seconds = Integer.parseInt(timeSplit[2]);

            return hours * 60 * 60 + minutes * 60 + seconds;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Time contained values that were not integers", ex);
        }
    }
}
