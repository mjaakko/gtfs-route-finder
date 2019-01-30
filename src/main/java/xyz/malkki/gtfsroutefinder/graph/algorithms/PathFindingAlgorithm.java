package xyz.malkki.gtfsroutefinder.graph.algorithms;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;

import java.util.List;

public interface PathFindingAlgorithm<N> {
    List<Edge<N>> findPath(Graph<N> graph, N from, long startTime, N to);
}
