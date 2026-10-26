package innowise.array.service.impl;

import innowise.array.entity.IntArray;
import innowise.array.exception.InvalidArrayException;
import innowise.array.factory.ArrayFactory;
import innowise.array.parser.ArrayLineParser;
import innowise.array.reader.ArrayFileReader;
import innowise.array.service.ArrayLoadingService;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ArrayLoadingServiceImpl implements ArrayLoadingService {
    private final Logger logger = LogManager.getLogger(ArrayLoadingServiceImpl.class);

    private final ArrayFileReader reader;
    private final ArrayLineParser parser;
    private final ArrayFactory factory;

    public ArrayLoadingServiceImpl(
            ArrayFileReader reader,
            ArrayLineParser parser,
            ArrayFactory factory) {
        if (reader == null || parser == null || factory == null) {
            throw new InvalidArrayException("Loading dependencies must not be null");
        }

        this.reader = reader;
        this.parser = parser;
        this.factory = factory;
    }

    @Override
    public List<IntArray> load(Path path) {
        List<String> lines = reader.readLines(path);
        List<IntArray> arrays = new ArrayList<>();
        int lineNumber = 0;
        int skippedCount = 0;

        for (String line : lines) {
            lineNumber++;

            try {
                int[] values = parser.parseLine(line);
                IntArray array = factory.createArray(values);
                arrays.add(array);
            } catch (InvalidArrayException exception) {
                skippedCount++;
                String reason = exception.getMessage();
                logger.warn("Skipping line {} in {}: {}. Input: [{}]", lineNumber, path, reason, line);
            }
        }

        int loadedCount = arrays.size();
        logger.info(
                "Loaded {} arrays from {}; skipped {} invalid lines", loadedCount, path, skippedCount);
        return arrays;
    }
}
