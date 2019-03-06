package xyz.malkki.gtfsroutefinder.graph.algorithms;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;

import java.util.*;
import java.util.function.BiFunction;

public class Dijkstra<N> implements PathFindingAlgorithm<N> {
    private AStar<N> aStar = new AStar<N>((a,b) -> 0L);

    /**
     * Calculates a path from node A to node B using Dijkstra's algorithm
     * @param graph Graph of nodes
     * @param from Node A
     * @param startTime Departure time from node A
     * @param to Node B
     * @return
     */
    @Override
    public List<Edge<N>> findPath(Graph<N> graph, N from, long startTime, N to) {
        return aStar.findPath(graph, from, startTime, to);
    }
}
