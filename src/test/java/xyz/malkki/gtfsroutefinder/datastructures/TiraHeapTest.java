package xyz.malkki.gtfsroutefinder.datastructures;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.*;
import java.util.stream.Stream;

public class TiraHeapTest {
    private TiraHeap<Integer> heap = new TiraHeap<>(Comparator.<Integer>naturalOrder());

    @Test
    public void testOffer() {
        heap.offer(1);
        heap.offer(2);
        heap.offer(3);

        assertEquals(3, heap.size());
    }

    @Test
    public void testPoll() {
        heap.offer(8);
        heap.offer(2);
        heap.offer(3);
        heap.offer(1);

        int head = heap.poll();

        assertEquals(1, head);
        assertEquals(3, heap.size());
    }

    @Test
    public void testPollingEmptyHeapDoesNotSetSizeToNegative() {
        Integer i1 = heap.poll();
        Integer i2 = heap.poll();
        Integer i3 = heap.poll();

        assertNull(i1);
        assertNull(i2);
        assertNull(i3);
        assertEquals(0, heap.size());
    }

    @Test
    public void testPeek() {
        heap.offer(9);
        heap.offer(3);
        heap.offer(2);
        heap.offer(4);
        heap.offer(1);

        int head = heap.peek();

        assertEquals(1, head);
        assertEquals(5, heap.size());
    }

    @Test
    public void testPollOrder() {
        int[] values = new int[]{ 9, 52398, 1455, 5115, 64398, 129348, 1, 5259, 45189, 19589, 1598, 5198, 51985, 95815 };

        for (int value : values) {
            heap.offer(value);
        }

        int[] fromHeap = Stream.generate(heap::poll).limit(heap.size()).mapToInt(i -> i).toArray();

        Arrays.sort(values);
        assertArrayEquals(values, fromHeap);
    }

    @Test
    public void testIterator() {
        heap.offer(1);
        heap.offer(2);

        List<Integer> values = new ArrayList<>();

        Iterator<Integer> iterator = heap.iterator();
        while (iterator.hasNext()) {
            values.add(iterator.next());
        }

        assertEquals(2, values.size());
        assertTrue(values.contains(1));
        assertTrue(values.contains(2));
    }
}
