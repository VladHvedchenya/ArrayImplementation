package innowise.array.service.impl;

import innowise.array.entity.IntArray;
import innowise.array.exception.InvalidArrayException;
import innowise.array.service.ArraySortingService;

public class ArraySortingServiceImpl implements ArraySortingService {
    @Override
    public void useBubbleSort(IntArray array) {
        if (array == null) {
            throw new InvalidArrayException("Array must not be null");
        }

        int[] nums = array.getArray();

        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - 1; j++) {
                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }

        array.setArray(nums);
    }

    @Override
    public void useMergeSort(IntArray array) {
        if (array == null) {
            throw new InvalidArrayException("Array must not be null");
        }

        int[] nums = array.getArray();
        mergeSort(nums);
        array.setArray(nums);
    }

    private void mergeSort(int[] nums) {
        if (nums.length < 2) {
            return;
        }

        int mid = nums.length / 2;
        int[] left = new int[mid];
        int[] right = new int[nums.length - mid];

        for (int i = 0; i < left.length; i++) {
            left[i] = nums[i];
        }

        for (int i = 0; i < right.length; i++) {
            right[i] = nums[mid + i];
        }

        mergeSort(left);
        mergeSort(right);

        int i = 0;
        int j = 0;
        int k = 0;

        while (i < left.length && j < right.length) {
            if (left[i] <= right[j]) {
                nums[k] = left[i];
                i++;
            } else {
                nums[k] = right[j];
                j++;
            }
            k++;
        }

        while (i < left.length) {
            nums[k] = left[i];
            i++;
            k++;
        }

        while (j < right.length) {
            nums[k] = right[j];
            j++;
            k++;
        }
    }
}
