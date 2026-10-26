package innowise.array.exception;

public class ArrayFileReadException extends RuntimeException {
    public ArrayFileReadException(String message) {
        super(message);
    }

    public ArrayFileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
