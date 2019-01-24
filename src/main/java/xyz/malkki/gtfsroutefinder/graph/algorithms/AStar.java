package xyz.malkki.gtfsroutefinder.graph.algorithms;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;

import java.util.*;
import java.util.function.BiFunction;

//https://en.wikipedia.org/wiki/A*_search_algorithm
public class AStar {
    public <N> List<Edge<N>> findPath(Graph<N> graph, BiFunction<N, N, Integer> heuristicFunction, N from, Integer startTime, N to) {
        Map<N, Edge<N>> routeMap = new HashMap<>();
        Set<N> found = new HashSet<>();

        Map<N, Integer> timeAtNode = new HashMap<>();
        timeAtNode.put(from, startTime);

        PriorityQueue<N> queue = new PriorityQueue<>(new Comparator<N>() {
            @Override
            public int compare(N node1, N node2) {
                return Integer.compare(timeAtNode.get(node1), timeAtNode.get(node2));
            }
        });

        queue.add(from);

        while(!queue.isEmpty()) {
            N current = queue.poll();

            if (found.contains(current)) {
                continue;
            }

            found.add(current);

            if (current.equals(to)) {
                List<Edge<N>> route = new ArrayList<>();
                N node = current;
                Edge<N> edge;
                while((edge = routeMap.get(node)) != null) {
                    route.add(edge);
                    node = edge.getFrom();
                }
                return route;
            }

            for (Edge<N> edge : graph.getEdgesFromNode(current)) {
                N target = edge.getTo();

                if (edge.getDepartureTime() < timeAtNode.get(current)) {
                    //Cannot use edges that departed before arriving to the current node
                    continue;
                }

                if (edge.getArrivalTime() <= timeAtNode.getOrDefault(target, Integer.MAX_VALUE)) {
                    timeAtNode.put(target, edge.getArrivalTime() + heuristicFunction.apply(target, to));
                    routeMap.put(target, edge);
                }
                queue.offer(target);
            }
        }

        return null;
    }
}
