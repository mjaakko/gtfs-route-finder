package xyz.malkki.gtfsroutefinder.gtfs.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.gtfs.model.Stop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class GTFSParser {
    private GTFSParser() {}

    public static <T> List<T> parseFromFile(String file, Function<CSVRecord, T> parser) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            CSVParser csvParser = CSVParser.parse(reader, CSVFormat.RFC4180.withFirstRecordAsHeader());

            List<T> stops = new ArrayList<>();

            csvParser.forEach(record -> stops.add(parser.apply(record)));

            return stops;
        }
    }
}
