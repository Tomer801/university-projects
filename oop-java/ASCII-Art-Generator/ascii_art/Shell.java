package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;
import java.io.IOException;
import java.util.Map;
import static java.lang.System.exit;

/**
 * A command-line interface shell for ASCII art generation and manipulation.
 * Provides functionality to load images, convert them to ASCII art, and manipulate
 * the character set and output formatting.
 * @author Tomer Kadosh
 * @see AsciiArtAlgorithm
 */
public class Shell {
    // Command arguments and output constants
    /** Console output */
    public static final String CONSOLE = "console";
    /** HTML output */
    public static final String HTML = "html";
    /** Output file name for HTML */
        public static final String OUTPUT_HTML = "output.html";
    /** Font name for HTML output */
    public static final String FONT_NAME = "Courier New";
    /** Command line arguments */
    public static final String UP = "up";
    /** Command line arguments */
    public static final String DOWN = "down";
    /** Command line arguments */
    public static final String SPACE = "space";
    /** Command line arguments */
    public static final String ALL = "all";
    /** Command line arguments */
    public static final int LEGAL_RANGE_STRING = 3;
    /** Command line arguments */

    /** Default character set for ASCII art */
    public static char[] DEFAULT_CHAR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /** Initial resolution setting */
    public final static int DEFAULT_RESOLUTION = 2;
    /** Default console output handler */
    public final static AsciiOutput OUTPUT = new ConsoleAsciiOutput();

    // Instance fields for shell state management
    private KeyboardInput keyboardInput = KeyboardInput.getObject();
    private int resolution;                // Current resolution setting
    private SubImgCharMatcher charMatcher; // Handles character-brightness matching
    private AsciiOutput output;            // Current output handler
    private Image image;                   // Source image being processed
    private int lastResolution;            // Caches last used resolution
    private char[][] lastBrightness;       // Caches last generated ASCII art

    /**
     * Initializes shell with default settings.
     */
    public Shell() {
        this.resolution = DEFAULT_RESOLUTION;
        this.charMatcher = new SubImgCharMatcher(DEFAULT_CHAR);
        this.output = OUTPUT;
        this.image = null;
        this.lastBrightness = null;
    }

