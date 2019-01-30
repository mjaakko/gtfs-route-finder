package xyz.malkki.gtfsroutefinder.datastructures;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Objects;

public class TiraArrayList<E> extends AbstractList<E> {
    private Object[] array;

    private int itemCount;

    public TiraArrayList(int size) {
        array = new Object[size];

        itemCount = 0;
    }

    public TiraArrayList(E... items) {
        array = items;
        itemCount = items.length;
    }

    private void increaseCapacity() {
        Object[] newArray = new Object[2 * (1 + array.length)];

        System.arraycopy(array, 0, newArray, 0, itemCount);

        array = newArray;
    }

    private void checkIndexInclusive(int index) {
        if (index < 0 || index >= itemCount) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    private void checkIndexExclusive(int index) {
        if (index < 0 || index > itemCount) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public E remove(int index) {
        checkIndexInclusive(index);

        E item = (E)array[index];
        if (index + 1 < itemCount) {
            System.arraycopy(array, index + 1, array, index, itemCount - (index + 1));
        } else {
            array[index] = null;
        }

        itemCount--;

        return item;
    }

    @Override
    public void add(int index, E value) {
        checkIndexExclusive(index);

        if (itemCount == array.length) {
            increaseCapacity();
        }

        System.arraycopy(array, index, array, index + 1, itemCount - index);
        array[index] = value;

        itemCount++;
    }

    @Override
    public E set(int index, E value) {
        checkIndexInclusive(index);

        E item = (E)array[index];
        array[index] = value;
        return item;
    }

    @Override
    public E get(int index) {
        checkIndexInclusive(index);

        return (E)array[index];
    }

    @Override
    public int size() {
        return itemCount;
    }
}
