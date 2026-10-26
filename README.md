# ArrayImplementation

Educational Java 17 application for loading, validating, analyzing, and sorting integer arrays.

## Requirements

- JDK 17 installed and available through `JAVA_HOME`.
- Internet access for the first Gradle build to download Gradle and dependencies.

## Build, test, and run

Run commands from the project root.

Windows PowerShell:

```powershell
.\gradlew.bat clean build
.\gradlew.bat test
.\gradlew.bat run
.\gradlew.bat run --args="data/arrays.txt"
```

Linux and macOS:

```sh
sh ./gradlew clean build
sh ./gradlew test
sh ./gradlew run
sh ./gradlew run --args="data/arrays.txt"
```

The Gradle Wrapper supplies the project's Gradle version. A separate Gradle installation is not required.
`build` runs JUnit 5 tests and creates application distributions in `build/distributions/`.
The HTML test report is available at `build/reports/tests/test/index.html` after a test run.
`run` uses `data/arrays.txt` by default; an optional argument selects another relative input path.

## IntelliJ IDEA

Import the root `build.gradle` as a Gradle project. Use the Gradle Wrapper and select JDK 17 for both
Project SDK and Gradle JVM. Build and run tests through Gradle so the IDE and command line use the
same configuration. Local IntelliJ IDEA settings are excluded from Git.

## Project layout

- `src/main/java/innowise/array` - application packages.
- `src/test/java/innowise/array` - JUnit 5 tests.
- `data/arrays.txt` - valid and invalid input lines; one comma-separated integer array per line.
- `config/log4j2.xml` - console and rolling file logging configuration.
- `docs/learning-guide.md` - detailed Russian-language explanation of the implementation.
- `build/` - generated classes, reports, distributions, and temporary test input files.
- `logs/` - generated log files, excluded from Git.

Gradle copies files from `config/` onto the application classpath so Log4j2 discovers its configuration
automatically. Logs at INFO level and above go to the console and `logs/application.log`, relative to
the working directory. The active log rolls over at 5 MB, retaining up to five compressed archives.
Run the application from the project root so input and log paths resolve consistently.

## Input format

Input files use UTF-8. Each valid line contains one or more decimal `int` values separated by commas:

```text
1,2,3
10, -5, 0, 42
+1,+2,-3
2147483647,0,-2147483648
```

Spaces around numbers and optional signs are accepted. Empty lines, missing values, other separators,
letters, decimal fractions, and values outside the `int` range are rejected.
Do not include square brackets around arrays.

The sample file contains 10 valid lines and 24 invalid lines. Invalid lines are skipped with a WARN
message containing the source path, line number, reason, and original text. A missing or unreadable
file fails the operation instead of being treated as an empty file. An empty file or a file containing
only invalid lines successfully produces no arrays.

## Architecture

```text
ArrayApplication
  -> ArrayLoadingService
       -> ArrayFileReader: UTF-8 file -> List<String>
       -> ArrayLineParser -> ArrayLineValidation: String -> int[]
       -> ArrayFactory -> IntArrayFactory: int[] -> IntArray
  -> ArrayStatisticsService: min, max, sum, average
  -> ArraySortingService: bubble sort, merge sort
```

The reader only reads. It does not parse or validate file contents. The loading service coordinates
the reader, parser, and factory. Dependencies are passed through constructors without a framework.
`ArrayFactory` declares a Factory Method that `IntArrayFactory` overrides to construct `IntArray`.
Entity fixtures in tests are constructed directly; the factory tests exercise the factory itself.

`IntArray` copies constructor arguments, setter arguments, and getter results. Both sorts update the
entity through its setter and sort in ascending order. The application sorts two independent copies
so the original data is preserved and each algorithm receives the same unsorted input.

Statistics return `Optional`: null input has no result; an empty array has no minimum, maximum, or
average, but its sum is `0L`. Sums use `long` to avoid overflowing when adding `int` values.
Sorting null input throws `InvalidArrayException`; sorting an empty array is allowed.

## Errors and exit codes

- `0`: the file was processed, including an empty file or a file with only invalid lines.
- `1`: a file could not be read, a path is invalid or absolute, or array processing failed.
- `2`: arguments are invalid: more than one argument or a blank path.

`ArrayFileReadException` wraps I/O errors and retains their original cause. `InvalidArrayException`
represents invalid array-related input. The application logs fatal failures at ERROR level with the
exception stack trace; rejected individual lines are WARN events and do not stop the remaining lines.

## Tests

Tests cover defensive copying, statistics, both sorting algorithms, validation, parsing, factory
creation, UTF-8 file reading, row-level recovery, and application exit codes. Reader and loading tests
create isolated files under relative `build/` directories; no external test files live beside Java
classes. A clean build removes these generated fixtures.

The only application-defined static method is the JVM entry point, `main`.
No Lombok, `Objects`, `equals`, or `hashCode` implementation is used.
