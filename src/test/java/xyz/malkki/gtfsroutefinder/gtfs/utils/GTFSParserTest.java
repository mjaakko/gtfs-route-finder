package xyz.malkki.gtfsroutefinder.gtfs.utils;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

public class GTFSParserTest {
    @Test
    public void testParsing() throws IOException {
        String testFilePath = getClass().getClassLoader().getResource("test.csv").getFile();

        List<String[]> data = GTFSParser.parseFromFile(testFilePath, record -> {
            return new String[]{ record.get("column1"), record.get("column2")};
        });

        assertArrayEquals(new String[]{ "test1_1", "test1_2" }, data.get(0));
        assertArrayEquals(new String[]{ "test2_1", "test2_2" }, data.get(1));
    }
}
