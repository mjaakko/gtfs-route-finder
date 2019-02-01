package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//https://developers.google.com/transit/gtfs/reference/#stopstxt
public class Stop {
    private String id;
    private String name;
    private LatLng location;

    public Stop(String id, String name, LatLng location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stop stop = (Stop) o;
        return Objects.equals(id, stop.id) &&
                Objects.equals(name, stop.name) &&
                Objects.equals(location, stop.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location);
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", location=" + location +
                '}';
    }

    public static List<Stop> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, record -> {
            String id = record.get("stop_id");
            String name = record.get("stop_name");
            double latitude = Double.parseDouble(record.get("stop_lat"));
            double longitude = Double.parseDouble(record.get("stop_lon"));

            return new Stop(id, name, new LatLng(latitude, longitude));
        });
    }
}