    /**
     * Starts the interactive shell and processes user commands.
     * @param imageName Path to the image file to process
     */
    public void run(String imageName) {
        // Load and validate source image
        try {
            image = new Image(imageName);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            return;
        }

        // Main command processing loop
        while (true) {
            String[] userInput = processUserInput();
            String arg1 = userInput[0];
            String arg2 = userInput[1];
            Command command = Command.fromString(arg1);

            try {
                if (command == null) {
                    throw new UserInputException(AsciiArtConstant.USER_ERROR_COMMAND);
                }
                executeCommand(command, arg2);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Processes and executes the given command.
     * @throws IncorrectFormatException if command format is invalid
     * @throws UserInputException if command parameters are invalid
     */
    private void executeCommand(Command command, String arg2)
            throws IncorrectFormatException, UserInputException {
        switch (command) {
            case EXIT -> exit(0);
            case CHARS -> printCurrentCharset();
            case ADD -> handleAddCommand(arg2);
            case REMOVE -> handleRemoveCommand(arg2);
            case RES -> handleResCommand(arg2);
            case ROUND -> handleRoundCommand(arg2);
            case OUTPUT -> handelOutputCommand(arg2);
            case ASCIIART -> handleAsciiArtCommand();
        }
    }

    /**
     * Generates and displays ASCII art using current settings.
     * @throws UserInputException if character set is insufficient
     */
    private void handleAsciiArtCommand() throws UserInputException {
        // Convert character set to array
        char[] asciiCharset = new char[charMatcher.getBrightnessMap().size()];
        int i = 0;
        for (Map.Entry<Character, Double> entry : charMatcher.getBrightnessMap().entrySet()) {
            asciiCharset[i++] = entry.getKey();
        }

        // Validate minimum character set size
        if (i < 2) {
            throw new UserInputException(AsciiArtConstant.USER_ERROR_CHARSET);
        }

        // Generate new ASCII art if needed
        if (lastBrightness == null || lastResolution != resolution) {
            AsciiArtAlgorithm asciiArt = new AsciiArtAlgorithm(image, asciiCharset, resolution);
            lastBrightness = asciiArt.run();
            lastResolution = resolution;
        }

        output.out(lastBrightness);
    }

    /**
     * Changes output format between console and HTML.
     * @throws IncorrectFormatException if format specification is invalid
     */
    private void handelOutputCommand(String arg2) throws IncorrectFormatException {
        if (arg2 == null) {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_OUTPUT);
        }

        // Set appropriate output handler
        if (arg2.equals(CONSOLE)) {
            output = new ConsoleAsciiOutput();
        } else if (arg2.equals(HTML)) {
            output = new HtmlAsciiOutput(OUTPUT_HTML, FONT_NAME);
        } else {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_OUTPUT);
        }
    }

    /**
     * Sets the brightness rounding method.
     * @throws IncorrectFormatException if rounding method is invalid
     */
    private void handleRoundCommand(String arg2) throws IncorrectFormatException {
        if (arg2 == null) {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_ROUND);
        }

        // Validate and set rounding method
        if (arg2.equals(UP) || arg2.equals(DOWN) || arg2.equals("abs")) {
            charMatcher.setRoundingMethod(arg2);
        } else {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_ROUND);
        }
    }

    /**
     * Adjusts the resolution of the ASCII art output.
     * @throws UserInputException if resolution would exceed image boundaries
     * @throws IncorrectFormatException if resolution command is invalid
     */
    private void handleResCommand(String arg2) throws UserInputException, IncorrectFormatException {
        // Calculate resolution boundaries
        double maxCharsInRow = image.getWidth();
        double minCharsInRow = Math.max(1, image.getWidth() / image.getHeight());

        if (arg2 == null) {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_RESOLUTION);
        }

        // Adjust resolution within bounds
        if (arg2.equals(UP)) {
            if (resolution * 2 > maxCharsInRow) {
                throw new UserInputException(AsciiArtConstant.FORMAT_ERROR_RESOLUTION_BOUNDARIES);
            }
            resolution *= 2;
            System.out.println(AsciiArtConstant.CHANGE_RESOLUTION + resolution);
        } else if (arg2.equals(DOWN)) {
            if (resolution / 2 < minCharsInRow) {
                throw new UserInputException(AsciiArtConstant.FORMAT_ERROR_RESOLUTION_BOUNDARIES);
            }
            resolution /= 2;
            System.out.println(AsciiArtConstant.CHANGE_RESOLUTION + resolution);
        } else {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_RESOLUTION);
        }
    }

    /**
     * Adds characters to the ASCII art character set.
     * @throws IncorrectFormatException if character specification is invalid
     */
    private void handleAddCommand(String arg2) throws IncorrectFormatException {
        if (arg2 == null) {
            throw new IllegalArgumentException(AsciiArtConstant.FORMAT_ERROR_ADD);
        }

        // Handle different add formats
        if (arg2.length() == 1 && checkRange(arg2.charAt(0))) {
            charMatcher.addChar(arg2.charAt(0));
        } else if (arg2.equals(ALL)) {
            for (char c = 32; c <= 125; c++) {
                charMatcher.addChar(c);
            }
        } else if (arg2.equals(SPACE)) {
            charMatcher.addChar(' ');
        } else if (arg2.length() == LEGAL_RANGE_STRING && arg2.charAt(1) == '-') {
            addAsciiRange(arg2);
        } else {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_ADD);
        }
    }

    /**
     * Adds a range of ASCII characters to the character set.
     * @throws IncorrectFormatException if range specification is invalid
     */
    private void addAsciiRange(String arg2) {
        // Validate range endpoints
        if (!checkRange(arg2.charAt(0)) || !checkRange(arg2.charAt(2))) {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_ADD);
        }

        // Add characters in range (handles reversed ranges)
        char start = arg2.charAt(0);
        char end = arg2.charAt(2);
        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }

        for (char c = start; c <= end; c++) {
            charMatcher.addChar(c);
        }
    }

    /**
     * Checks if a character is within valid ASCII range (32-125).
     */
    private boolean checkRange(char c) {
        return c >= 32 && c <= 125;
    }

    /**
     * Removes characters from the ASCII art character set.
     * @throws IncorrectFormatException if character specification is invalid
     */
    private void handleRemoveCommand(String arg2) throws IncorrectFormatException {
        if (arg2 == null) {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_REMOVE);
        }

        // Handle different remove formats
        if (arg2.length() == 1 && checkRange(arg2.charAt(0))) {
            charMatcher.removeChar(arg2.charAt(0));
        } else if (arg2.equals(ALL)) {
            for (char c = 0; c <= 125; c++) {
                charMatcher.removeChar(c);
            }
        } else if (arg2.equals(SPACE)) {
            charMatcher.removeChar(' ');
        } else if (arg2.length() == LEGAL_RANGE_STRING && arg2.charAt(1) == '-') {
            removeAsciiRange(arg2);
        } else {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_REMOVE);
        }
    }

    /**
     * Removes a range of ASCII characters from the character set.
     * @throws IncorrectFormatException if range specification is invalid
     */
    private void removeAsciiRange(String arg2) {
        if (!checkRange(arg2.charAt(0)) || !checkRange(arg2.charAt(2))) {
            throw new IncorrectFormatException(AsciiArtConstant.FORMAT_ERROR_REMOVE);
        }

        // Remove characters in range (handles reversed ranges)
        char start = arg2.charAt(0);
        char end = arg2.charAt(2);
        if (start > end) {
            char temp = start;
            start = end;
            end = temp;
        }

        for (char c = start; c <= end; c++) {
            charMatcher.removeChar(c);
        }
    }

    /**
     * Displays the current character set.
     */
    private void printCurrentCharset() {
        Map<Character, Double> brightnessMap = charMatcher.getBrightnessMap();

        brightnessMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) // מיון לפי מפתח (char)
                .forEach(entry -> System.out.print(entry.getKey() + " "));

        System.out.println(); // ירידת שורה בסוף
    }


    /**
     * Reads and processes user input from command line.
     * @return Array containing command and argument
     */
    private String[] processUserInput() {
        System.out.print(">>> ");
        String userInput = KeyboardInput.readLine();

        // Split input into command and argument
        String[] cmdString = userInput.trim().split(" ");
        String arg1 = cmdString[0];
        String arg2 = cmdString.length > 1 ? cmdString[1] : "";

        return new String[]{arg1, arg2};
    }

    /**
     * Application entry point.
     * @throws IllegalArgumentException if no image filename provided
     */
    public static void main(String[] args) throws IllegalArgumentException {
        if (args.length != 1) {
            return;
        }

        // Initialize and run shell
        String imageName = args[0];
        Shell shell = new Shell();
        shell.run(imageName);
    }
}