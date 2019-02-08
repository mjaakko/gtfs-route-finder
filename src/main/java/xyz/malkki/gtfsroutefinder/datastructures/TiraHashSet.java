package xyz.malkki.gtfsroutefinder.datastructures;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.List;

public class TiraHashSet<E> extends AbstractSet<E> {
    private TiraLinkedList<E>[] values = new TiraLinkedList[10];

    private int itemCount = 0;

    private void increaseCapacity() {
        TiraLinkedList<E>[] newArray = new TiraLinkedList[2 * (1 + values.length)];
        for (TiraLinkedList<E> list : values) {
            if (list == null) {
                continue;
            }
            for (E item : list) {
                int index = getIndex(item);

                if (newArray[index] == null) {
                    newArray[index] = new TiraLinkedList<>();
                }
                newArray[index].add(item);
            }
        }

        values = newArray;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int listIndex = 0;
            private int valueIndex = 0;

            private int elementListIndex = -1;
            private int elementValueIndex = -1;
            private E next = findNext();

            @Override
            public boolean hasNext() {
                return next != null;
            }

            @Override
            public E next() {
                E value = next;
                next = findNext();
                return value;
            }

            private E findNext() {
                E value = null;
                while (value == null) {
                    if (listIndex == values.length) {
                        return null;
                    }

                    List<E> list = values[listIndex];
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
                if (elementListIndex == -1 || elementValueIndex == -1) {
                    throw new IllegalStateException();
                }

                values[elementListIndex].remove(elementValueIndex);
                itemCount--;
            }
        };
    }

    @Override
    public int size() {
        return itemCount;
    }

    @Override
    public boolean contains(Object o) {
        int index = getIndex(o);

        if (values[index] == null) {
            return false;
        }

        for (E value : values[index]) {
            if (value.equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        int index = getIndex(o);

        if (values[index] == null) {
            return false;
        }

        for (int i = 0; i < values[index].size(); i++) {
            if (values[index].get(i).equals(o)) {
                values[index].remove(i);
                itemCount--;
                return true;
            }
        }

        return false;
    }

    @Override
    public void clear() {
        values = new TiraLinkedList[10];

        itemCount = 0;
    }

    @Override
    public boolean add(E item) {
        if (getLoadFactor() > 1) {
            increaseCapacity();
        }

        int index = getIndex(item);

        if (values[index] == null) {
            values[index] = new TiraLinkedList<>();
        }

        for (E value : values[index]) {
            if (value.equals(item)) {
                return false;
            }
        }

        values[index].add(item);
        itemCount++;
        return true;
    }

    private int getIndex(Object o) {
        return Math.abs(o.hashCode() % values.length);
    }

    private double getLoadFactor() {
        return (double)itemCount / (double)values.length;
    }
}
