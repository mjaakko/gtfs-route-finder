package xyz.malkki.gtfsroutefinder;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
import xyz.malkki.gtfsroutefinder.datastructures.TiraHashMap;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.graph.algorithms.AStar;
import xyz.malkki.gtfsroutefinder.graph.algorithms.Dijkstra;
import xyz.malkki.gtfsroutefinder.graph.algorithms.PathFindingAlgorithm;
import xyz.malkki.gtfsroutefinder.gtfs.graph.GTFSGraph;
import xyz.malkki.gtfsroutefinder.gtfs.graph.StopEdge;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSGraphBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static final int ASTAR_HEURISTIC_SPEED_MS = 30;
    private static final int ASTAR_PESSIMISTIC_SPEED_MS = 5;

    private static final BiFunction<Stop, Stop, Long> ASTAR_HEURISTIC = (a,b) -> 1000 * Math.round(a.getLocation().distanceTo(b.getLocation()) / ASTAR_HEURISTIC_SPEED_MS);
    private static final BiFunction<Stop, Stop, Long> ASTAR_HEURISTIC_PESSIMISTIC = (a,b) -> 1000 * Math.round(a.getLocation().distanceTo(b.getLocation()) / ASTAR_PESSIMISTIC_SPEED_MS);

    public static void main(String[] args) throws IOException {
        if (args.length >= 3 && "--time-complexity-test".equals(args[0])) {
            String outputFile = args[1];
            String[] feeds = Arrays.copyOfRange(args,2, args.length);

            TimeComplexityTest test = new TimeComplexityTest(feeds);
            test.runTests(outputFile);

            System.out.println("Time complexity test done, results written to "+outputFile);

            return;
        }

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

        AStar<Stop> aStarRouteFinder = new AStar<>(ASTAR_HEURISTIC);

        loop: while (true) {
            System.out.println("Select action\n\t1 - algorithm comparison\n\t2 - route finder\n\t3 - quit");
            try {
                int action = Integer.parseInt(scanner.nextLine());

                switch (action) {
                    case 1:
                        AlgorithmComparison comparison = new AlgorithmComparison();
                        comparison.addAlgorithm("AStar", new AStar<>(ASTAR_HEURISTIC));
                        comparison.addAlgorithm("AStar (pessimistic, unoptimal routes)", new AStar<>(ASTAR_HEURISTIC_PESSIMISTIC));
                        comparison.addAlgorithm("Dijkstra", new Dijkstra<>());

                        System.out.println("Number of iterations?");
                        int count = Integer.parseInt(scanner.nextLine());

                        comparison.runComparison(gtfsGraph, count);
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

    private static class TimeComplexityTest {
        private int iterations = 10;
        private Map<String, PathFindingAlgorithm<Stop>> algorithms = new TiraHashMap<>();
        private String[] feeds;

        public TimeComplexityTest(String... feeds) {
            this.feeds = feeds;

            algorithms.put("AStar", new AStar<>(ASTAR_HEURISTIC));
            algorithms.put("AStar (pessimistic, unoptimal routes)", new AStar<>(ASTAR_HEURISTIC_PESSIMISTIC));
            algorithms.put("Dijkstra", new Dijkstra<>());
        }

        public void runTests(String outputFile) {
            try(CSVPrinter csvPrinter = new CSVPrinter(new BufferedWriter(new FileWriter(outputFile)), CSVFormat.RFC4180)) {
                csvPrinter.printRecord("Feed", "Algorithm", "Time", "NumberOfStopsInFeed");

                for (String feed : feeds) {
                    GTFSGraph gtfsGraph = GTFSGraphBuilder.buildFromFiles(feed);

                    List<Stop> origins = Stream.generate(gtfsGraph::getRandomStop).limit(iterations).collect(Collectors.toCollection(() -> new TiraArrayList<>()));
                    List<Stop> destinations = Stream.generate(gtfsGraph::getRandomStop).limit(iterations).collect(Collectors.toCollection(() -> new TiraArrayList<>()));

                    long time = System.currentTimeMillis();

                    Map<String, Long> timePerAlgorithm = new TiraHashMap<>();

                    for (int i = 0; i < iterations; i++) {
                        for (Map.Entry<String, PathFindingAlgorithm<Stop>> entry : algorithms.entrySet()) {
                            String name = entry.getKey();
                            PathFindingAlgorithm<Stop> algorithm = entry.getValue();

                            long start = System.nanoTime();
                            algorithm.findPath(gtfsGraph, origins.get(i), time, destinations.get(i));
                            long end = System.nanoTime();

                            timePerAlgorithm.compute(name, (key, prev) -> (prev == null ? 0 : prev) + (end - start));
                        }

                        System.out.println("Feed "+feed+", iteration "+(i+1)+"/"+iterations);
                    }

                    for (Map.Entry<String, Long> entry : timePerAlgorithm.entrySet()) {
                        String algorithmName = entry.getKey();
                        long timeInSeconds = TimeUnit.NANOSECONDS.toSeconds(entry.getValue());

                        csvPrinter.printRecord(feed, algorithmName, timeInSeconds, gtfsGraph.getStopCount());
                    }
                }
            } catch (IOException e) {
                System.err.println("Failed to run time complexity tests: "+e.getMessage());
            }
        }
    }

    private static class AlgorithmComparison {
        private Map<String, PathFindingAlgorithm<Stop>> algorithms = new TiraHashMap<>();

        public void addAlgorithm(String name, PathFindingAlgorithm<Stop> algorithm) {
            algorithms.put(name, algorithm);
        }

        public void runComparison(GTFSGraph gtfsGraph, int iterations) {
            List<Stop> origins = Stream.generate(gtfsGraph::getRandomStop).limit(iterations).collect(Collectors.toCollection(() -> new TiraArrayList<>()));
            List<Stop> destinations = Stream.generate(gtfsGraph::getRandomStop).limit(iterations).collect(Collectors.toCollection(() -> new TiraArrayList<>()));

            long time = System.currentTimeMillis();

            Map<String, Long> timePerAlgorithm = new TiraHashMap<>();
            System.out.println("Random stops selected, starting comparison");

            for (int i = 0; i < iterations; i++) {
                for (Map.Entry<String, PathFindingAlgorithm<Stop>> entry : algorithms.entrySet()) {
                    String name = entry.getKey();
                    PathFindingAlgorithm<Stop> algorithm = entry.getValue();

                    long start = System.nanoTime();
                    boolean found = algorithm.findPath(gtfsGraph, origins.get(i), time, destinations.get(i)) != null;
                    long end = System.nanoTime();

                    if (!found) {
                        System.err.println("Warning! No route found from "+origins.get(i).getName()+" to "+destinations.get(i).getName());
                    }

                    timePerAlgorithm.compute(name, (key, prev) -> (prev == null ? 0 : prev) + (end - start));
                }
                System.out.println("Iteration "+(i+1)+"/"+iterations);
            }

            for (Map.Entry<String, Long> entry : timePerAlgorithm.entrySet()) {
                String algorithmName = entry.getKey();
                long timeInSeconds = TimeUnit.NANOSECONDS.toSeconds(entry.getValue());

                System.out.println(String.format("%s found %d random routes in %ds", algorithmName, iterations, timeInSeconds));
            }
        }
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
