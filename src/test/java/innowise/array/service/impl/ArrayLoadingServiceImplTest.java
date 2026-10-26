package innowise.array.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import innowise.array.entity.IntArray;
import innowise.array.exception.ArrayFileReadException;
import innowise.array.exception.InvalidArrayException;
import innowise.array.factory.ArrayFactory;
import innowise.array.factory.IntArrayFactory;
import innowise.array.parser.ArrayLineParser;
import innowise.array.parser.impl.ArrayLineParserImpl;
import innowise.array.reader.ArrayFileReader;
import innowise.array.reader.impl.ArrayFileReaderImpl;
import innowise.array.service.ArrayLoadingService;
import innowise.array.validation.ArrayLineValidation;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ArrayLoadingServiceImplTest {
  private static final Path TEST_DIRECTORY = Path.of("build", "loading-test-data");
  private static final Charset CHARSET = StandardCharsets.UTF_8;

  private final ArrayFileReader reader = new ArrayFileReaderImpl();
  private final ArrayLineValidation validation = new ArrayLineValidation();
  private final ArrayLineParser parser = new ArrayLineParserImpl(validation);
  private final ArrayFactory factory = new IntArrayFactory();
  private final ArrayLoadingService loadingService =
      new ArrayLoadingServiceImpl(reader, parser, factory);
  private final int[] firstValues = {1, 2, 3};
  private final int[] secondValues = {-4, 0, 5};
  private final int[] thirdValues = {7};
  private Path file;

  @BeforeEach
  void createTestDirectory() throws IOException {
    Files.createDirectories(TEST_DIRECTORY);
    Path directory = Files.createTempDirectory(TEST_DIRECTORY, "case-");
    file = directory.resolve("arrays.txt");
  }

  @Test
  @DisplayName("Loading preserves valid line order and continues after invalid lines")
  void loadShouldSkipInvalidLinesAndPreserveValidOrder() throws IOException {
    List<String> lines = List.of("1,2,3", "invalid", "-4, 0, +5", "", "2147483648,1", "7");
    Files.write(file, lines, CHARSET);

    List<IntArray> actual = loadingService.load(file);

    assertAll(
        () -> assertEquals(3, actual.size()),
        () -> assertArrayEquals(firstValues, valuesAt(actual, 0)),
        () -> assertArrayEquals(secondValues, valuesAt(actual, 1)),
        () -> assertArrayEquals(thirdValues, valuesAt(actual, 2)));
  }

  @Test
  @DisplayName("A file containing only invalid lines produces an empty list")
  void loadShouldAcceptFileWithoutValidLines() throws IOException {
    List<String> lines = List.of("", " ", "1,,2", "-2147483649");
    Files.write(file, lines, CHARSET);

    List<IntArray> actual = loadingService.load(file);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("An empty input file produces an empty list")
  void loadShouldAcceptEmptyFile() throws IOException {
    Files.createFile(file);

    List<IntArray> actual = loadingService.load(file);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("File errors propagate instead of becoming an empty successful result")
  void loadShouldPropagateFileError() {
    Executable loading = () -> loadingService.load(file);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, loading);

    assertInstanceOf(IOException.class, actual.getCause());
  }

  @Test
  @DisplayName("Loading propagates the reader's null-path validation")
  void loadShouldRejectNullPath() {
    Path missingPath = null;
    Executable loading = () -> loadingService.load(missingPath);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, loading);

    assertEquals("Input path must not be null", actual.getMessage());
  }

  @Test
  @DisplayName("Loading requires a reader")
  void constructorShouldRejectNullReader() {
    ArrayFileReader missingReader = null;
    Executable construction = () -> new ArrayLoadingServiceImpl(missingReader, parser, factory);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, construction);

    assertEquals("Loading dependencies must not be null", actual.getMessage());
  }

  @Test
  @DisplayName("Loading requires a parser")
  void constructorShouldRejectNullParser() {
    ArrayLineParser missingParser = null;
    Executable construction = () -> new ArrayLoadingServiceImpl(reader, missingParser, factory);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, construction);

    assertEquals("Loading dependencies must not be null", actual.getMessage());
  }

  @Test
  @DisplayName("Loading requires a factory")
  void constructorShouldRejectNullFactory() {
    ArrayFactory missingFactory = null;
    Executable construction = () -> new ArrayLoadingServiceImpl(reader, parser, missingFactory);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, construction);

    assertEquals("Loading dependencies must not be null", actual.getMessage());
  }

  private int[] valuesAt(List<IntArray> arrays, int index) {
    IntArray array = arrays.get(index);
    return array.getArray();
  }
}
