package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//https://developers.google.com/transit/gtfs/reference/#routestxt
public class Route {
    private String id;
    private String name;
    private TransportMode mode;

    public Route(String id, String name, TransportMode mode) {
        this.id = id;
        this.name = name;
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TransportMode getMode() {
        return mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id) &&
                Objects.equals(name, route.name) &&
                mode == route.mode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mode);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", mode=" + mode +
                '}';
    }

    public static List<Route> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, record -> {
            String id = record.get("route_id");
            String shortName = record.get("route_short_name");
            String longName = record.get("route_long_name");
            int transportMode = Integer.parseInt(record.get("route_type"));

            return new Route(id, shortName == null || shortName.isEmpty() ? longName : shortName, TransportMode.getTransportModeFromGTFSType(transportMode));
        });
    }
}
