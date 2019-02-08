package xyz.malkki.gtfsroutefinder;

import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.Graph;
import xyz.malkki.gtfsroutefinder.graph.algorithms.AStar;
import xyz.malkki.gtfsroutefinder.gtfs.graph.GTFSGraph;
import xyz.malkki.gtfsroutefinder.gtfs.graph.StopEdge;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.StopTime;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSGraphBuilder;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Location for GTFS feed:");
        String path = scanner.nextLine();

        long start = System.nanoTime();
        GTFSGraph gtfsGraph = GTFSGraphBuilder.buildFromFiles(path);
        System.out.println("Graph built in  "+((System.nanoTime() - start) / 1_000_000)+"ms");

        AStar<Stop> routeFinder = new AStar<>((a,b) -> 1000 * Math.round(a.getLocation().distanceTo(b.getLocation()) / (50*3.6)));

        System.out.println("Search for origin stop:");
        Stop origin = findStop(gtfsGraph, scanner);
        System.out.println("Search for destination stop:");
        Stop destination = findStop(gtfsGraph, scanner);

        long startRouteSearch = System.nanoTime();
        List<Edge<Stop>> route = routeFinder.findPath(gtfsGraph, origin, System.currentTimeMillis(), destination);
        Collections.reverse(route);
        System.out.println("Route found in  "+((System.nanoTime() - startRouteSearch) / 1_000_000)+"ms");

        route.forEach(edge -> {
            StopEdge stopEdge = (StopEdge)edge;
            System.out.println(stopEdge.getUsedRoute() + " ("+stopEdge.getTransportMode().name()+") "+stopEdge.getFrom().getName()+" -> "+stopEdge.getTo().getName());
        });
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
