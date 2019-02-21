package xyz.malkki.gtfsroutefinder.graph;

import java.util.List;
import java.util.Set;

public abstract class Graph<N> {
    /**
     * Get possible paths from the node
     * @param time Departure time from node
     * @param node Node
     * @param alreadyFound Set of already found nodes. This can be used for optimizing the list of returned edges.
     * @return List of possible paths from the node
     */
    public abstract List<Edge<N>> getEdgesFromNode(long time, N node, Set<N> alreadyFound);
}
