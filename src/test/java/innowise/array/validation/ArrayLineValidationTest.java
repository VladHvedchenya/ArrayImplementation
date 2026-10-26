package innowise.array.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ArrayLineValidationTest {

  private final ArrayLineValidation validation = new ArrayLineValidation();

  @ParameterizedTest
  @ValueSource(
      strings = {
        "1",
        "1,2,3",
        " 10, -5, 0, 42 ",
        "+1,+2,-3",
        "0,-0,+0",
        "2147483647,-2147483648",
        "0001,-0002,+0003",
        "\t1,\t-2\t"
      })
  @DisplayName("Valid integer lines are accepted")
  void isValidShouldAcceptValidLine(String line) {
    boolean actual = validation.isValid(line);

    assertTrue(actual);
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(
      strings = {
        " ",
        "\t",
        "1y1,21,32",
        "1,2.5,3",
        "1,2e3,4",
        "1,NaN,3",
        "1,Infinity,3",
        "1 2 3",
        "1;2;3",
        "1,,3",
        ",1,2",
        "1,2,",
        ",",
        "1, ,3",
        "[1,2,3]",
        "\"1,2,3\"",
        "1,+,3",
        "1,-,3",
        "1,--2,3",
        "1,2 3,4",
        "+ 1,2",
        "1,+-2,3",
        "2147483648,1",
        "-2147483649,1",
        "999999999999999999999999,2",
        "null"
      })
  @DisplayName("Invalid syntax, blank lines, and out-of-range values are rejected")
  void isValidShouldRejectInvalidLine(String line) {
    boolean actual = validation.isValid(line);

    assertFalse(actual);
  }
}
