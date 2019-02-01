package xyz.malkki.gtfsroutefinder.common.utils;

import java.util.*;
import java.util.function.Function;

public class Indexer<T, I> {
    private Map<I, List<T>> index = new HashMap<>();

    private Function<T, I> indexKeyFunction;

    /**
     * Creates a new indexer
     * @param indexKeyFunction Function that calculates index key for the value
     */
    public Indexer(Function<T, I> indexKeyFunction) {
        this.indexKeyFunction = indexKeyFunction;
    }

    /**
     * Adds a value to the index
     * @param item
     */
    public void add(T item) {
        List<T> items = index.computeIfAbsent(indexKeyFunction.apply(item), i -> new LinkedList<>());
        items.add(item);
    }

    /**
     * Gets a list of items associated to an index key
     * @param indexKey
     * @return List of items
     */
    public List<T> getItems(I indexKey) {
        return index.getOrDefault(indexKey, Collections.emptyList());
    }

    public static <T, I> Indexer<T, I> buildFromColletion(Collection<T> collection, Function<T, I> indexKeyFunction) {
        Indexer<T, I> indexer = new Indexer<>(indexKeyFunction);
        collection.forEach(indexer::add);

        return indexer;
    }
}
