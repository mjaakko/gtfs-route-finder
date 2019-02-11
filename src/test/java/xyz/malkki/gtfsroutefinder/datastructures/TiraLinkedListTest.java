package xyz.malkki.gtfsroutefinder.datastructures;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TiraLinkedListTest {
    @Test
    public void testConstructor() {
        List<String> list = new TiraLinkedList<>();

        assertEquals(0, list.size());
    }

    @Test
    public void testRemoveWithIndex() {
        List<String> list = new TiraLinkedList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        list.remove(1);

        assertEquals(2, list.size());
        assertFalse(list.contains("test2"));
    }

    @Test
    public void testRemoveWithIndexTail() {
        List<String> list = new TiraLinkedList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        list.remove(2);

        assertEquals(2, list.size());
        assertFalse(list.contains("test3"));
    }

    @Test
    public void testAddMiddle() {
        List<String> list = new TiraLinkedList<>();
        list.add("test1");
        list.add("test2");

        list.add(1, "test3");

        assertEquals(3, list.size());

        assertEquals("test2", list.get(2));
        assertEquals("test3", list.get(1));
        assertEquals("test1", list.get(0));
    }

    @Test
    public void testCanRemoveExistingItem() {
        List<String> list = new TiraLinkedList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        list.remove("test1");

        assertEquals(2, list.size());
        assertFalse(list.contains("test1"));
    }

    @Test
    public void testRemovingNonexistentItemDoesNothing() {
        List<String> list = new TiraLinkedList<String>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        list.remove("test4");

        assertEquals(3, list.size());
        assertTrue(list.contains("test1"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotRemoveOutOfBoundsPositive() {
        List<String> list = new TiraLinkedList<>();
        list.remove(10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotRemoveOutOfBoundsNegative() {
        List<String> list = new TiraLinkedList<>();
        list.remove(-1);
    }

    @Test
    public void testCanAdd() {
        List<String> list = new TiraLinkedList<>();
        list.add("test1");
        list.add("test2");
        list.add("test3");

        assertEquals(3, list.size());
        assertTrue(list.contains("test1"));
        assertTrue(list.contains("test2"));
        assertTrue(list.contains("test3"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotAddOutOfBoundsPositive() {
        List<String> list = new TiraLinkedList<>();
        list.add(10, "test");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotAddOutOfBoundsNegative() {
        List<String> list = new TiraLinkedList<>();
        list.add(-1, "test");
    }

    @Test
    public void testSet() {
        List<String> list = new TiraLinkedList<>();
        list.add("test1");

        assertEquals("test1", list.get(0));

        list.set(0, "test2");

        assertEquals("test2", list.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotSetOutOfBoundsPositive() {
        List<String> list = new TiraLinkedList<>();
        list.set(10, "test");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotSetOutOfBoundsNegative() {
        List<String> list = new TiraLinkedList<>();
        list.set(-1, "test");
    }

    @Test
    public void testGet() {
        List<String> list = new TiraLinkedList<>();
        list.add("test1");

        assertEquals("test1", list.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotGetOutOfBoundsPositive() {
        List<String> list = new TiraLinkedList<>();
        list.get(10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotGetOutOfBoundsNegative() {
        List<String> list = new TiraLinkedList<>();
        list.get(-1);
    }
}
