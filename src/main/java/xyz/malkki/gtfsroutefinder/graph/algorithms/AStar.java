package xyz.malkki.gtfsroutefinder.graph.algorithms;

import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
import xyz.malkki.gtfsroutefinder.datastructures.TiraHeap;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;

import java.util.*;
import java.util.function.BiFunction;

//https://en.wikipedia.org/wiki/A*_search_algorithm
public class AStar<N> implements PathFindingAlgorithm<N> {
    private BiFunction<N, N, Long> heuristicFunction;

    public AStar(BiFunction<N, N, Long> heuristicFunction) {
        this.heuristicFunction = heuristicFunction;
    }

    /**
     * Calculates a path from node A to node B using A*-algorithm
     * @param graph Graph of nodes
     * @param from Node A
     * @param startTime Departure time from node A
     * @param to Node B
     * @return
     */
    @Override
    public List<Edge<N>> findPath(Graph<N> graph, N from, long startTime, N to) {
        Map<N, Edge<N>> routeMap = new HashMap<>();
        Set<N> found = new HashSet<>();

        //Keep track of arrival times to nodes so that edges which have departed from node before arrival can be ignored
        Map<N, Long> timeAtNode = new HashMap<>();
        timeAtNode.put(from, startTime);

        //Select next nodes based on the value of heuristic function
        Map<N, Long> heuristicTimeAtNode = new HashMap<>();
        heuristicTimeAtNode.put(from, startTime);

        Queue<N> queue = new TiraHeap<>(Comparator.comparingLong(heuristicTimeAtNode::get));

        queue.add(from);

        while(!queue.isEmpty()) {
            N current = queue.poll();

            if (found.contains(current)) {
                continue;
            }
            found.add(current);

            if (current.equals(to)) {
                List<Edge<N>> route = new TiraArrayList<>();
                N node = current;
                Edge<N> edge;
                while((edge = routeMap.get(node)) != null) {
                    route.add(edge);
                    node = edge.getFrom();
                }
                return route;
            }

            for (Edge<N> edge : graph.getEdgesFromNode(timeAtNode.get(current), current)) {
                N target = edge.getTo();

                if (edge.getDepartureTime() < timeAtNode.get(current)) {
                    //Cannot use edges that departed before arriving to the current node
                    continue;
                }

                if (edge.getArrivalTime() <= timeAtNode.getOrDefault(target, Long.MAX_VALUE)) {
                    timeAtNode.put(target, edge.getArrivalTime());
                    heuristicTimeAtNode.put(target, edge.getArrivalTime() + heuristicFunction.apply(target, to));

                    routeMap.put(target, edge);
                }
                queue.offer(target);
            }
        }

        return null;
    }
}
