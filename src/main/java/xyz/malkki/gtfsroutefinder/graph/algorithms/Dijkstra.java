package xyz.malkki.gtfsroutefinder.graph.algorithms;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;

import java.util.*;
import java.util.function.BiFunction;

public class Dijkstra<N> implements PathFindingAlgorithm<N> {
    private AStar<N> aStar = new AStar<N>((a,b) -> 0L);

    @Override
    public List<Edge<N>> findPath(Graph<N> graph, N from, long startTime, N to) {
        return aStar.findPath(graph, from, startTime, to);
    }
}
