package ascii_art;

/**
 * Exception thrown when user input does not match expected format or constraints.
 * This exception is used to handle validation failures for user-provided commands
 * and parameters in the ASCII art application.
 * @author Tomer Kadosh
 */
public class UserInputException extends IllegalArgumentException {
    /** Default error message for invalid user input */
    private static final String DEFAULT_MESSAGE = "Invalid input. Please try again.";

    /**
     * Creates a new UserInputException with the default message.
     */
    public UserInputException() {
        super(DEFAULT_MESSAGE);
    }

    /**
     * Creates a new UserInputException with a custom message.
     *
     * @param message Specific error message describing the validation failure
     */
    public UserInputException(String message) {
        super(message);
    }
}
