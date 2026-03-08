package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ImagePrepration;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

/**
 * Converts images to ASCII art by mapping brightness values to characters.
 * This class handles the core algorithm for converting an image into a 2D array
 * of characters that represent the image when displayed as ASCII art.
 * @author Tomer Kadosh
 */
public class AsciiArtAlgorithm {

    /** The source image to convert to ASCII art */
    private final Image image;
    /** Character set used for ASCII art conversion */
    private final char[] charset;
    /** Resolution setting for the output */
    private final int resolution;
    /** Handles matching of characters to brightness values */
    private final SubImgCharMatcher charMatcher;
    /** Final ASCII art output as 2D character array */
    private char[][] finalBrightness;

    /**
     * Creates a new ASCII art conversion algorithm instance.
     *
     * @param image Source image to convert
     * @param charset Characters to use in ASCII art output
     * @param resolution Output resolution setting
     */
    public AsciiArtAlgorithm(Image image, char[] charset, int resolution) {
        this.image = image;
        this.charset = charset;
        this.resolution = resolution;
        this.charMatcher = new SubImgCharMatcher(charset);
        this.finalBrightness = null;
    }

    /**
     * Executes the ASCII art conversion algorithm.
     * Processes the image by:
     * 1. Preparing the image and calculating brightness values
     * 2. Converting brightness values to characters
     * 3. Creating the final ASCII art character array
     *
     * @return 2D array of characters representing the ASCII art
     */
    public char[][] run() {
        // Prepare image and calculate brightness values
        ImagePrepration imagePrepration = new ImagePrepration(image, resolution);
        double[][] givenBrightness = imagePrepration.calculateBrightnessForALL(image, resolution);

        // Initialize output array
        finalBrightness = new char[givenBrightness.length][givenBrightness[0].length];

        // Convert brightness values to characters
        for (int x = 0; x < givenBrightness.length; x++) {
            for (int y = 0; y < givenBrightness[0].length; y++) {
                char bri = charMatcher.getCharByImageBrightness(givenBrightness[x][y]);
                finalBrightness[x][y] = bri;
            }
        }

        return finalBrightness;
    }
}