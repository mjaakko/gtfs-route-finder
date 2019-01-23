package xyz.malkki.gtfsroutefinder.common.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class IndexerTest {
    @Test
    public void testCanAddToIndex() {
        Indexer<String, Character> indexer = new Indexer<>(s -> s.charAt(0));
        indexer.add("test");
        indexer.add("testi");

        assertEquals(2, indexer.getItems('t').size());
    }

    @Test
    public void testIndexDoesNotContainValuesThatHaveNotBeenAdded() {
        Indexer<String, Character> indexer = new Indexer<>(s -> s.charAt(0));

        assertTrue(indexer.getItems('t').isEmpty());
    }
}
