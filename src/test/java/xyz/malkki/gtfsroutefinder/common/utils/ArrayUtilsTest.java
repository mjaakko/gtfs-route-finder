package xyz.malkki.gtfsroutefinder.common.utils;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayUtilsTest {
    @Test
    public void testSwapping() {
        String[] array = new String[]{ "test1", "test2" };

        ArrayUtils.swap(array, 0, 1);

        assertArrayEquals(new String[]{ "test2", "test1" }, array);
    }
}
