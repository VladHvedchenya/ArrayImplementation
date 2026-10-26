package innowise.array.service.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import innowise.array.entity.IntArray;
import innowise.array.service.ArrayStatisticsService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArrayStatisticsImplTest {

  private static final int MINIMUM_INT = Integer.MIN_VALUE;
  private static final int MAXIMUM_INT = Integer.MAX_VALUE;

  private final ArrayStatisticsService statisticsService = new ArrayStatisticsImpl();
  private final IntArray positiveArray = new IntArray(new int[] {4, 9, 2, 7});
  private final IntArray negativeArray = new IntArray(new int[] {-8, -3, -15});
  private final IntArray mixedArray = new IntArray(new int[] {4, -7, 0, 12, -2});
  private final IntArray ascendingArray = new IntArray(new int[] {-5, -1, 0, 4, 9});
  private final IntArray descendingArray = new IntArray(new int[] {9, 4, 0, -1, -5});
  private final IntArray equalValuesArray = new IntArray(new int[] {6, 6, 6});
  private final IntArray singleElementArray = new IntArray(new int[] {-4});
  private final IntArray zeroArray = new IntArray(new int[] {0, 0, 0});
  private final IntArray boundaryValuesArray =
      new IntArray(new int[] {MAXIMUM_INT, 0, MINIMUM_INT});
  private final IntArray largeValuesArray = new IntArray(new int[] {MAXIMUM_INT, MAXIMUM_INT});
  private final IntArray smallValuesArray = new IntArray(new int[] {MINIMUM_INT, MINIMUM_INT});
  private final IntArray negativeFractionalArray = new IntArray(new int[] {-5, -2});
  private final IntArray emptyArray = new IntArray(new int[0]);

  @Test
  @DisplayName("Minimum is found among positive values without assuming zero")
  void findMinShouldReturnSmallestPositiveValue() {
    Optional<Integer> actual = statisticsService.findMin(positiveArray);

    assertEquals(Optional.of(2), actual);
  }

  @Test
  @DisplayName("Minimum is found among negative values")
  void findMinShouldReturnSmallestNegativeValue() {
    Optional<Integer> actual = statisticsService.findMin(negativeArray);

    assertEquals(Optional.of(-15), actual);
  }

  @Test
  @DisplayName("Minimum is found among positive, negative, and zero values")
  void findMinShouldReturnSmallestMixedValue() {
    Optional<Integer> actual = statisticsService.findMin(mixedArray);

    assertEquals(Optional.of(-7), actual);
  }

  @Test
  @DisplayName("Minimum can be the first element")
  void findMinShouldReturnFirstElementWhenItIsSmallest() {
    Optional<Integer> actual = statisticsService.findMin(ascendingArray);

    assertEquals(Optional.of(-5), actual);
  }

  @Test
  @DisplayName("Minimum can be the last element")
  void findMinShouldReturnLastElementWhenItIsSmallest() {
    Optional<Integer> actual = statisticsService.findMin(descendingArray);

    assertEquals(Optional.of(-5), actual);
  }

  @Test
  @DisplayName("Minimum is present when all values are equal")
  void findMinShouldReturnRepeatedValue() {
    Optional<Integer> actual = statisticsService.findMin(equalValuesArray);

    assertEquals(Optional.of(6), actual);
  }

  @Test
  @DisplayName("Minimum of a single-element array is that element")
  void findMinShouldReturnOnlyElement() {
    Optional<Integer> actual = statisticsService.findMin(singleElementArray);

    assertEquals(Optional.of(-4), actual);
  }

  @Test
  @DisplayName("Minimum of zero values is present and equals zero")
  void findMinShouldReturnZeroForZeroValues() {
    Optional<Integer> actual = statisticsService.findMin(zeroArray);

    assertEquals(Optional.of(0), actual);
  }

  @Test
  @DisplayName("Minimum supports the complete int range")
  void findMinShouldSupportIntegerBoundaries() {
    Optional<Integer> actual = statisticsService.findMin(boundaryValuesArray);

    assertEquals(Optional.of(MINIMUM_INT), actual);
  }

  @Test
  @DisplayName("Minimum is absent for an empty array")
  void findMinShouldReturnEmptyForEmptyArray() {
    Optional<Integer> actual = statisticsService.findMin(emptyArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Minimum is absent for a null argument")
  void findMinShouldReturnEmptyForNull() {
    IntArray missingArray = null;

    Optional<Integer> actual = statisticsService.findMin(missingArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Finding the minimum preserves the input values and their order")
  void findMinShouldNotModifyInputArray() {
    int[] expectedValues = mixedArray.getArray();

    statisticsService.findMin(mixedArray);

    assertArrayEquals(expectedValues, mixedArray.getArray());
  }

  @Test
  @DisplayName("Maximum is found among positive values")
  void findMaxShouldReturnLargestPositiveValue() {
    Optional<Integer> actual = statisticsService.findMax(positiveArray);

    assertEquals(Optional.of(9), actual);
  }

  @Test
  @DisplayName("Maximum is found among negative values without assuming zero")
  void findMaxShouldReturnLargestNegativeValue() {
    Optional<Integer> actual = statisticsService.findMax(negativeArray);

    assertEquals(Optional.of(-3), actual);
  }

  @Test
  @DisplayName("Maximum is found among positive, negative, and zero values")
  void findMaxShouldReturnLargestMixedValue() {
    Optional<Integer> actual = statisticsService.findMax(mixedArray);

    assertEquals(Optional.of(12), actual);
  }

  @Test
  @DisplayName("Maximum can be the first element")
  void findMaxShouldReturnFirstElementWhenItIsLargest() {
    Optional<Integer> actual = statisticsService.findMax(descendingArray);

    assertEquals(Optional.of(9), actual);
  }

  @Test
  @DisplayName("Maximum can be the last element")
  void findMaxShouldReturnLastElementWhenItIsLargest() {
    Optional<Integer> actual = statisticsService.findMax(ascendingArray);

    assertEquals(Optional.of(9), actual);
  }

  @Test
  @DisplayName("Maximum is present when all values are equal")
  void findMaxShouldReturnRepeatedValue() {
    Optional<Integer> actual = statisticsService.findMax(equalValuesArray);

    assertEquals(Optional.of(6), actual);
  }

  @Test
  @DisplayName("Maximum of a single-element array is that element")
  void findMaxShouldReturnOnlyElement() {
    Optional<Integer> actual = statisticsService.findMax(singleElementArray);

    assertEquals(Optional.of(-4), actual);
  }

  @Test
  @DisplayName("Maximum of zero values is present and equals zero")
  void findMaxShouldReturnZeroForZeroValues() {
    Optional<Integer> actual = statisticsService.findMax(zeroArray);

    assertEquals(Optional.of(0), actual);
  }

  @Test
  @DisplayName("Maximum supports the complete int range")
  void findMaxShouldSupportIntegerBoundaries() {
    Optional<Integer> actual = statisticsService.findMax(boundaryValuesArray);

    assertEquals(Optional.of(MAXIMUM_INT), actual);
  }

  @Test
  @DisplayName("Maximum is absent for an empty array")
  void findMaxShouldReturnEmptyForEmptyArray() {
    Optional<Integer> actual = statisticsService.findMax(emptyArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Maximum is absent for a null argument")
  void findMaxShouldReturnEmptyForNull() {
    IntArray missingArray = null;

    Optional<Integer> actual = statisticsService.findMax(missingArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Finding the maximum preserves the input values and their order")
  void findMaxShouldNotModifyInputArray() {
    int[] expectedValues = mixedArray.getArray();

    statisticsService.findMax(mixedArray);

    assertArrayEquals(expectedValues, mixedArray.getArray());
  }

  @Test
  @DisplayName("Sum can exceed the int range without overflowing")
  void findSumShouldSupportSumBeyondIntegerRange() {
    Optional<Long> actual = statisticsService.findSum(largeValuesArray);

    assertEquals(Optional.of(4294967294L), actual);
  }

  @Test
  @DisplayName("Sum of an empty array is present and equals zero")
  void findSumShouldReturnZeroForEmptyArray() {
    Optional<Long> actual = statisticsService.findSum(emptyArray);

    assertEquals(Optional.of(0L), actual);
  }

  @Test
  @DisplayName("Average preserves the fractional part")
  void findAverageShouldPreserveFractionalPart() {
    Optional<Double> actual = statisticsService.findAverage(positiveArray);

    assertEquals(Optional.of(5.5), actual);
  }

  @Test
  @DisplayName("Average is absent for an empty array")
  void findAverageShouldReturnEmptyForEmptyArray() {
    Optional<Double> actual = statisticsService.findAverage(emptyArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Sum is absent for a null argument")
  void findSumShouldReturnEmptyForNull() {
    IntArray missingArray = null;

    Optional<Long> actual = statisticsService.findSum(missingArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Sum includes positive, negative, and zero values")
  void findSumShouldIncludeAllMixedValues() {
    Optional<Long> actual = statisticsService.findSum(mixedArray);

    assertEquals(Optional.of(7L), actual);
  }

  @Test
  @DisplayName("Sum includes every negative value")
  void findSumShouldAddNegativeValues() {
    Optional<Long> actual = statisticsService.findSum(negativeArray);

    assertEquals(Optional.of(-26L), actual);
  }

  @Test
  @DisplayName("Sum of zero values is present and equals zero")
  void findSumShouldReturnZeroForZeroValues() {
    Optional<Long> actual = statisticsService.findSum(zeroArray);

    assertEquals(Optional.of(0L), actual);
  }

  @Test
  @DisplayName("Sum can fall below the int range without overflowing")
  void findSumShouldSupportSumBelowIntegerRange() {
    Optional<Long> actual = statisticsService.findSum(smallValuesArray);

    assertEquals(Optional.of(-4294967296L), actual);
  }

  @Test
  @DisplayName("Sum of a single-element array is that element")
  void findSumShouldReturnOnlyElement() {
    Optional<Long> actual = statisticsService.findSum(singleElementArray);

    assertEquals(Optional.of(-4L), actual);
  }

  @Test
  @DisplayName("Calculating the sum preserves the input values and their order")
  void findSumShouldNotModifyInputArray() {
    int[] expectedValues = mixedArray.getArray();

    statisticsService.findSum(mixedArray);

    assertArrayEquals(expectedValues, mixedArray.getArray());
  }

  @Test
  @DisplayName("Average is absent for a null argument")
  void findAverageShouldReturnEmptyForNull() {
    IntArray missingArray = null;

    Optional<Double> actual = statisticsService.findAverage(missingArray);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Average remains correct when the sum exceeds the int range")
  void findAverageShouldSupportSumBeyondIntegerRange() {
    Optional<Double> actual = statisticsService.findAverage(largeValuesArray);

    assertEquals(Optional.of((double) MAXIMUM_INT), actual);
  }

  @Test
  @DisplayName("Average remains correct when the sum falls below the int range")
  void findAverageShouldSupportSumBelowIntegerRange() {
    Optional<Double> actual = statisticsService.findAverage(smallValuesArray);

    assertEquals(Optional.of((double) MINIMUM_INT), actual);
  }

  @Test
  @DisplayName("Average preserves a negative fractional result")
  void findAverageShouldPreserveNegativeFractionalPart() {
    Optional<Double> actual = statisticsService.findAverage(negativeFractionalArray);

    assertEquals(Optional.of(-3.5), actual);
  }

  @Test
  @DisplayName("Average of a single-element array is that element")
  void findAverageShouldReturnOnlyElement() {
    Optional<Double> actual = statisticsService.findAverage(singleElementArray);

    assertEquals(Optional.of(-4.0), actual);
  }

  @Test
  @DisplayName("Average of zero values is present and equals zero")
  void findAverageShouldReturnZeroForZeroValues() {
    Optional<Double> actual = statisticsService.findAverage(zeroArray);

    assertEquals(Optional.of(0.0), actual);
  }

  @Test
  @DisplayName("Calculating the average preserves the input values and their order")
  void findAverageShouldNotModifyInputArray() {
    int[] expectedValues = mixedArray.getArray();

    statisticsService.findAverage(mixedArray);

    assertArrayEquals(expectedValues, mixedArray.getArray());
  }
}
