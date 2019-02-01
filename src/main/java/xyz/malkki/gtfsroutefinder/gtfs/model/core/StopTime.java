package xyz.malkki.gtfsroutefinder.gtfs.model.core;

import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSParser;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSTimeParser;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//https://developers.google.com/transit/gtfs/reference/#stop_timestxt
public class StopTime {
    private String tripId;
    private int arrivalTime;
    private int departureTime;
    private String stopId;
    private int stopSequence;

    public StopTime(String tripId, int arrivalTime, int departureTime, String stopId, int stopSequence) {
        this.tripId = tripId;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
    }

    public String getTripId() {
        return tripId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public int getStopSequence() {
        return stopSequence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopTime stopTime = (StopTime) o;
        return arrivalTime == stopTime.arrivalTime &&
                departureTime == stopTime.departureTime &&
                stopSequence == stopTime.stopSequence &&
                Objects.equals(tripId, stopTime.tripId) &&
                Objects.equals(stopId, stopTime.stopId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tripId, arrivalTime, departureTime, stopId, stopSequence);
    }

    public static List<StopTime> parseFromFile(String file) throws IOException {
        return GTFSParser.parseFromFile(file, record -> {
            String tripId = record.get("\uFEFFtrip_id");
            int arrivalTime = GTFSTimeParser.parseTime(record.get("arrival_time"));
            int departureTime = GTFSTimeParser.parseTime(record.get("departure_time"));
            String stopId = record.get("stop_id");
            int stopSequence = Integer.parseInt(record.get("stop_sequence"));

            return new StopTime(tripId, arrivalTime, departureTime, stopId, stopSequence);
        });
    }
}
