package xyz.malkki.gtfsroutefinder.graph;

public abstract class Edge<N> {
    /**
     * @return Node where this edge started
     */
    public abstract N getFrom();

    /**
     * @return Node where this edge ended
     */
    public abstract N getTo();
    /**
     * @return Arrival time to the end node ({@link #getTo()})
     */
    public abstract int getArrivalTime();
    /**
     * @return Departure time from the start node ({@link #getFrom()})
     */
    public abstract int getDepartureTime();
}
