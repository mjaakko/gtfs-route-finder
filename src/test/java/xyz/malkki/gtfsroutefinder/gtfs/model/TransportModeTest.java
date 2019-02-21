package xyz.malkki.gtfsroutefinder.gtfs.model;

import org.junit.Test;
import xyz.malkki.gtfsroutefinder.gtfs.model.TransportMode;

import static org.junit.Assert.*;

public class TransportModeTest {
    @Test
    public void testGetFromGTFS1() {
        assertEquals(TransportMode.RAIL, TransportMode.getTransportModeFromGTFSType(100));
    }

    @Test
    public void testGetFromGTFS2() {
        assertEquals(TransportMode.RAIL, TransportMode.getTransportModeFromGTFSType(199));
    }

    @Test
    public void testGetFromGTFS3() {
        assertEquals(TransportMode.BUS, TransportMode.getTransportModeFromGTFSType(200));
    }

    @Test
    public void testGetFromGTFS4() {
        assertEquals(TransportMode.BUS, TransportMode.getTransportModeFromGTFSType(299));
    }

    @Test
    public void testGetFromGTFS5() {
        assertEquals(TransportMode.RAIL, TransportMode.getTransportModeFromGTFSType(300));
    }

    @Test
    public void testGetFromGTFS6() {
        assertEquals(TransportMode.RAIL, TransportMode.getTransportModeFromGTFSType(499));
    }

    @Test
    public void testGetFromGTFS7() {
        assertEquals(TransportMode.SUBWAY, TransportMode.getTransportModeFromGTFSType(401));
    }

    @Test
    public void testGetFromGTFS8() {
        assertEquals(TransportMode.SUBWAY, TransportMode.getTransportModeFromGTFSType(500));
    }

    @Test
    public void testGetFromGTFS9() {
        assertEquals(TransportMode.SUBWAY, TransportMode.getTransportModeFromGTFSType(699));
    }

    @Test
    public void testGetFromGTFS10() {
        assertEquals(TransportMode.BUS, TransportMode.getTransportModeFromGTFSType(700));
    }

    @Test
    public void testGetFromGTFS11() {
        assertEquals(TransportMode.BUS, TransportMode.getTransportModeFromGTFSType(899));
    }

    @Test
    public void testGetFromGTFS12() {
        assertEquals(TransportMode.TRAM, TransportMode.getTransportModeFromGTFSType(900));
    }

    @Test
    public void testGetFromGTFS13() {
        assertEquals(TransportMode.TRAM, TransportMode.getTransportModeFromGTFSType(999));
    }

    @Test
    public void testGetFromGTFS14() {
        assertEquals(TransportMode.FERRY, TransportMode.getTransportModeFromGTFSType(1000));
    }

    @Test
    public void testGetFromGTFS15() {
        assertEquals(TransportMode.FERRY, TransportMode.getTransportModeFromGTFSType(1099));
    }

    @Test
    public void testGetFromGTFS16() {
        assertEquals(TransportMode.AIRPLANE, TransportMode.getTransportModeFromGTFSType(1100));
    }

    @Test
    public void testGetFromGTFS17() {
        assertEquals(TransportMode.AIRPLANE, TransportMode.getTransportModeFromGTFSType(1199));
    }

    @Test
    public void testGetFromGTFS18() {
        assertEquals(TransportMode.FERRY, TransportMode.getTransportModeFromGTFSType(1200));
    }

    @Test
    public void testGetFromGTFS19() {
        assertEquals(TransportMode.FERRY, TransportMode.getTransportModeFromGTFSType(1299));
    }

    @Test
    public void testGetFromGTFS20() {
        assertEquals(TransportMode.GONDOLA, TransportMode.getTransportModeFromGTFSType(1300));
    }

    @Test
    public void testGetFromGTFS21() {
        assertEquals(TransportMode.GONDOLA, TransportMode.getTransportModeFromGTFSType(1399));
    }

    @Test
    public void testGetFromGTFS22() {
        assertEquals(TransportMode.FUNICULAR, TransportMode.getTransportModeFromGTFSType(1400));
    }

    @Test
    public void testGetFromGTFS23() {
        assertEquals(TransportMode.FUNICULAR, TransportMode.getTransportModeFromGTFSType(1499));
    }

    @Test
    public void testGetFromGTFS24() {
        assertEquals(TransportMode.TRAM, TransportMode.getTransportModeFromGTFSType(0));
    }

    @Test
    public void testGetFromGTFS25() {
        assertEquals(TransportMode.SUBWAY, TransportMode.getTransportModeFromGTFSType(1));
    }

    @Test
    public void testGetFromGTFS26() {
        assertEquals(TransportMode.RAIL, TransportMode.getTransportModeFromGTFSType(2));
    }

    @Test
    public void testGetFromGTFS27() {
        assertEquals(TransportMode.BUS, TransportMode.getTransportModeFromGTFSType(3));
    }

    @Test
    public void testGetFromGTFS28() {
        assertEquals(TransportMode.FERRY, TransportMode.getTransportModeFromGTFSType(4));
    }

    @Test
    public void testGetFromGTFS29() {
        assertEquals(TransportMode.CABLE_CAR, TransportMode.getTransportModeFromGTFSType(5));
    }

    @Test
    public void testGetFromGTFS30() {
        assertEquals(TransportMode.GONDOLA, TransportMode.getTransportModeFromGTFSType(6));
    }

    @Test
    public void testGetFromGTFS31() {
        assertEquals(TransportMode.FUNICULAR, TransportMode.getTransportModeFromGTFSType(7));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFromGTFSUnsupported() {
        TransportMode.getTransportModeFromGTFSType(1600);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetFromGTFSUnknown() {
        TransportMode.getTransportModeFromGTFSType(80);
    }
}
