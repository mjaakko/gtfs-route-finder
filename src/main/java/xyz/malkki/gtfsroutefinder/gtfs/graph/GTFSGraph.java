package xyz.malkki.gtfsroutefinder.gtfs.graph;

import xyz.malkki.gtfsroutefinder.common.model.Geohash;
import xyz.malkki.gtfsroutefinder.common.utils.Indexer;
import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
import xyz.malkki.gtfsroutefinder.datastructures.TiraHashMap;
import xyz.malkki.gtfsroutefinder.datastructures.TiraLinkedList;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.*;
import xyz.malkki.gtfsroutefinder.gtfs.utils.ServiceDates;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GTFSGraph extends Graph<Stop> {
    private static final int STOP_INDEX_GEOHASH_LEVEL = 2;

    private static final double AVERAGE_WALKING_SPEED_MS = 1.4;
    private static final double MAX_WALKING_DISTANCE_IN_METERS = 500;

    /**
     * Time zone where the graph is based in
     */
    private TimeZone timeZone;

    /**
     * Stops indexed by geohash (e.g. 60;24/19/28)
     */
    private Indexer<Stop, Geohash> stopByGeohashIndex;

    private Indexer<StopTime, String> stopTimesByStopIdIndex;
    private Indexer<StopTime, String> stopTimesByTripIdIndex;

    private Map<String, ServiceDates> serviceDates;

    private Map<String, Stop> stops;

    private Map<String, Route> routes;

    private Map<String, Trip> trips;

    /**
     * Construct a new GTFS graph
     * @param timeZone Time zone where the graph is based
     * @param routes List of routes
     * @param trips List of trips
     * @param stops List of stops
     * @param stopTimes List of arrival and departure times at stops
     * @param calendars List of date ranges when public transportation services are available
     * @param calendarDates List of exceptions to public transportation services
     */
    public GTFSGraph(TimeZone timeZone, List<Route> routes, List<Trip> trips, List<Stop> stops, List<StopTime> stopTimes, List<Calendar> calendars, List<CalendarDate> calendarDates) {
        this.timeZone = timeZone;

        stopByGeohashIndex = Indexer.buildFromColletion(stops, stop -> new Geohash(stop.getLocation().getLatitude(), stop.getLocation().getLongitude(), STOP_INDEX_GEOHASH_LEVEL));

        stopTimesByStopIdIndex = Indexer.buildFromColletion(stopTimes, StopTime::getStopId);
        stopTimesByTripIdIndex = Indexer.buildFromColletion(stopTimes, StopTime::getTripId);

        Map<String, List<CalendarDate>> calendarDatesAsMap = new TiraHashMap<>(calendars.size());
        calendarDates.forEach(calendarDate -> {
            List<CalendarDate> list = calendarDatesAsMap.computeIfAbsent(calendarDate.getServiceId(), k -> new TiraLinkedList<>());
            list.add(calendarDate);
        });

        serviceDates = new TiraHashMap<>(calendars.size());
        calendars.forEach(calendar -> {
            List<LocalDate> additions = calendarDatesAsMap.getOrDefault(calendar.getServiceId(), Collections.emptyList())
                    .stream()
                    .filter(CalendarDate::isAvailable)
                    .map(CalendarDate::getDate)
                    .collect(Collectors.toCollection(() -> new TiraArrayList<>()));

            List<LocalDate> exceptions = calendarDatesAsMap.getOrDefault(calendar.getServiceId(), Collections.emptyList())
                    .stream()
                    .filter(calendarDate -> !calendarDate.isAvailable())
                    .map(CalendarDate::getDate)
                    .collect(Collectors.toCollection(() -> new TiraArrayList<>()));

            serviceDates.put(calendar.getServiceId(), new ServiceDates(calendar.getServiceId(), calendar.getStartDate(), calendar.getEndDate(),
                    calendar.isMonday(), calendar.isTuesday(), calendar.isWednesday(), calendar.isThursday(), calendar.isFriday(), calendar.isSaturday(), calendar.isSunday(), additions, exceptions));
        });

        this.stops = new TiraHashMap<>(stops.size());
        stops.forEach(stop -> this.stops.put(stop.getId(), stop));

        this.routes = new TiraHashMap<>(routes.size());
        routes.forEach(route -> this.routes.put(route.getId(), route));

        this.trips = new TiraHashMap<>(trips.size());
        trips.forEach(trip -> this.trips.put(trip.getId(), trip));
    }

    /**
     * Finds nearby stops that can be reached by walking (distance < 500m)
     * @param stop Search stops near this stop
     * @param found List of already found stops
     * @return
     */
    private List<Edge<Stop>> getWalkingEdgesFromStop(long timeAtStop, Stop stop, Set<Stop> found) {
        //Calculate a list of geohashes around the stop
        List<Geohash> nearbyGeohashes = Geohash.getSurroundingGeohashes(
                BigDecimal.valueOf(stop.getLocation().getLatitude()),
                BigDecimal.valueOf(stop.getLocation().getLongitude()), STOP_INDEX_GEOHASH_LEVEL);

        return nearbyGeohashes.stream()
                //Get all stops inside the geohashes
                .flatMap(geohash -> stopByGeohashIndex.getItems(geohash).stream())
                //Filter stops that have been already found
                .filter(stopFromIndex -> !found.contains(stopFromIndex))
                //Filter stops that are too far
                .filter(stopFromIndex -> stopFromIndex.getLocation().distanceTo(stop.getLocation()) <= MAX_WALKING_DISTANCE_IN_METERS
                        && !stopFromIndex.getId().equals(stop.getId()))
                .map(walkableStop -> new StopEdge(null, //No public transport route used when walking -> route = null, trip = null
                        null,
                        TransportMode.WALK,
                        stop,
                        walkableStop,
                        //Calculate walking time
                        timeAtStop + 1 + Math.round(walkableStop.getLocation().distanceTo(stop.getLocation()) / AVERAGE_WALKING_SPEED_MS),
                        timeAtStop))
                .collect(Collectors.toCollection(() -> new TiraArrayList<>()));
    }

    /**
     * Finds possible public transit journeys departing from the stop after the specified time
     * @param timeAtStop Arrival time to the stop (i.e. earliest possible departure time from the stop)
     * @param stop Get departures from this stop
     * @param found List of already found stops
     * @return
     */
    private List<Edge<Stop>> getPublicTransportEdgesFromStop(long timeAtStop, Stop stop, Set<Stop> found) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.setTimeInMillis(timeAtStop);

        LocalDate date = LocalDate.of(gregorianCalendar.get(java.util.Calendar.YEAR), gregorianCalendar.get(java.util.Calendar.MONTH) + 1, gregorianCalendar.get(java.util.Calendar.DAY_OF_MONTH));

        List<StopTime> stopTimes = stopTimesByStopIdIndex.getItems(stop.getId());
        return stopTimes.stream().flatMap(stopTime -> {
            ServiceDates serviceDatesForStopTime = serviceDates.get(trips.get(stopTime.getTripId()).getServiceId());

            LocalDate estimatedDepartureDate = date.minus(Math.round(stopTime.getArrivalTime() / 86400f), ChronoUnit.DAYS);

            List<StopEdge> edges = new TiraArrayList<>();

            //Optimization: list of best arrival times (to ignore routes that would arrive to these stops later than the previously found best route)
            //This optimization is not optimal but should decrease the amount of possible returned edges
            Map<String, Long> bestArrivalTime = new TiraHashMap<>();

            /**
             * Check possible departure dates that are: yesterday, today and the day after today
             */
            for (int i = -1; i <= 1; i++) {
                LocalDate departureDate = estimatedDepartureDate.plus(i, ChronoUnit.DAYS);

                if (serviceDatesForStopTime.runsOn(departureDate)) {
                    edges.addAll(
                            getPossibleDestinations(stop,
                                    timeAtStop,
                                    routes.get(trips.get(stopTime.getTripId()).getRouteId()),
                                    stopTime.getTripId(),
                                    departureDate,
                                    stopTime.getArrivalTime(),
                                    stopTimesByTripIdIndex.getItems(stopTime.getTripId()),
                                    found,
                                    bestArrivalTime));
                }
            }

            return edges.stream();
        }).collect(Collectors.toCollection(() -> new TiraArrayList<>()));
    }

    /**
     * Finds possible destinations using a specific route from specfic stop at specific time
     * @param stop Origin stop
     * @param timeAtStop Current time
     * @param route Route name (used only when printing the route for user)
     * @param tripId
     * @param departureDate
     * @param departureTime
     * @param stopTimesOfTrip List of arrival and departure times of the trip that is used
     * @param found List of already found stops
     * @return
     */
    private List<StopEdge> getPossibleDestinations(Stop stop, long timeAtStop, Route route, String tripId, LocalDate departureDate, long departureTime, List<StopTime> stopTimesOfTrip, Set<Stop> found, Map<String, Long> bestArrivalTime) {
        List<StopEdge> edges = new TiraArrayList<>();

        long departureTimeMillis = calculateTime(departureDate, departureTime);
        //If the public transport service departs from the stop before current time, that service cannot be used for the route -> return empty list
        if (departureTimeMillis < timeAtStop) {
            return Collections.emptyList();
        }

        for (StopTime stopTime : stopTimesOfTrip) {
            Stop destinationStop = stops.get(stopTime.getStopId());
            if (destinationStop == null) {
                //This should happen only if the GTFS feed is malformed
                return Collections.emptyList();
            }

             //Don't add new edge if a better route to the destination was already found
            long arrivalTimeMillis = calculateTime(departureDate, stopTime.getArrivalTime());
            if (arrivalTimeMillis > bestArrivalTime.getOrDefault(destinationStop.getId(), Long.MAX_VALUE)) {
                continue;
            }

            if (arrivalTimeMillis > departureTimeMillis) {
                //If the trip would pass through any of already found stops, return empty list as it would be slower to reach the final destination using this route than the route that was used for reaching the previously found route
                if (found.contains(destinationStop)) {
                    return Collections.emptyList();
                }

                bestArrivalTime.put(destinationStop.getId(), arrivalTimeMillis);
                edges.add(new StopEdge(route.getName(), tripId, route.getMode(), stop, destinationStop, arrivalTimeMillis, departureTimeMillis));
            }
        }

        return edges;
    }


    /**
     * Calculates time in milliseconds from date and time in seconds since midnight
     * @param date Date
     * @param secondsSinceMidnight Time in seconds since midnight of the specified date
     * @return
     */
    private long calculateTime(LocalDate date, long secondsSinceMidnight) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.setTimeInMillis(0);
        gregorianCalendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        gregorianCalendar.add(java.util.Calendar.SECOND, (int)secondsSinceMidnight);

        return gregorianCalendar.getTimeInMillis();
    }

    /**
     * Finds possible edges from the specified stop at specified time
     * @param time Departure time from node
     * @param node Departure stop
     * @param found List of already found stops, used for optimization
     * @return
     */
    @Override
    public List<Edge<Stop>> getEdgesFromNode(long time, Stop node, Set<Stop> found) {
        List<Edge<Stop>> walkingEdges = getWalkingEdgesFromStop(time, node, found);
        List<Edge<Stop>> publicTransportEdges = getPublicTransportEdgesFromStop(time, node, found);

        List<Edge<Stop>> edges = new TiraArrayList<>(walkingEdges.size() + publicTransportEdges.size());
        edges.addAll(walkingEdges);
        edges.addAll(publicTransportEdges);

        return edges;
    }

    /**
     * Finds a list of stops by name
     * @param name
     * @return
     */
    public List<Stop> findStopsByName(String name) {
        return stops.values()
                .stream()
                .filter(stop -> stop.getName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toCollection(() -> new TiraArrayList<>()));
    }

    /**
     * Gets a random stop from the graph
     * @return A random stop
     */
    public Stop getRandomStop() {
        int index = ThreadLocalRandom.current().nextInt(0, stops.size());

        Iterator<String> iterator = stops.keySet().iterator();
        String id = null;
        for (int i = 0; i <= index; i++) {
            id = iterator.next();
        }

        return stops.get(id);
    }

    /**
     * @return Number of stops in the graph
     */
    public int getStopCount() {
        return stops.size();
    }
}
