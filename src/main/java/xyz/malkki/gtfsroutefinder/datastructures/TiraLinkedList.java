package xyz.malkki.gtfsroutefinder.datastructures;

import java.util.AbstractList;
import java.util.List;

public class TiraLinkedList<E> extends AbstractList<E> {
    private ListElement<E> head;

    private ListElement<E> getElement(int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }

        ListElement<E> current = head;
        for(int i = 0; i < index; i++) {
            if (current == null) {
                throw new ArrayIndexOutOfBoundsException();
            }
            current = current.next;
        }
        return current;
    }

    @Override
    public E remove(int index) {
        if (index == 0) {
            ListElement<E> currentHead = head;
            head = currentHead.next;

            return currentHead.value;
        } else {
            ListElement<E> previous = getElement(index - 1);
            ListElement<E> current = previous.next;
            previous.next = current.next;

            return current.value;
        }
    }

    @Override
    public void add(int index, E value) {
        if (index == 0) {
            head = new ListElement<>(value, head);
        } else {
            ListElement<E> previous = getElement(index - 1);
            ListElement<E> current = previous.next;

            previous.next = new ListElement<>(value, current);
        }
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
        int count = 0;

        ListElement current = head;
        while (current != null) {
            count++;
            current = current.next;
        }

        return count;
    }

    private static class ListElement<E> {
        E value;
        ListElement<E> next;

        public ListElement(E value, ListElement<E> next) {
            this.value = value;
            this.next = next;
        }
    }
}
