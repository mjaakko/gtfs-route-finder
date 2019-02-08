package xyz.malkki.gtfsroutefinder.datastructures;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

public class TiraHashSetTest {
    private TiraHashSet<String> set;

    @Before
    public void setup() {
        set = new TiraHashSet<>();
    }

    @Test
    public void testAdd() {
        set.add("test1");
        set.add("test2");

        assertEquals(2, set.size());
    }

    @Test
    public void testAddMany() {
        for (int i = 0; i < 1000000; i++) {
            set.add(String.valueOf(i));
        }

        assertEquals(1000000, set.size());
    }

    @Test
    public void testCannotAddSameItemTwice() {
        set.add("test1");
        set.add("test1");

        assertEquals(1, set.size());
    }

    @Test
    public void testContains() {
        set.add("test1");

        assertTrue(set.contains("test1"));
    }

    @Test
    public void testDoesNotContain() {
        set.add("test1");

        assertFalse(set.contains("test2"));
    }

    @Test
    public void testClear() {
        set.add("test1");
        set.add("test2");

        assertEquals(2, set.size());

        set.clear();

        assertEquals(0, set.size());
    }

    @Test
    public void testRemove() {
        set.add("test1");

        assertTrue(set.contains("test1"));
        assertEquals(1, set.size());

        set.remove("test1");

        assertFalse(set.contains("test1"));
        assertEquals(0, set.size());
    }

    @Test
    public void testCannotRemoveNonexistent() {
        assertFalse(set.contains("test1"));
        assertEquals(0, set.size());

        set.remove("test1");

        assertEquals(0, set.size());
    }

    @Test
    public void testIterator() {
        set.add("test1");
        set.add("test2");

        List<String> strings = new ArrayList<>();
        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()) {
            strings.add(iterator.next());
        }

        assertTrue(strings.contains("test1"));
        assertTrue(strings.contains("test2"));
        assertEquals(2, strings.size());
    }

    @Test
    public void testIteratorRemove() {
        set.add("test1");
        set.add("test2");
        set.add("test3");

        assertTrue(set.contains("test3"));
        assertEquals(3, set.size());

        Iterator<String> iterator = set.iterator();
        while(iterator.hasNext()) {
            String s = iterator.next();
            if ("test3".equals(s)) {
                iterator.remove();
            }
        }

        assertEquals(2, set.size());
        assertFalse(set.contains("test3"));
    }
}
