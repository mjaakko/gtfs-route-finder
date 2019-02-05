package xyz.malkki.gtfsroutefinder.common.utils;

public class ArrayUtils {
    private ArrayUtils() {}

    /**
     * Swaps items at indexes i1 and i2 in the array
     * @param array Array
     * @param i1 Index of the first item
     * @param i2 Index of the second item
     */
    public static void swap(Object[] array, int i1, int i2) {
        Object x = array[i1];
        array[i1] = array[i2];
        array[i2] = x;
    }
}
