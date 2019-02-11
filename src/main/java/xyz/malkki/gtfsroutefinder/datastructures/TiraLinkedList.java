package xyz.malkki.gtfsroutefinder.datastructures;

import java.util.AbstractList;

public class TiraLinkedList<E> extends AbstractList<E> {
    private ListElement<E> head;
    private ListElement<E> tail;
    private int itemCount = 0;

    private ListElement<E> getElement(int index) {
        if (index < 0 || index >= itemCount) {
            throw new ArrayIndexOutOfBoundsException();
        }

        if (index <= itemCount / 2) {
            ListElement<E> current = head;
            for(int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            ListElement<E> current = tail;
            for(int i = 0; i < itemCount - index - 1; i++) {
                current = current.prev;
            }
            return current;
        }
    }

    @Override
    public E remove(int index) {
        if (index == 0) {
            ListElement<E> currentHead = head;
            head = currentHead.next;
            if (head == null) {
                tail = null;
            }

            itemCount--;
            return currentHead.value;
        } else {
            ListElement<E> previous = getElement(index - 1);
            ListElement<E> current = previous.next;
            previous.next = current != null ? current.next : null;

            if (previous.next == null) {
                tail = previous;
            } else {
                previous.next.prev = previous;
            }

            itemCount--;
            return current.value;
        }
    }

    @Override
    public void add(int index, E value) {
        if (index == 0) {
            head = new ListElement<>(value, head, null);
            if (head.next == null) {
                tail = head;
            }
        } else {
            ListElement<E> previous = getElement(index - 1);
            ListElement<E> current = previous.next;

            previous.next = new ListElement<>(value, current, previous);
            if (current != null) {
                current.prev = previous.next;
            } else {
                tail = previous.next;
            }
        }

        itemCount++;
    }

    @Override
    public E set(int index, E value) {
        ListElement<E> current = getElement(index);
        E currentValue = current.value;
        current.value = value;

        return currentValue;
    }

    @Override
    public E get(int index) {
        return getElement(index).value;
    }

    @Override
    public int size() {
        return itemCount;
    }

    private static class ListElement<E> {
        E value;
        ListElement<E> next;
        ListElement<E> prev;

        public ListElement(E value, ListElement<E> next, ListElement<E> prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }
}
