package xyz.malkki.gtfsroutefinder.gtfs.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import xyz.malkki.gtfsroutefinder.common.model.LatLng;
import xyz.malkki.gtfsroutefinder.datastructures.TiraArrayList;
import xyz.malkki.gtfsroutefinder.datastructures.TiraHashMap;
import xyz.malkki.gtfsroutefinder.gtfs.model.core.Stop;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class GTFSParser {
    private GTFSParser() {}

    /**
     * Parses a single file of GTFS feed
     * @param file File to parse
     * @param parser Function that maps a single row of the file to a value
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> parseFromFile(String file, BiFunction<CSVRecord, Map<String, Integer>, T> parser) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            CSVParser csvParser = CSVParser.parse(reader, CSVFormat.RFC4180.withFirstRecordAsHeader());

            List<T> data = new TiraArrayList<>();

            Map<String, Integer> headerMap = csvParser.getHeaderMap();
            Map<String, Integer> fixedHeaderMap = new TiraHashMap<>(headerMap.size());
            //GTFS headers seem to often have control characters that break parsing
            headerMap.forEach((header, index) -> {
                fixedHeaderMap.put(header.replaceAll("\\W", "") ,index);
            });


            csvParser.forEach(record -> data.add(parser.apply(record, fixedHeaderMap)));

            return data;
        }
    }
}
