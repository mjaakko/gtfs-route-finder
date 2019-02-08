package xyz.malkki.gtfsroutefinder.gtfs.graph;

import xyz.malkki.gtfsroutefinder.common.model.Geohash;
import xyz.malkki.gtfsroutefinder.common.utils.Indexer;
import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
import xyz.malkki.gtfsroutefinder.datastructures.TiraLinkedList;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.*;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Calendar;
import xyz.malkki.gtfsroutefinder.gtfs.utils.ServiceDates;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

public class GTFSGraph extends Graph<Stop> {
    private static final double AVERAGE_WALKING_SPEED_MS = 1.4;

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

    public GTFSGraph(TimeZone timeZone, List<Route> routes, List<Trip> trips, List<Stop> stops, List<StopTime> stopTimes, List<Calendar> calendars, List<CalendarDate> calendarDates) {
        this.timeZone = timeZone;

        stopByGeohashIndex = Indexer.buildFromColletion(stops, stop -> new Geohash(stop.getLocation().getLatitude(), stop.getLocation().getLongitude(), 3));

        stopTimesByStopIdIndex = Indexer.buildFromColletion(stopTimes, stopTime -> stopTime.getStopId());
        stopTimesByTripIdIndex = Indexer.buildFromColletion(stopTimes, stopTime -> stopTime.getTripId());

        Map<String, List<CalendarDate>> calendarDatesAsMap = new HashMap<>();
        calendarDates.forEach(calendarDate -> {
            List<CalendarDate> list = calendarDatesAsMap.computeIfAbsent(calendarDate.getServiceId(), k -> new TiraLinkedList<>());
            list.add(calendarDate);
        });

        serviceDates = new HashMap<>();
        calendars.forEach(calendar -> {
            List<LocalDate> additions = calendarDatesAsMap.getOrDefault(calendar.getServiceId(), Collections.emptyList())
                    .stream()
                    .filter(calendarDate -> calendarDate.isAvailable())
                    .map(calendarDate -> calendarDate.getDate())
                    .collect(Collectors.toCollection(() -> new TiraArrayList<>()));

            List<LocalDate> exceptions = calendarDatesAsMap.getOrDefault(calendar.getServiceId(), Collections.emptyList())
                    .stream()
                    .filter(calendarDate -> !calendarDate.isAvailable())
                    .map(calendarDate -> calendarDate.getDate())
                    .collect(Collectors.toCollection(() -> new TiraArrayList<>()));

            serviceDates.put(calendar.getServiceId(), new ServiceDates(calendar.getServiceId(), calendar.getStartDate(), calendar.getEndDate(),
                    calendar.isMonday(), calendar.isTuesday(), calendar.isWednesday(), calendar.isThursday(), calendar.isFriday(), calendar.isSaturday(), calendar.isSunday(), additions, exceptions));
        });

        this.stops = new HashMap<>(stops.size());
        stops.forEach(stop -> this.stops.put(stop.getId(), stop));

        this.routes = new HashMap<>(routes.size());
        routes.forEach(route -> this.routes.put(route.getId(), route));

        this.trips = new HashMap<>(trips.size());
        trips.forEach(trip -> this.trips.put(trip.getId(), trip));
    }

