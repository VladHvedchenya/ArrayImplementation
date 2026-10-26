package innowise.array.factory;

import innowise.array.entity.IntArray;

public abstract class ArrayFactory {
    public abstract IntArray createArray(int[] values);
}
