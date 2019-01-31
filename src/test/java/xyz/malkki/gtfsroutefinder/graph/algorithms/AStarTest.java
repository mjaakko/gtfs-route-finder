package xyz.malkki.gtfsroutefinder.graph.algorithms;

import org.junit.Test;

import static org.junit.Assert.*;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;

import java.util.*;
import java.util.stream.Collectors;

public class AStarTest {
    private static class StringEdge extends Edge<String> {
        private String from;
        private String to;
        private int arrival;
        private int departure;

        public StringEdge(String from, String to, int arrival, int departure) {
            this.from = from;
            this.to = to;
            this.arrival = arrival;
            this.departure = departure;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public String getTo() {
            return to;
        }

        @Override
        public long getArrivalTime() {
            return arrival;
        }

        @Override
        public long getDepartureTime() {
            return departure;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StringEdge that = (StringEdge) o;
            return arrival == that.arrival &&
                    departure == that.departure &&
                    Objects.equals(from, that.from) &&
                    Objects.equals(to, that.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to, arrival, departure);
        }
    }

    @Test
    public void testCannotFindPathToNonexistentNode() {
        AStar<String> aStar = new AStar<String>((a,b) -> 0L);

        assertNull(aStar.findPath(new Graph<String>() {
            @Override
            public List<Edge<String>> getEdgesFromNode(long time, String node) {
                return Collections.emptyList();
            }
        }, "node1", 0, "node2"));
    }

    @Test
    public void testSimplePathFindingWithoutHeuristics() {
        AStar<String> aStar = new AStar<String>((a,b) -> 0L);

        Map<String, List<Edge<String>>> graphAsMap = new HashMap<>();
        graphAsMap.put("node1", Arrays.asList(new StringEdge("node1", "node2", 10, 0),
                new StringEdge("node1", "node2", 15, 5),
                new StringEdge("node1", "node3", 20, 0)));

        graphAsMap.put("node2", Arrays.asList(new StringEdge("node2", "node3", 18, 12),
                new StringEdge("node2", "node4", 30, 15)));

        graphAsMap.put("node3", Arrays.asList(new StringEdge("node3", "node4", 25, 20)));

        Graph<String> graph = new Graph<String>() {
            @Override
            public List<Edge<String>> getEdgesFromNode(long time, String node) {
                return graphAsMap.getOrDefault(node, Collections.emptyList()).stream().filter(edge -> edge.getDepartureTime() >= time).collect(Collectors.toList());
            }
        };

        List<Edge<String>> edges = aStar.findPath(graph, "node1", 0, "node4");
        //Path should be node1 -> node2 -> node3 -> node4
        assertEquals(3, edges.size());
    }

    @Test
    public void testSimplePathFindingWithMissedDepartureWithoutHeuristics() {
        AStar<String> aStar = new AStar<String>((a,b) -> 0L);

        Map<String, List<Edge<String>>> graphAsMap = new HashMap<>();
        graphAsMap.put("node1", Arrays.asList(new StringEdge("node1", "node2", 10, 0),
                new StringEdge("node1", "node2", 15, 5),
                new StringEdge("node1", "node3", 20, 0)));

        graphAsMap.put("node2", Arrays.asList(new StringEdge("node2", "node3", 18, 12),
                new StringEdge("node2", "node4", 30, 15)));

        graphAsMap.put("node3", Arrays.asList(new StringEdge("node3", "node4", 25, 15)));

        Graph<String> graph = new Graph<String>() {
            @Override
            public List<Edge<String>> getEdgesFromNode(long time, String node) {
                return graphAsMap.getOrDefault(node, Collections.emptyList()).stream().filter(edge -> edge.getDepartureTime() >= time).collect(Collectors.toList());
            }
        };

        List<Edge<String>> edges = aStar.findPath(graph, "node1", 0, "node4");
        //Path should be node1 -> node2 -> node4 (edge from node3 to node4 cannot be used as the departure time is before arrival to node 3)
        assertEquals(2, edges.size());
    }
}
