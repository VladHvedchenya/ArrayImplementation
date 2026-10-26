package innowise.array.reader;

import java.nio.file.Path;
import java.util.List;

public interface ArrayFileReader {
    List<String> readLines(Path path);
}
