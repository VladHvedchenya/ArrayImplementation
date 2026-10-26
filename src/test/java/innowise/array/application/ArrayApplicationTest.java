package innowise.array.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import innowise.array.exception.InvalidArrayException;
import innowise.array.factory.ArrayFactory;
import innowise.array.factory.IntArrayFactory;
import innowise.array.parser.ArrayLineParser;
import innowise.array.parser.impl.ArrayLineParserImpl;
import innowise.array.reader.ArrayFileReader;
import innowise.array.reader.impl.ArrayFileReaderImpl;
import innowise.array.service.ArrayLoadingService;
import innowise.array.service.ArraySortingService;
import innowise.array.service.ArrayStatisticsService;
import innowise.array.service.impl.ArrayLoadingServiceImpl;
import innowise.array.service.impl.ArraySortingServiceImpl;
import innowise.array.service.impl.ArrayStatisticsImpl;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ArrayApplicationTest {
  private static final Path TEST_DIRECTORY = Path.of("build", "application-test-data");
  private static final Charset CHARSET = StandardCharsets.UTF_8;

  private final ArrayFileReader reader = new ArrayFileReaderImpl();
  private final ArrayLineValidation validation = new ArrayLineValidation();
  private final ArrayLineParser parser = new ArrayLineParserImpl(validation);
  private final ArrayFactory factory = new IntArrayFactory();
  private final ArrayLoadingService loadingService =
      new ArrayLoadingServiceImpl(reader, parser, factory);
  private final ArrayStatisticsService statisticsService = new ArrayStatisticsImpl();
  private final ArraySortingService sortingService = new ArraySortingServiceImpl();
  private final ArrayApplication application =
      new ArrayApplication(loadingService, statisticsService, sortingService, factory);
  private Path file;

  @BeforeEach
  void createTestDirectory() throws IOException {
    Files.createDirectories(TEST_DIRECTORY);
    Path directory = Files.createTempDirectory(TEST_DIRECTORY, "case-");
    file = directory.resolve("arrays.txt");
  }

  @Test
  @DisplayName("Application processes the default example file successfully")
  void runShouldProcessDefaultInput() {
    String[] arguments = new String[0];

    int actual = application.run(arguments);

    assertEquals(0, actual);
  }

  @Test
  @DisplayName("Application accepts an explicit relative file and skips invalid lines")
  void runShouldProcessExplicitInput() throws IOException {
    List<String> lines = List.of("3,1,2", "invalid", "-5,0,4");
    Files.write(file, lines, CHARSET);
    String input = file.toString();
    String[] arguments = {input};

    int actual = application.run(arguments);

    assertEquals(0, actual);
  }

  @Test
  @DisplayName("Application accepts an empty file")
  void runShouldAcceptEmptyFile() throws IOException {
    Files.createFile(file);
    String input = file.toString();
    String[] arguments = {input};

    int actual = application.run(arguments);

    assertEquals(0, actual);
  }

  @Test
  @DisplayName("Missing input files produce a failure exit status")
  void runShouldReportFileError() {
    String input = file.toString();
    String[] arguments = {input};

    int actual = application.run(arguments);

    assertEquals(1, actual);
  }

  @Test
  @DisplayName("Absolute input paths produce a failure exit status")
  void runShouldRejectAbsolutePath() {
    Path absolutePath = file.toAbsolutePath();
    String input = absolutePath.toString();
    String[] arguments = {input};

    int actual = application.run(arguments);

    assertEquals(1, actual);
  }

  @Test
  @DisplayName("Invalid path syntax produces a failure exit status")
  void runShouldReportInvalidPathSyntax() {
    String[] arguments = {"invalid\0path"};

    int actual = application.run(arguments);

    assertEquals(1, actual);
  }

  @Test
  @DisplayName("Application rejects a missing argument array")
  void runShouldRejectNullArguments() {
    String[] arguments = null;

    int actual = application.run(arguments);

    assertEquals(2, actual);
  }

  @Test
  @DisplayName("Application rejects extra arguments")
  void runShouldRejectExtraArguments() {
    String[] arguments = {"data/arrays.txt", "another-file.txt"};

    int actual = application.run(arguments);

    assertEquals(2, actual);
  }

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {"", " ", "\t"})
  @DisplayName("Application rejects missing or blank path arguments")
  void runShouldRejectBlankPathArgument(String input) {
    String[] arguments = {input};

    int actual = application.run(arguments);

    assertEquals(2, actual);
  }

  @Test
  @DisplayName("Application requires all dependencies")
  void constructorShouldRejectNullDependency() {
    ArrayFactory missingFactory = null;
    Executable construction =
        () ->
            new ArrayApplication(loadingService, statisticsService, sortingService, missingFactory);

    InvalidArrayException actual = assertThrows(InvalidArrayException.class, construction);

    assertEquals("Application dependencies must not be null", actual.getMessage());
  }
}
