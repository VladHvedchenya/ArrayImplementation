package innowise.array.validation;

public class ArrayLineValidation {
    private static final String ARRAY_LINE_REGEX = "\\s*[+-]?[0-9]+(\\s*,\\s*[+-]?[0-9]+)*\\s*";
    private static final String DELIMITER_REGEX = ",";

    public boolean isValid(String line) {
        if (line == null || line.isBlank()) {
            return false;
        }

        if (!line.matches(ARRAY_LINE_REGEX)) {
            return false;
        }

        String[] values = line.split(DELIMITER_REGEX);

        try {
            for (String value : values) {
                String number = value.strip();
                Integer.parseInt(number);
            }
        } catch (NumberFormatException exception) {
            return false;
        }

        return true;
    }
}
