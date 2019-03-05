package xyz.malkki.gtfsroutefinder.datastructures;

import java.util.*;

public class TiraHashMap<K, V> extends AbstractMap<K, V> {
    private TiraLinkedList<Entry<K,V>>[] entries;
    private int defaultSize;

    private int itemCount = 0;

    public TiraHashMap() {
        this(10);
    }

    public TiraHashMap(int defaultSize) {
        this.defaultSize = defaultSize;
        this.entries = new TiraLinkedList[defaultSize];
    }

    private void increaseCapacity() {
        TiraLinkedList<Entry<K,V>>[] newArray = new TiraLinkedList[2 * (1 + entries.length)];
        for (TiraLinkedList<Entry<K,V>> list : entries) {
            if (list == null) {
                continue;
            }
            for (Entry<K,V> item : list) {
                int index = getIndex(item.getKey(), newArray.length);

                if (newArray[index] == null) {
                    newArray[index] = new TiraLinkedList<>();
                }
                newArray[index].add(item);
            }
        }

        entries = newArray;
    }


    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    @Override
    public void clear() {
        entries = new TiraLinkedList[defaultSize];

        itemCount = 0;
    }

    @Override
    public boolean containsKey(Object key) {
        int index = getIndex(key, entries.length);

        if (entries[index] == null || entries[index].isEmpty()) {
            return false;
        } else {
            for (Entry<K, V> entry : entries[index]) {
                if (entry.getKey().equals(key)) {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public boolean containsValue(Object value) {
        for (TiraLinkedList<Entry<K, V>> entryList : entries) {
            if (entryList != null) {
                for (Entry<K, V> entry : entryList) {
                    if (entry.getValue().equals(value)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key, entries.length);
        if (entries[index] == null) {
            return null;
        }

        for (int i = 0; i < entries[index].size(); i++) {
            Entry<K, V> e = entries[index].get(i);
            if (e.getKey().equals(key)) {
                entries[index].remove(i);
                itemCount--;
                return e.getValue();
            }
        }

        return null;
    }

    @Override
    public V get(Object key) {
        int index = getIndex(key, entries.length);
        if (entries[index] == null) {
            return null;
        }

        for (Entry<K, V> entry : entries[index]) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }

        return null;
    }

    @Override
    public V put(K key, V value) {
        if (value == null) {
            throw new NullPointerException("Cannot add null");
        }

        if (getLoadFactor() > 1) {
            increaseCapacity();
        }

        Entry<K, V> entry = new AbstractMap.SimpleEntry<>(key, value);

        int index = getIndex(key, entries.length);
        if (entries[index] == null) {
            entries[index] = new TiraLinkedList<>();
        }

        V previousValue = null;

        for (int i = 0; i < entries[index].size(); i++) {
            Entry<K, V> e = entries[index].get(i);
            if (e.getKey().equals(key)) {
                previousValue = e.getValue();
                entries[index].remove(i);
                break;
            }
        }

        entries[index].add(entry);
        if (previousValue == null) {
            //If previous value is null, an item was added to the map
            itemCount++;
        }

        return previousValue;
    }

    private int getIndex(Object o, int arrayLength) {
        return Math.abs(o.hashCode() % arrayLength);
    }

    private double getLoadFactor() {
        return (double)itemCount / (double)entries.length;
    }

    private class EntrySet extends AbstractSet<Entry<K, V>> {
        @Override
        public Iterator<Entry<K, V>> iterator() {
            return new Iterator<Entry<K, V>>() {
                private int listIndex = 0;
                private int valueIndex = 0;

                private int elementListIndex = -1;
                private int elementValueIndex = -1;

                private int removeListIndex = -1;
                private int removeValueIndex = -1;
                private Entry<K, V> next = findNext();

                @Override
                public boolean hasNext() {
                    return next != null;
                }

                @Override
                public Entry<K, V> next() {
                    Entry<K, V> value = next;

                    removeListIndex = elementListIndex;
                    removeValueIndex = elementValueIndex;

                    next = findNext();
                    return value;
                }

                private Entry<K, V> findNext() {
                    Entry<K, V> value = null;
                    while (value == null) {
                        if (listIndex == entries.length) {
                            return null;
                        }

                        List<Entry<K, V>> list = entries[listIndex];
                        value = list == null ? null : list.get(valueIndex);

                        elementListIndex = listIndex;
                        elementValueIndex = valueIndex;

                        valueIndex++;
                        if (list == null || valueIndex == list.size()) {
                            valueIndex = 0;
                            listIndex++;
                        }
                    }

                    return value;
                }

                @Override
                public void remove() {
                    if (removeListIndex == -1 || removeValueIndex == -1) {
                        throw new IllegalStateException();
                    }

                    entries[removeListIndex].remove(removeValueIndex);
                    itemCount--;
                }
            };
        }

        @Override
        public boolean contains(Object o) {
            if (o instanceof Entry) {
                V value = TiraHashMap.this.get(((Entry) o).getKey());
                return value != null && value.equals(((Entry) o).getValue());
            } else {
                return false;
            }
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Entry) {
                return TiraHashMap.this.remove(((Entry) o).getKey()) != null;
            } else {
                return false;
            }
        }

        @Override
        public int size() {
            return itemCount;
        }

        @Override
        public void clear() {
            TiraHashMap.this.clear();
        }

        @Override
        public boolean add(Entry<K, V> entry) {
            int before = TiraHashMap.this.size();
            TiraHashMap.this.put(entry.getKey(), entry.getValue());
            return TiraHashMap.this.size() != before;
        }
    }
}
