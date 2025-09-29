package ascii_art;

/**
 * Represents available commands in the ASCII art shell.
 * Each command maps to a specific user input string and corresponding action.
 * @author Tomer Kadosh
 */
public enum Command {
    /** Adds characters to the ASCII art character set */
    ADD("add"),
    /** Removes characters from the ASCII art character set */
    REMOVE("remove"),
    /** Changes the resolution of the ASCII art output */
    RES("res"),
    /** Generates and displays ASCII art */
    ASCIIART("asciiArt"),
    /** Changes the output format (console/HTML) */
    OUTPUT("output"),
    /** Exits the program */
    EXIT("exit"),
    /** Displays current character set */
    CHARS("chars"),
    /** Sets brightness rounding method */
    ROUND("round");

    /** Input string that maps to this command */
    private final String input;

    /**
     * Creates a new Command with the specified input string.
     * @param input String that triggers this command
     */
    Command(String input) {
        this.input = input;
    }

    /**
     * Gets the input string for this command.
     * @return Command's input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Converts an input string to its corresponding Command.
     * Case-insensitive matching is used.
     *
     * @param input String to convert to Command
     * @return Matching Command or null if no match found
     */
    public static Command fromString(String input) {
        // Search for matching command
        for (Command cmd : Command.values()) {
            if (cmd.input.equalsIgnoreCase(input)) {
                // Return command if input matches (case-insensitive)
                return cmd;
            }
        }
        return null; // No matching command found
    }
}
