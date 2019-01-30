package xyz.malkki.gtfsroutefinder.datastructures;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class TiraArrayListTest {
    @Test
    public void testConstructor1() {
        List<String> list = new TiraArrayList<>(10);

        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor2() {
        List<String> list = new TiraArrayList<>();

        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor3() {
        List<String> list = new TiraArrayList<String>("test1", "test2", "test3");

        assertEquals(3, list.size());
    }

    @Test
    public void testCanRemoveExistingItem() {
        List<String> list = new TiraArrayList<String>("test1", "test2", "test3");
        list.remove("test1");

        assertEquals(2, list.size());
        assertFalse(list.contains("test1"));
    }

    @Test
    public void testRemovingNonexistentItemDoesNothing() {
        List<String> list = new TiraArrayList<String>("test1", "test2", "test3");
        list.remove("test4");

        assertEquals(3, list.size());
        assertTrue(list.contains("test1"));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotRemoveOutOfBoundsPositive() {
        List<String> list = new TiraArrayList<>();
        list.remove(10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotRemoveOutOfBoundsNegative() {
        List<String> list = new TiraArrayList<>();
        list.remove(-1);
    }

    @Test
    public void testCanAdd() {
        List<String> list = new TiraArrayList<>();
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
        List<String> list = new TiraArrayList<>();
        list.add(10, "test");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotAddOutOfBoundsNegative() {
        List<String> list = new TiraArrayList<>();
        list.add(-1, "test");
    }

    @Test
    public void testSet() {
        List<String> list = new TiraArrayList<>();
        list.add("test1");

        assertEquals("test1", list.get(0));

        list.set(0, "test2");

        assertEquals("test2", list.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotSetOutOfBoundsPositive() {
        List<String> list = new TiraArrayList<>();
        list.set(10, "test");
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotSetOutOfBoundsNegative() {
        List<String> list = new TiraArrayList<>();
        list.set(-1, "test");
    }

    @Test
    public void testGet() {
        List<String> list = new TiraArrayList<>("test1");

        assertEquals("test1", list.get(0));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotGetOutOfBoundsPositive() {
        List<String> list = new TiraArrayList<>();
        list.get(10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testCannotGetOutOfBoundsNegative() {
        List<String> list = new TiraArrayList<>();
        list.get(-1);
    }
}
