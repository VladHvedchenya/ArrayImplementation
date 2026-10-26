package innowise.array.entity;

import innowise.array.exception.InvalidArrayException;

import java.util.Arrays;

public class IntArray {
    private int[] array;

    public IntArray(int[] array) {
        if (array == null) {
            throw new InvalidArrayException("The array is invalid!");
        }

        this.array = Arrays.copyOf(array, array.length);
    }

    public int[] getArray() {
        return Arrays.copyOf(array, array.length);
    }

    public void setArray(int[] array) {
        if (array == null) {
            throw new InvalidArrayException("The passed array is invalid!");
        }

        this.array = Arrays.copyOf(array, array.length);
    }
}
