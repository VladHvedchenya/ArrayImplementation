package innowise.array.factory;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import innowise.array.entity.IntArray;
import innowise.array.exception.InvalidArrayException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class IntArrayFactoryTest {
  private final ArrayFactory factory = new IntArrayFactory();
  private final int[] values = {3, -1, 0, 3};
  private final int[] emptyValues = new int[0];

  @Test
  @DisplayName("Factory Method creates an IntArray containing the supplied values")
  void createArrayShouldPreserveValues() {
    IntArray actual = factory.createArray(values);

    assertArrayEquals(values, actual.getArray());
  }

  @Test
  @DisplayName("Factory Method accepts an empty array")
  void createArrayShouldAcceptEmptyValues() {
    IntArray actual = factory.createArray(emptyValues);

    assertArrayEquals(emptyValues, actual.getArray());
  }

  @Test
  @DisplayName("Factory Method preserves entity validation for null values")
  void createArrayShouldRejectNullValues() {
    int[] missingValues = null;
    Executable creation = () -> factory.createArray(missingValues);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, creation);

    assertEquals("The array is invalid!", actual.getMessage());
  }
}
