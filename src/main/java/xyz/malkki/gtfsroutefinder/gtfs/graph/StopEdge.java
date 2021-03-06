package xyz.malkki.gtfsroutefinder.gtfs.graph;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;

import java.util.Objects;

/**
 * Describes a journey from one stop to another
 */
public class StopEdge extends Edge<Stop> {
    private String usedRoute;
    private String usedTripId;
    private TransportMode transportMode;
    private Stop from;
    private Stop to;
    private long arrivalTime;
    private long departureTime;

    public StopEdge(String usedRoute, String usedTripId, TransportMode transportMode, Stop from, Stop to, long arrivalTime, long departureTime) {
        this.usedRoute = usedRoute;
        this.usedTripId = usedTripId;
        this.transportMode = transportMode;
        this.from = from;
        this.to = to;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
    }

    /**
     * Gets the public transportation route name that was used for this edge
     * @return
     */
    public String getUsedRoute() {
        return usedRoute;
    }

    public String getUsedTripId() { return usedTripId; }

    /**
     * Gets the transportation mode (e.g. walking, taking a bus..) that was used for this edge
     * @return
     */
    public TransportMode getTransportMode() {
        return transportMode;
    }

    @Override
    public Stop getFrom() {
        return from;
    }

    @Override
    public Stop getTo() {
        return to;
    }

    @Override
    public long getArrivalTime() {
        return arrivalTime;
    }

    @Override
    public long getDepartureTime() {
        return departureTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StopEdge stopEdge = (StopEdge) o;
        return arrivalTime == stopEdge.arrivalTime &&
                departureTime == stopEdge.departureTime &&
                Objects.equals(usedRoute, stopEdge.usedRoute) &&
                Objects.equals(usedTripId, stopEdge.usedTripId) &&
                transportMode == stopEdge.transportMode &&
                Objects.equals(from, stopEdge.from) &&
                Objects.equals(to, stopEdge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usedRoute, usedTripId, transportMode, from, to, arrivalTime, departureTime);
    }

    @Override
    public String toString() {
        return "StopEdge{" +
                "usedRoute='" + usedRoute + '\'' +
                ", usedTripId='" + usedTripId + '\'' +
                ", transportMode=" + transportMode +
                ", from=" + from +
                ", to=" + to +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                '}';
    }
}
