package xyz.malkki.gtfsroutefinder.gtfs.model;

public enum TransportMode {
    RAIL, BUS, SUBWAY, TRAM, FERRY, AIRPLANE, CABLE_CAR, GONDOLA, FUNICULAR, WALK;

    /**
     * Parses {@link TransportMode} from GTFS type
     * See list of possible types here: https://developers.google.com/transit/gtfs/reference/#routestxt and https://developers.google.com/transit/gtfs/reference/extended-route-types
     * Note that taxi and car modes are not supported
     * @param type GTFS transport type
     * @return Transport mode corresponding to the GTFS type
     */
    public static TransportMode getTransportModeFromGTFSType(int type) {
        if (type >= 100 && type < 200) {
            return TransportMode.RAIL;
        } else if (type >= 200 && type < 300) {
            return TransportMode.BUS;
        } else if (type >= 300 && type < 500) {
            if (type >= 401 && type <= 402) {
                return TransportMode.SUBWAY;
            }
            
            return TransportMode.RAIL;
        } else if (type >= 500 && type < 700) {
            return TransportMode.SUBWAY;
        } else if (type >= 700 && type < 900) {
            return TransportMode.BUS;
        } else if (type >= 900 && type < 1000) {
            return TransportMode.TRAM;
        } else if (type >= 1000 && type < 1100) {
            return TransportMode.FERRY;
        } else if (type >= 1100 && type < 1200) {
            return TransportMode.AIRPLANE;
        } else if (type >= 1200 && type < 1300) {
            return TransportMode.FERRY;
        } else if (type >= 1300 && type < 1400) {
            return TransportMode.GONDOLA;
        } else if (type >= 1400 && type < 1500) {
            return TransportMode.FUNICULAR;
        } else if (type >= 1500 && type < 1700) {
            //In some GTFS feeds, minibuses are marked as taxis
            if (type == 1501) {
                return TransportMode.BUS;
            }

            throw new IllegalArgumentException("Unsupported type " + type);
        }
        
        switch (type) {
            case 0:
                return TransportMode.TRAM;
            case 1:
                return TransportMode.SUBWAY;
            case 2:
                return TransportMode.RAIL;
            case 3:
                return TransportMode.BUS;
            case 4:
                return TransportMode.FERRY;
            case 5:
                return TransportMode.CABLE_CAR;
            case 6:
                return TransportMode.GONDOLA;
            case 7:
                return TransportMode.FUNICULAR;
            default:
                throw new IllegalArgumentException("Unknown GTFS type " + type);
        }
    }
}
