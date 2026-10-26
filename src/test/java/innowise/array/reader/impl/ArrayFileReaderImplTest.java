package innowise.array.reader.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import innowise.array.exception.ArrayFileReadException;
import innowise.array.reader.ArrayFileReader;
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

class ArrayFileReaderImplTest {
  private static final Path TEST_DIRECTORY = Path.of("build", "reader-test-data");
  private static final Charset CHARSET = StandardCharsets.UTF_8;

  private final ArrayFileReader reader = new ArrayFileReaderImpl();
  private Path file;

  @BeforeEach
  void createTestDirectory() throws IOException {
    Files.createDirectories(TEST_DIRECTORY);
    Path directory = Files.createTempDirectory(TEST_DIRECTORY, "case-");
    file = directory.resolve("arrays.txt");
  }

  @Test
  @DisplayName("Reader preserves blank lines, invalid content, whitespace, and UTF-8 characters")
  void readLinesShouldPreserveFileContents() throws IOException {
    List<String> expected = List.of("1,2,3", "", "invalid,line", "  -4,+5  ", "café");
    Files.write(file, expected, CHARSET);

    List<String> actual = reader.readLines(file);

    assertEquals(expected, actual);
  }

  @Test
  @DisplayName("Reader returns an empty list for an empty file")
  void readLinesShouldAcceptEmptyFile() throws IOException {
    Files.createFile(file);

    List<String> actual = reader.readLines(file);

    assertTrue(actual.isEmpty());
  }

  @Test
  @DisplayName("Missing files produce a custom exception preserving the I/O cause")
  void readLinesShouldWrapMissingFileError() {
    Executable reading = () -> reader.readLines(file);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, reading);

    assertInstanceOf(IOException.class, actual.getCause());
  }

  @Test
  @DisplayName("Reader rejects a null path")
  void readLinesShouldRejectNullPath() {
    Path missingPath = null;
    Executable reading = () -> reader.readLines(missingPath);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, reading);

    assertEquals("Input path must not be null", actual.getMessage());
  }

  @Test
  @DisplayName("Reader rejects absolute paths")
  void readLinesShouldRejectAbsolutePath() {
    Path absolutePath = file.toAbsolutePath();
    Executable reading = () -> reader.readLines(absolutePath);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, reading);

    assertEquals("Input path must be relative", actual.getMessage());
  }

  @Test
  @DisplayName("Directories cannot be read as input files")
  void readLinesShouldWrapDirectoryError() {
    Path directory = file.getParent();
    Executable reading = () -> reader.readLines(directory);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, reading);

    assertInstanceOf(IOException.class, actual.getCause());
  }

  @Test
  @DisplayName("Invalid UTF-8 produces a custom exception preserving the I/O cause")
  void readLinesShouldRejectMalformedUtf8() throws IOException {
    byte[] content = {(byte) 0xc3, (byte) 0x28};
    Files.write(file, content);
    Executable reading = () -> reader.readLines(file);

    ArrayFileReadException actual = assertThrows(ArrayFileReadException.class, reading);

    assertInstanceOf(IOException.class, actual.getCause());
  }
}
