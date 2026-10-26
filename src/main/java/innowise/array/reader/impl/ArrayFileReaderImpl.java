package innowise.array.reader.impl;

import innowise.array.exception.ArrayFileReadException;
import innowise.array.reader.ArrayFileReader;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ArrayFileReaderImpl implements ArrayFileReader {
    @Override
    public List<String> readLines(Path path) {
        if (path == null) {
            throw new ArrayFileReadException("Input path must not be null");
        }

        if (path.isAbsolute()) {
            throw new ArrayFileReadException("Input path must be relative");
        }

        Charset charset = StandardCharsets.UTF_8;

        try {
            return Files.readAllLines(path, charset);
        } catch (IOException exception) {
            throw new ArrayFileReadException("Unable to read input file: " + path, exception);
        }
    }
}
