package innowise.array.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import innowise.array.exception.InvalidArrayException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class IntArrayTest {

  private static final int CHANGED_VALUE = 99;

  private final int[] initialValues = {-5, 0, 3, 3, 8};
  private final int[] replacementValues = {42, -7, 42};
  private final int[] emptyValues = new int[0];

  @Test
  @DisplayName("Constructor preserves the provided values and their order")
  public void constructorShouldPreserveProvidedValues() {
    int[] sourceValues = initialValues.clone();

    IntArray intArray = new IntArray(sourceValues);

    assertArrayEquals(initialValues, intArray.getArray());
  }

  @Test
  @DisplayName("Changes to the constructor argument do not affect stored values")
  public void constructorShouldCopyProvidedArray() {
    int[] sourceValues = initialValues.clone();
    IntArray intArray = new IntArray(sourceValues);
    sourceValues[0] = CHANGED_VALUE;

    int[] actualValues = intArray.getArray();

    assertArrayEquals(initialValues, actualValues);
  }

  @Test
  @DisplayName("Constructor accepts an empty array")
  public void constructorShouldAcceptEmptyArray() {
    int[] sourceValues = emptyValues.clone();

    IntArray intArray = new IntArray(sourceValues);

    assertArrayEquals(emptyValues, intArray.getArray());
  }

  @Test
  @DisplayName("Constructor rejects null with the custom exception")
  public void constructorShouldRejectNullArray() {
    int[] invalidValues = null;
    Executable construction = () -> new IntArray(invalidValues);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, construction);

    assertEquals("The array is invalid!", actual.getMessage());
  }

  @Test
  @DisplayName("Changes to the getter result do not affect stored values")
  public void getArrayShouldProtectStoredValues() {
    IntArray intArray = new IntArray(initialValues);
    int[] returnedValues = intArray.getArray();
    returnedValues[0] = CHANGED_VALUE;

    int[] actualValues = intArray.getArray();

    assertArrayEquals(initialValues, actualValues);
  }

  @Test
  @DisplayName("Setter replaces all values, including the array length")
  public void setArrayShouldReplaceStoredValues() {
    IntArray intArray = new IntArray(initialValues);

    intArray.setArray(replacementValues);

    assertArrayEquals(replacementValues, intArray.getArray());
  }

  @Test
  @DisplayName("Changes to the setter argument do not affect stored values")
  public void setArrayShouldCopyProvidedArray() {
    IntArray intArray = new IntArray(initialValues);
    int[] sourceValues = replacementValues.clone();
    intArray.setArray(sourceValues);
    sourceValues[0] = CHANGED_VALUE;

    int[] actualValues = intArray.getArray();

    assertArrayEquals(replacementValues, actualValues);
  }

  @Test
  @DisplayName("Setter accepts an empty array and removes previous values")
  public void setArrayShouldAcceptEmptyArray() {
    IntArray intArray = new IntArray(initialValues);

    intArray.setArray(emptyValues);

    assertArrayEquals(emptyValues, intArray.getArray());
  }

  @Test
  @DisplayName("Setter rejects null and preserves the previous values")
  public void setArrayShouldRejectNullAndPreserveStoredValues() {
    IntArray intArray = new IntArray(initialValues);
    Executable replacement = () -> intArray.setArray(null);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, replacement);

    assertAll(
        () -> assertEquals("The passed array is invalid!", actual.getMessage()),
        () -> assertArrayEquals(initialValues, intArray.getArray()));
  }
}
