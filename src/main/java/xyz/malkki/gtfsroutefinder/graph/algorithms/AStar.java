package xyz.malkki.gtfsroutefinder.graph.algorithms;

import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
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

    @Override
    public List<Edge<N>> findPath(Graph<N> graph, N from, long startTime, N to) {
        Map<N, Edge<N>> routeMap = new HashMap<>();
        Set<N> found = new HashSet<>();

        Map<N, Long> timeAtNode = new HashMap<>();
        timeAtNode.put(from, startTime);
        Map<N, Long> heuristicTimeAtNode = new HashMap<>();
        heuristicTimeAtNode.put(from, startTime);

        PriorityQueue<N> queue = new PriorityQueue<>(new Comparator<N>() {
            @Override
            public int compare(N node1, N node2) {
                return Long.compare(heuristicTimeAtNode.get(node1), heuristicTimeAtNode.get(node2));
            }
        });

        queue.add(from);

        while(!queue.isEmpty()) {
            N current = queue.poll();

            if (found.contains(current)) {
                continue;
            }
            //System.out.println("Current node "+current.toString()+" - "+current.hashCode());

            found.add(current);

            if (current.equals(to)) {
                List<Edge<N>> route = new TiraArrayList<>();
                N node = current;
                Edge<N> edge;
                while((edge = routeMap.get(node)) != null) {
                    //System.out.println("Added "+edge.toString()+" to finished route");
                    route.add(edge);
                    node = edge.getFrom();
                }
                return route;
            }

            for (Edge<N> edge : graph.getEdgesFromNode(timeAtNode.get(current), current)) {
                N target = edge.getTo();

                if (edge.getDepartureTime() < timeAtNode.get(current)) {
                    //Cannot use edges that departed before arriving to the current node
                    //System.out.println("Cannot use "+edge.toString());
                    continue;
                }

                //System.out.println("Found edge "+edge.toString()+" from "+current.toString());

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
