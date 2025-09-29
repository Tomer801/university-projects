package ascii_art;

/**
 * Constants for error messages and status notifications in the ASCII art application.
 * This class provides standardized messages for various error conditions and state changes
 * that can occur during ASCII art generation and manipulation.
 * @author Tomer Kadosh
 */
public class AsciiArtConstant {
    /** Error message for invalid character addition format */
    public final static String FORMAT_ERROR_ADD = "Did not add due to incorrect format.";

    /** Error message for invalid character removal format */
    public final static String FORMAT_ERROR_REMOVE = "Did not remove due to incorrect format.";

    /** Error message for resolution change exceeding image boundaries */
    public final static String FORMAT_ERROR_RESOLUTION_BOUNDARIES =
            "Did not change resolution due to exceeding boundaries.";

    /** Status message template for resolution changes */
    public final static String CHANGE_RESOLUTION = "Resolution set to ";

    /** Error message for invalid resolution change format */
    public final static String FORMAT_ERROR_RESOLUTION =
            "Did not change resolution due to incorrect format.";

    /** Error message for invalid rounding method format */
    public final static String FORMAT_ERROR_ROUND =
            "Did not change rounding method due to incorrect format.";

    /** Error message for invalid output method format */
    public final static String FORMAT_ERROR_OUTPUT =
            "Did not change output method due to incorrect format.";

    /** Error message for insufficient character set size */
    public final static String USER_ERROR_CHARSET =
            "Did not execute. Charset is too small.";

    /** Error message for invalid command input */
    public final static String USER_ERROR_COMMAND =
            "Did not execute due to incorrect command.";
}