package xyz.malkki.gtfsroutefinder;

import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.algorithms.AStar;
import xyz.malkki.gtfsroutefinder.graph.algorithms.Dijkstra;
import xyz.malkki.gtfsroutefinder.gtfs.graph.GTFSGraph;
import xyz.malkki.gtfsroutefinder.gtfs.graph.StopEdge;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSGraphBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Location for GTFS feed:");
        String path = scanner.nextLine();
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            System.out.println("No directory found in "+path);
            return;
        }

        long start = System.nanoTime();
        GTFSGraph gtfsGraph = GTFSGraphBuilder.buildFromFiles(path);
        System.out.println("Graph built in "+((System.nanoTime() - start) / 1_000_000)+"ms");

        AStar<Stop> aStarRouteFinder = new AStar<>((a,b) -> 1000 * Math.round(a.getLocation().distanceTo(b.getLocation()) / (30 * 3.6)));
        Dijkstra<Stop> dijkstraRouteFinder = new Dijkstra<>();

        loop: while (true) {
            System.out.println("Select action\n\t1 - algorithm comparison\n\t2 - route finder\n\t3 - quit");
            try {
                int action = Integer.parseInt(scanner.nextLine());

                switch (action) {
                    case 1:
                        comparison(scanner, gtfsGraph, aStarRouteFinder, dijkstraRouteFinder);
                        break;
                    case 2:
                        routeFinder(gtfsGraph, scanner, aStarRouteFinder);
                        break;
                    case 3:
                        break loop;
                }
            } catch (NumberFormatException e) {}
        }

    }


    private static void comparison(Scanner scanner, GTFSGraph gtfsGraph, AStar<Stop> aStar, Dijkstra<Stop> dijkstra) {
        System.out.println("Number of iterations?");
        int count = Integer.parseInt(scanner.nextLine());

        List<Stop> origins = Stream.generate(gtfsGraph::getRandomStop).limit(count).collect(Collectors.toCollection(() -> new TiraArrayList<>()));
        List<Stop> destinations = Stream.generate(gtfsGraph::getRandomStop).limit(count).collect(Collectors.toCollection(() -> new TiraArrayList<>()));

        long aStarTotal = 0;
        long dijkstraTotal = 0;

        long time = System.currentTimeMillis();

        System.out.println("Random stops selected, starting comparison");

        for (int i = 0; i < count; i++) {
            long aStarStart = System.nanoTime();
            aStar.findPath(gtfsGraph, origins.get(i), time, destinations.get(i));
            aStarTotal += (System.nanoTime() - aStarStart) / 1_000_000;

            long dijkstraStart = System.nanoTime();
            dijkstra.findPath(gtfsGraph, origins.get(i), time, destinations.get(i));
            dijkstraTotal += (System.nanoTime() - dijkstraStart) / 1_000_000;
            System.out.println("Iteration "+(i+1)+"/"+count);
        }

        System.out.println("AStar found "+count+" random routes in "+(aStarTotal / 1000)+"s");
        System.out.println("Dijkstra found "+count+" random routes in "+(dijkstraTotal / 1000)+"s");
    }

    private static void routeFinder(GTFSGraph gtfsGraph, Scanner scanner, AStar<Stop> aStarRouteFinder) {
        System.out.println("Search for origin stop:");
        Stop origin = findStop(gtfsGraph, scanner);
        System.out.println("Search for destination stop:");
        Stop destination = findStop(gtfsGraph, scanner);

        long startRouteSearch = System.nanoTime();
        List<Edge<Stop>> route = aStarRouteFinder.findPath(gtfsGraph, origin, System.currentTimeMillis(), destination);
        if (route != null) {
            Collections.reverse(route);

            route.forEach(edge -> {
                StopEdge stopEdge = (StopEdge)edge;
                System.out.println(stopEdge.getUsedRoute() + " ("+stopEdge.getTransportMode().name()+") "+stopEdge.getFrom().getName()+" -> "+stopEdge.getTo().getName());
            });
        } else {
            System.out.println("No route found");
        }

        System.out.println("Route found in "+((System.nanoTime() - startRouteSearch) / 1_000_000)+"ms");
    }

    private static Stop findStop(GTFSGraph graph, Scanner scanner) {
        List<Stop> stops = graph.findStopsByName(scanner.nextLine());
        if (stops.size() > 1) {
            for (int i = 0; i < stops.size(); i++) {
                System.out.println((i+1)+" - "+stops.get(i).getName());
            }

            System.out.println("Select one");
            int index = Integer.parseInt(scanner.nextLine());

            return stops.get(index - 1);
        } else {
            return stops.get(0);
        }
    }
}
