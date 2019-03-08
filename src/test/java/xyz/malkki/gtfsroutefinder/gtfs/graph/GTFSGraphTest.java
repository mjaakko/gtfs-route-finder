package xyz.malkki.gtfsroutefinder.gtfs.graph;

import org.junit.Before;
import org.junit.Test;
import xyz.malkki.gtfsroutefinder.graph.Edge;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;
import xyz.malkki.gtfsroutefinder.gtfs.utils.GTFSGraphBuilder;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GTFSGraphTest {
    private GTFSGraph gtfsGraph;

    @Before
    public void setup() throws IOException {
        gtfsGraph = GTFSGraphBuilder.buildFromFiles(getClass().getClassLoader().getResource("gtfs_graph").getFile());
    }

    @Test
    public void testStopCount() {
        assertEquals(11, gtfsGraph.getStopCount());
    }

    @Test
    public void testFindWalkingEdgesEdges() {
        Stop pasila = gtfsGraph.findStopsByName("pasila").stream().filter(stop -> "1174504".equals(stop.getId())).findAny().get();
        Set<Stop> found = Collections.singleton(pasila);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Helsinki"));
        calendar.setTimeInMillis(0);
        calendar.set(2019,2,6, 4,0);

        List<Edge<Stop>> edges = gtfsGraph.getEdgesFromNode(calendar.getTimeInMillis(), pasila, found);

        List<Edge<Stop>> walking = edges.stream().filter(edge -> {
            StopEdge stopEdge = (StopEdge) edge;
            return stopEdge.getTransportMode() == TransportMode.WALK;
        }).collect(Collectors.toList());

        assertEquals(3, walking.size());
        walking.forEach(edge -> {
            assertTrue(edge.getFrom().getLocation().distanceTo(edge.getTo().getLocation()) <= 500);
        });
    }

    @Test
    public void testFindPublicTransportEdges() {
        Stop pasila = gtfsGraph.findStopsByName("pasila").stream().filter(stop -> "1174504".equals(stop.getId())).findAny().get();
        Set<Stop> found = Collections.singleton(pasila);

        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Europe/Helsinki"));
        calendar.setTimeInMillis(0);
        calendar.set(2019,2,6, 4,0);

        List<Edge<Stop>> edges = gtfsGraph.getEdgesFromNode(calendar.getTimeInMillis(), pasila, found);

        List<Edge<Stop>> publicTransport = edges.stream().filter(edge -> {
            StopEdge stopEdge = (StopEdge) edge;
            return stopEdge.getTransportMode() != TransportMode.WALK;
        }).collect(Collectors.toList());

        assertEquals(6, publicTransport.size());
    }
}
