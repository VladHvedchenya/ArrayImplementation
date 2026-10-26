package innowise.array.service.impl;

import innowise.array.entity.IntArray;
import innowise.array.service.ArrayStatisticsService;
import java.util.Optional;
import java.util.function.IntBinaryOperator;

public class ArrayStatisticsImpl implements ArrayStatisticsService {
  @Override
  public Optional<Integer> findMax(IntArray array) {
    return findExtreme(
        array,
        (currentValue, candidateValue) ->
            currentValue > candidateValue ? currentValue : candidateValue);
  }

  @Override
  public Optional<Integer> findMin(IntArray array) {
    return findExtreme(
        array,
        (currentValue, candidateValue) ->
            currentValue < candidateValue ? currentValue : candidateValue);
  }

  @Override
  public Optional<Long> findSum(IntArray array) {
    if (array == null) {
      return Optional.empty();
    }

    int[] nums = array.getArray();
    long sum = calculateSum(nums);

    return Optional.of(sum);
  }

  @Override
  public Optional<Double> findAverage(IntArray array) {
    if (array == null) {
      return Optional.empty();
    }

    int[] nums = array.getArray();

    if (nums.length == 0) {
      return Optional.empty();
    }

    long sum = calculateSum(nums);
    double average = (double) sum / nums.length;

    return Optional.of(average);
  }

  private Optional<Integer> findExtreme(IntArray array, IntBinaryOperator selector) {
    if (array == null) {
      return Optional.empty();
    }

    int[] values = array.getArray();

    if (values.length == 0) {
      return Optional.empty();
    }

    int extremeValue = values[0];

    for (int index = 1; index < values.length; index++) {
      extremeValue = selector.applyAsInt(extremeValue, values[index]);
    }

    return Optional.of(extremeValue);
  }

  private long calculateSum(int[] values) {
    long sum = 0L;

    for (int value : values) {
      sum += value;
    }

    return sum;
  }
}
