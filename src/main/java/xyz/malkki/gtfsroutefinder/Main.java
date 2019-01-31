package xyz.malkki.gtfsroutefinder;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;
import xyz.malkki.gtfsroutefinder.graph.algorithms.AStar;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String node1 = "n1";
        String node2 = "n2";
        String node3 = "n3";

        Map<String, List<Edge<String>>> edges = new HashMap<>();
        edges.put(node1, Arrays.asList(new Edge<String>() {
            @Override
            public String getFrom() {
                return node1;
            }

            @Override
            public String getTo() {
                return node2;
            }

            @Override
            public long getArrivalTime() {
                return 10;
            }

            @Override
            public long getDepartureTime() {
                return 0;
            }
        }, new Edge<String>() {
            @Override
            public String getFrom() {
                return node1;
            }

            @Override
            public String getTo() {
                return node3;
            }

            @Override
            public long getArrivalTime() {
                return 45;
            }

            @Override
            public long getDepartureTime() {
                return 0;
            }
        }));

        edges.put(node2, Arrays.asList(new Edge<String>() {
            @Override
            public String getFrom() {
                return node2;
            }

            @Override
            public String getTo() {
                return node3;
            }

            @Override
            public long getArrivalTime() {
                return 30;
            }

            @Override
            public long getDepartureTime() {
                return 5;
            }
        }, new Edge<String>() {
            @Override
            public String getFrom() {
                return node2;
            }

            @Override
            public String getTo() {
                return node1;
            }

            @Override
            public long getArrivalTime() {
                return 20;
            }

            @Override
            public long getDepartureTime() {
                return 15;
            }
        }));

        Graph<String> graph = new Graph<String>() {
            @Override
            public List<Edge<String>> getEdgesFromNode(long time, String node) {
                return edges.get(node);
            }
        };

        AStar aStar = new AStar((a,b) -> 0);
        System.out.println(aStar.findPath(graph, node1, 0, node3).size());
    }
}