    /**
     * Finds nearby stops that can be reached by walking (distance < 500m)
     * @param stop
     * @return
     */
    private List<Edge<Stop>> getWalkingEdgesFromStop(long timeAtStop, Stop stop) {
        //Calculate a list of geohashes around the stop
        List<Geohash> nearbyGeohashes = Geohash.getSurroundingGeohashes(
                BigDecimal.valueOf(stop.getLocation().getLatitude()),
                BigDecimal.valueOf(stop.getLocation().getLongitude()), 3);

        return nearbyGeohashes.stream()
                //Get all stops inside the geohashes
                .flatMap(geohash -> stopByGeohashIndex.getItems(geohash).stream())
                //Filter stops that are too far
                .filter(stopFromIndex -> stopFromIndex.getLocation().distanceTo(stop.getLocation()) <= 500
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
     * @param timeAtStop
     * @param stop
     * @return
     */
    private List<Edge<Stop>> getPublicTransportEdgesFromStop(long timeAtStop, Stop stop) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.setTimeInMillis(timeAtStop);

        LocalDate date = LocalDate.of(gregorianCalendar.get(java.util.Calendar.YEAR), gregorianCalendar.get(java.util.Calendar.MONTH) + 1, gregorianCalendar.get(java.util.Calendar.DAY_OF_MONTH));

        List<StopTime> stopTimes = stopTimesByStopIdIndex.getItems(stop.getId());
        return stopTimes.stream().flatMap(stopTime -> {
            ServiceDates serviceDatesForStopTime = serviceDates.get(trips.get(stopTime.getTripId()).getServiceId());

            LocalDate estimatedDepartureDate = date.minus(Math.round(stopTime.getArrivalTime() / 86400f), ChronoUnit.DAYS);

            List<StopEdge> edges = new TiraArrayList<>();

            if (serviceDatesForStopTime.runsOn(estimatedDepartureDate)) {
                edges.addAll(
                        getPossibleDestinations(stop,
                                routes.get(trips.get(stopTime.getTripId()).getRouteId()),
                                stopTime.getTripId(),
                                estimatedDepartureDate,
                                stopTime.getArrivalTime(),
                                stopTimesByTripIdIndex.getItems(stopTime.getTripId())));
            }
            if (serviceDatesForStopTime.runsOn(estimatedDepartureDate.minus(1, ChronoUnit.DAYS))) {
                edges.addAll(
                        getPossibleDestinations(stop,
                                routes.get(trips.get(stopTime.getTripId()).getRouteId()),
                                stopTime.getTripId(),
                                estimatedDepartureDate.minus(1, ChronoUnit.DAYS),
                                stopTime.getArrivalTime(),
                                stopTimesByTripIdIndex.getItems(stopTime.getTripId())));
            }
            if (serviceDatesForStopTime.runsOn(estimatedDepartureDate.plus(1, ChronoUnit.DAYS))) {
                edges.addAll(
                        getPossibleDestinations(stop,
                                routes.get(trips.get(stopTime.getTripId()).getRouteId()),
                                stopTime.getTripId(),
                                estimatedDepartureDate.plus(1, ChronoUnit.DAYS),
                                stopTime.getArrivalTime(),
                                stopTimesByTripIdIndex.getItems(stopTime.getTripId())));
            }

            return edges.stream();
        }).collect(Collectors.toCollection(() -> new TiraArrayList<>()));
    }

    private List<StopEdge> getPossibleDestinations(Stop stop, Route route, String tripId, LocalDate departureDate, long departureTime, List<StopTime> stopTimesOfTrip) {
        List<StopEdge> edges = new TiraArrayList<>();

        long departureTimeMillis = calculateTime(departureDate, departureTime);

        stopTimesOfTrip.forEach(stopTime -> {
            if (calculateTime(departureDate, stopTime.getArrivalTime()) > departureTimeMillis) {
                edges.add(new StopEdge(route.getName(), tripId, route.getMode(), stop, stops.get(stopTime.getStopId()), calculateTime(departureDate, stopTime.getArrivalTime()), departureTimeMillis));
            }
        });

        return edges;
    }


    private long calculateTime(LocalDate date, long secondsSinceMidnight) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.setTimeInMillis(0);
        gregorianCalendar.set(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        gregorianCalendar.add(java.util.Calendar.SECOND, (int)secondsSinceMidnight);

        return gregorianCalendar.getTimeInMillis();
    }

    @Override
    public List<Edge<Stop>> getEdgesFromNode(long time, Stop node) {
        List<Edge<Stop>> walkingEdges = getWalkingEdgesFromStop(time, node);
        List<Edge<Stop>> publicTransportEdges = getPublicTransportEdgesFromStop(time, node);

        List<Edge<Stop>> edges = new TiraArrayList<>();
        edges.addAll(walkingEdges);
        edges.addAll(publicTransportEdges);

        return edges;
    }

    public List<Stop> findStopsByName(String name) {
        return stops.values()
                .stream()
                .filter(stop -> stop.getName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT)))
                .collect(Collectors.toCollection(() -> new TiraArrayList<>()));
    }
}
