package innowise.array.factory;

import innowise.array.entity.IntArray;

public class IntArrayFactory extends ArrayFactory {
    @Override
    public IntArray createArray(int[] values) {
        return new IntArray(values);
    }
}
