package ascii_art;

/**
 * Exception thrown when input format does not match expected patterns.
 * This exception is used to handle formatting errors in command arguments
 * and parameters in the ASCII art application.
 * @author Tomer Kadosh
 */
public class IncorrectFormatException extends IllegalArgumentException {
    /** Default error message for incorrect format */
    private static final String DEFAULT_MESSAGE = "Invalid format. Please try again.";

    /**
     * Creates a new IncorrectFormatException with the default message.
     */
    public IncorrectFormatException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Creates a new IncorrectFormatException with a custom message.
     *
     * @param message Specific error message describing the format error
     */
    public IncorrectFormatException(String message) {
        super(message);
    }
}