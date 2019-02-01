package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//https://developers.google.com/transit/gtfs/reference/#tripstxt
public class Trip {
    private String routeId;
    private String serviceId;
    private String id;

    public Trip(String routeId, String serviceId, String id) {
        this.routeId = routeId;
        this.serviceId = serviceId;
        this.id = id;
    }

    public String getRouteId() {
        return routeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(routeId, trip.routeId) &&
                Objects.equals(serviceId, trip.serviceId) &&
                Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, serviceId, id);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "routeId='" + routeId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public static List<Trip> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, record -> {
            String routeId = record.get("route_id");
            String serviceId = record.get("service_id");
            String id = record.get("trip_id");

            return new Trip(routeId, serviceId, id);
        });
    }
}
