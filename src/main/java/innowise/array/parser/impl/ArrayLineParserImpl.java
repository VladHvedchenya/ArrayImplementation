package innowise.array.parser.impl;

import innowise.array.exception.InvalidArrayException;
import innowise.array.parser.ArrayLineParser;
import innowise.array.validation.ArrayLineValidation;

public class ArrayLineParserImpl implements ArrayLineParser {
    private static final String DELIMITER_REGEX = ",";

    private final ArrayLineValidation arrayLineValidation;

    public ArrayLineParserImpl(ArrayLineValidation arrayLineValidation) {
        if (arrayLineValidation == null) {
            throw new InvalidArrayException("Array line validation must not be null");
        }

        this.arrayLineValidation = arrayLineValidation;
    }

    @Override
    public int[] parseLine(String line) {
        if (!arrayLineValidation.isValid(line)) {
            throw new InvalidArrayException("Line must contain comma-separated int values");
        }

        String[] values = line.split(DELIMITER_REGEX);
        int[] result = new int[values.length];

        for (int i = 0; i < values.length; i++) {
            String number = values[i].strip();
            result[i] = Integer.parseInt(number);
        }

        return result;
    }
}
