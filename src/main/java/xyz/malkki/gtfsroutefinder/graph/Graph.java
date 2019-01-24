package xyz.malkki.gtfsroutefinder.graph;

import sun.security.provider.certpath.Vertex;

import java.util.List;

public abstract class Graph<N> {
    /**
     * Get possible paths from the node
     * @param node Node
     * @return List of possible paths from the node
     */
    public abstract List<Edge<N>> getEdgesFromNode(N node);
}
