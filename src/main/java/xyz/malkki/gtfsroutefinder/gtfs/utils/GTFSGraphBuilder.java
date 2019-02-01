package xyz.malkki.gtfsroutefinder.gtfs.utils;

import xyz.malkki.gtfsroutefinder.common.model.Geohash;
import xyz.malkki.gtfsroutefinder.common.utils.Indexer;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;
import xyz.malkki.gtfsroutefinder.gtfs.graph.GTFSGraph;
import xyz.malkki.gtfsroutefinder.gtfs.model.*;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.*;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class GTFSGraphBuilder {
    public static GTFSGraph buildFromFiles(String directory) throws IOException {
        File dir = new File(directory);

        if (isMissingFile(dir, "stops.txt") ||
            isMissingFile(dir, "routes.txt") ||
            isMissingFile(dir, "trips.txt") ||
            isMissingFile(dir, "stop_times.txt") ||
            isMissingFile(dir,  "calendar.txt")) {
            return null;
        }

        List<Stop> stops = Stop.parseFromFile(new File(dir, "stops.txt").getAbsolutePath());
        List<Route> routes = Route.parseFromFile(new File(dir, "routes.txt").getAbsolutePath());
        List<Trip> trips = Trip.parseFromFile(new File(dir, "trips.txt").getAbsolutePath());
        List<StopTime> stopTimes = StopTime.parseFromFile(new File(dir, "stop_times.txt").getAbsolutePath());
        List<Calendar> calendars = Calendar.parseFromFile(new File(dir, "calendar.txt").getAbsolutePath());

        File calendarDatesFile = new File(dir, "calendar_dates.txt");
        List<CalendarDate> calendarDates = calendarDatesFile.exists() ? CalendarDate.parseFromFile(calendarDatesFile.getAbsolutePath()) : Collections.emptyList();

        return new GTFSGraph(TimeZone.getTimeZone("Europe/Helsinki"), routes, trips, stops, stopTimes, calendars, calendarDates);
   }

    private static boolean isMissingFile(File directory, String file) {
        boolean contains = false;
        for (String f : directory.list()) {
            if (file.equals(f)) {
                contains = true;
            }
        }

        if (!contains) {
            System.out.println(String.format("ERROR! Missing %s from GTFS feed", file));
        }

        return !contains;
    }
}
