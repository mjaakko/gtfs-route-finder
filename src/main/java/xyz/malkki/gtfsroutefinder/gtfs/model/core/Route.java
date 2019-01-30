package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//https://developers.google.com/transit/gtfs/reference/#routestxt
public class Route {
    private String id;
    private String name;

    public Route(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Objects.equals(id, route.id) &&
                Objects.equals(name, route.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static List<Route> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, record -> {
            String id = record.get("route_id");
            String shortName = record.get("route_short_name");
            String longName = record.get("route_long_name");

            return new Route(id, shortName == null || shortName.isEmpty() ? longName : shortName);
        });
    }
}
