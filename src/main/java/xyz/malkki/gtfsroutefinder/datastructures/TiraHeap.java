package xyz.malkki.gtfsroutefinder.datastructures;

import java.util.AbstractQueue;
import java.util.Comparator;
import java.util.Iterator;

import static xyz.malkki.gtfsroutefinder.common.utils.ArrayUtils.swap;

/**
 * Min-heap, implementation based on https://en.wikipedia.org/wiki/Binary_heap
 * @param <E>
 */
public class TiraHeap<E> extends AbstractQueue<E> {
    private Object[] array = new Object[10];
    private int itemCount = 0;

    private Comparator<E> comparator;

    public TiraHeap(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    private void increaseCapacity() {
        Object[] newArray = new Object[2 * (1 + array.length)];

        System.arraycopy(array, 0, newArray, 0, itemCount);

        array = newArray;
    }

    private void heapify(Object[] obj, int i) {
        int left = 2*i + 1;
        int right = 2*i + 2;

        int min = i;

        if (left < itemCount && comparator.compare((E)array[left], (E)array[min]) < 0) {
            min = left;
        }

        if (right < itemCount && comparator.compare((E)array[right], (E)array[min]) < 0) {
            min = right;
        }

        if (min != i) {
            swap(array, i, min);
            heapify(array, min);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < itemCount;
            }

            @Override
            public E next() {
                return (E)array[index++];
            }
        };
    }

    @Override
    public int size() {
        return itemCount;
    }

    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }

        if (itemCount == array.length) {
            increaseCapacity();
        }

        int index = itemCount++;
        array[index] = e;
        siftUp(index);

        return true;
    }

    private void siftUp(int index) {
        int parent = (index - 1 ) / 2;

        if (comparator.compare((E)array[parent], (E)array[index]) > 0) {
            swap(array, parent, index);

            siftUp(parent);
        }
    }

    @Override
    public E poll() {
        E head = (E)array[0];
        if (head == null) {
            return null;
        }

        swap(array, 0, --itemCount);
        heapify(array, 0);

        return head;
    }

    @Override
    public E peek() {
        return (E)array[0];
    }
}
