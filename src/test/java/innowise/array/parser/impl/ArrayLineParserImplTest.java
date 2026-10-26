package innowise.array.parser.impl;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import innowise.array.exception.InvalidArrayException;
import innowise.array.parser.ArrayLineParser;
import innowise.array.validation.ArrayLineValidation;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ArrayLineParserImplTest {

  private static final int MINIMUM_INT = Integer.MIN_VALUE;
  private static final int MAXIMUM_INT = Integer.MAX_VALUE;

  private final ArrayLineValidation validation = new ArrayLineValidation();
  private final ArrayLineParser parser = new ArrayLineParserImpl(validation);

  @ParameterizedTest
  @MethodSource("validLines")
  @DisplayName("Parser converts valid lines to integer arrays in the original order")
  void parseLineShouldReturnParsedValues(String line, int[] expectedValues) {
    int[] actualValues = parser.parseLine(line);

    assertArrayEquals(expectedValues, actualValues);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(
      strings = {
        " ",
        "1,,2",
        "1,2,",
        "1;2",
        "1,2.5",
        "1y1,2",
        "1, ,3",
        "2147483648,1",
        "-2147483649,1",
        "999999999999999999999999,2"
      })
  @DisplayName("Parser rejects invalid lines with the custom exception")
  void parseLineShouldRejectInvalidLine(String line) {
    Executable parsing = () -> parser.parseLine(line);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, parsing);

    assertEquals("Line must contain comma-separated int values", actual.getMessage());
  }

  @Test
  @DisplayName("Parser cannot be constructed without a validator")
  void constructorShouldRejectNullValidation() {
    ArrayLineValidation missingValidation = null;
    Executable construction = () -> new ArrayLineParserImpl(missingValidation);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, construction);

    assertEquals("Array line validation must not be null", actual.getMessage());
  }

  private Stream<Arguments> validLines() {
    return Stream.of(
        Arguments.of("1,2,3", new int[] {1, 2, 3}),
        Arguments.of(" 10, -5, 0, 42 ", new int[] {10, -5, 0, 42}),
        Arguments.of("+1,+2,-3", new int[] {1, 2, -3}),
        Arguments.of("7", new int[] {7}),
        Arguments.of("2147483647,-2147483648", new int[] {MAXIMUM_INT, MINIMUM_INT}),
        Arguments.of("0001,-0002,+0003", new int[] {1, -2, 3}),
        Arguments.of("\t1,\t-2\t", new int[] {1, -2}));
  }
}
