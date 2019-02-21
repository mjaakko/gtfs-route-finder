package xyz.malkki.gtfsroutefinder.datastructures;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class TiraHashMapTest {
    private TiraHashMap<String, String> map;

    @Before
    public void setup() {
        map = new TiraHashMap<>();
    }

    @Test
    public void testPutDifferentKey() {
        map.put("test", "value");
        map.put("test_key", "test_value");

        assertEquals(2, map.size());
    }

    @Test
    public void testPutSameKey() {
        map.put("test", "value");

        assertEquals("value", map.get("test"));

        String replaced = map.put("test", "new");
        assertEquals("value", replaced);
        assertEquals("new", map.get("test"));
        assertEquals(1, map.size());
    }

    @Test
    public void testPutMany() {
        for (int i = 0; i < 1000000; i++) {
            map.put(String.valueOf(i), "test");
        }

        assertEquals(1000000, map.size());
    }

    @Test
    public void testGetNonexistent() {
        assertNull(map.get("test"));
    }

    @Test
    public void testRemoveNonexistent() {
        map.put("new", "value");

        assertNull(map.remove("test"));
        assertEquals(1, map.size());
    }

    @Test
    public void testContainsKey() {
        map.put("test", "value");

        assertTrue(map.containsKey("test"));
    }

    @Test
    public void testDoesNotContainNonexistentKey() {
        map.put("test", "value");

        assertFalse(map.containsKey("key"));
    }

    @Test
    public void testContainsValue() {
        map.put("test", "value");

        assertTrue(map.containsValue("value"));
    }

    @Test
    public void testDoesNotContainNonexistentValue() {
        map.put("test", "test");

        assertFalse(map.containsValue("value"));
    }

    @Test
    public void testClear() {
        map.put("test", "value");
        map.put("key", "value");

        assertEquals(2, map.size());

        map.clear();

        assertEquals(0, map.size());
    }

    @Test
    public void testEntrySetClear() {
        map.put("test", "value");
        map.put("key", "value");

        assertEquals(2, map.size());
        assertEquals(2, map.entrySet().size());

        map.entrySet().clear();

        assertEquals(0, map.size());
        assertEquals(0, map.entrySet().size());
    }

    @Test
    public void testEntrySetContains() {
        map.put("test", "value");

        assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<>("test", "value")));
    }

    @Test
    public void testEntrySetDoesNotContainNonexistent() {
        assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<>("test", "value")));
    }

    @Test
    public void testEntrySetAdd() {
        assertFalse(map.containsKey("test"));

        map.entrySet().add(new AbstractMap.SimpleEntry<>("test", "value"));

        assertTrue(map.containsKey("test"));
        assertEquals("value", map.get("test"));
        assertEquals(1, map.size());
    }

    @Test
    public void testEntrySetRemove() {
        map.put("test", "value");
        assertEquals("value", map.get("test"));

        map.entrySet().remove(new AbstractMap.SimpleEntry<>("test", "value"));

        assertFalse(map.containsKey("test"));
        assertEquals(0, map.size());
    }

    @Test
    public void testEntrySetIterator() {
        map.put("test", "value");

        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();

        map.entrySet().iterator().forEachRemaining(e -> {
            keys.add(e.getKey());
            values.add(e.getValue());
        });

        assertEquals(1, keys.size());
        assertEquals(1, values.size());
        assertTrue(keys.contains("test"));
        assertTrue(values.contains("value"));
    }

    @Test
    public void testEntrySetIteratorRemove() {
        map.put("test", "value");
        map.put("key", "test_value");

        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();

            if (entry.getKey().equals("key")) {
                iterator.remove();
            }
        }

        assertEquals(1, map.size());
        assertFalse(map.containsKey("key"));
    }

    @Test(expected = IllegalStateException.class)
    public void testEntrySetIteratorCannotRemoveBeforeCallingNext() {
        map.entrySet().iterator().remove();
    }

    @Test
    public void testEntrySetDoesNotContainOtherTypes() {
        map.put("test", "value");

        assertFalse(map.entrySet().contains(new Object()));
    }

    @Test
    public void testCannotRemoveOtherTypesFromEntrySet() {
        map.put("test", "value");

        assertFalse(map.entrySet().remove(new Object()));
    }

    @Test(expected = NullPointerException.class)
    public void testCannotAddNull() {
        map.put("test", null);
    }

    @Test
    public void testDoesNotContainKeyThatHasSameHashcode() {
        TiraHashMap<TestInteger, String> map = new TiraHashMap<>();
        map.put(new TestInteger(13), "test");

        assertFalse(map.containsKey(new TestInteger(3)));
    }

    @Test
    public void testCannotRemoveWithKeyThatHasSameHashcode() {
        TiraHashMap<TestInteger, String> map = new TiraHashMap<>();
        map.put(new TestInteger(13), "test");

        assertNull(map.remove(new TestInteger(3)));
    }

    @Test
    public void testCannotGetWithKeyThatHasSameHashcode() {
        TiraHashMap<TestInteger, String> map = new TiraHashMap<>();
        map.put(new TestInteger(13), "test");

        assertNull(map.get(new TestInteger(3)));
    }

    private static class TestInteger {
        public final int value;

        public TestInteger(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestInteger that = (TestInteger) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value % 10;
        }
    }
}
