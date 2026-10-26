package innowise.array.service.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import innowise.array.entity.IntArray;
import innowise.array.exception.InvalidArrayException;
import innowise.array.service.ArraySortingService;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArraySortingServiceImplTest {

  private static final int MINIMUM_INT = Integer.MIN_VALUE;
  private static final int MAXIMUM_INT = Integer.MAX_VALUE;

  private final ArraySortingService sortingService = new ArraySortingServiceImpl();

  @ParameterizedTest
  @MethodSource("sortingCases")
  @DisplayName("Bubble sort stores all values in ascending order")
  void useBubbleSortShouldSortAscending(int[] sourceValues, int[] expectedValues) {
    IntArray array = new IntArray(sourceValues);

    sortingService.useBubbleSort(array);

    assertArrayEquals(expectedValues, array.getArray());
  }

  @Test
  @DisplayName("Bubble sort rejects null with the custom exception")
  void useBubbleSortShouldRejectNull() {
    IntArray missingArray = null;
    Executable sorting = () -> sortingService.useBubbleSort(missingArray);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, sorting);

    assertEquals("Array must not be null", actual.getMessage());
  }

  @ParameterizedTest
  @MethodSource("sortingCases")
  @DisplayName("Merge sort stores all values in ascending order")
  void useMergeSortShouldSortAscending(int[] sourceValues, int[] expectedValues) {
    IntArray array = new IntArray(sourceValues);

    sortingService.useMergeSort(array);

    assertArrayEquals(expectedValues, array.getArray());
  }

  @Test
  @DisplayName("Merge sort rejects null with the custom exception")
  void useMergeSortShouldRejectNull() {
    IntArray missingArray = null;
    Executable sorting = () -> sortingService.useMergeSort(missingArray);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, sorting);

    assertEquals("Array must not be null", actual.getMessage());
  }

  private Stream<Arguments> sortingCases() {
    return Stream.of(
        Arguments.of(new int[0], new int[0]),
        Arguments.of(new int[] {-7}, new int[] {-7}),
        Arguments.of(new int[] {2, 1}, new int[] {1, 2}),
        Arguments.of(new int[] {3, 2, 1}, new int[] {1, 2, 3}),
        Arguments.of(new int[] {-4, 0, 2, 8}, new int[] {-4, 0, 2, 8}),
        Arguments.of(new int[] {8, 2, 0, -4}, new int[] {-4, 0, 2, 8}),
        Arguments.of(new int[] {5, -2, 0, 5, -9}, new int[] {-9, -2, 0, 5, 5}),
        Arguments.of(new int[] {3, 3, 3}, new int[] {3, 3, 3}),
        Arguments.of(
            new int[] {MAXIMUM_INT, MINIMUM_INT, 0, MAXIMUM_INT, MINIMUM_INT},
            new int[] {MINIMUM_INT, MINIMUM_INT, 0, MAXIMUM_INT, MAXIMUM_INT}));
  }
}
