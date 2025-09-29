package image_char_matching;
import java.util.Map;
import java.util.HashMap;

/**
 * Matches characters to image brightness values for ASCII art generation.
 * This class handles the mapping between ASCII characters and their corresponding brightness values,
 * maintaining both raw and normalized brightness mappings.
 * @author Tomer Kadosh
 */
public class SubImgCharMatcher {
    /** Map storing normalized brightness values for each character */
    private final Map<Character, Double> brightnessMap;
    /** Map storing raw (non-normalized) brightness values for each character */
    private final Map<Character, Double> rawBrightnessMap;

    /** Minimum brightness value in the current character set */
    private double minBrightness = Double.MAX_VALUE;
    /** Maximum brightness value in the current character set */
    private double maxBrightness = Double.MIN_VALUE;
    /** Method used for rounding brightness values to match characters */
    private RoundingMethod roundingMethod = new AbsRound();

    /**
     * Constructs a new SubImgCharMatcher with the given character set.
     * Initializes brightness maps and calculates brightness values for all characters.
     * @param charset Array of characters to use for ASCII art
     */
    public SubImgCharMatcher(char[] charset){
        this.brightnessMap = new HashMap<>();
        this.rawBrightnessMap = new HashMap<>();
        for (char c : charset) {
            brightnessMap.put(c, null);
            rawBrightnessMap.put(c, null);
        }
        setBrightnessMap();
    }

    /**
     * Returns the character that best matches the given brightness value.
     * @param brightness The brightness value to match
     * @return The character with the closest brightness value
     */
    public char getCharByImageBrightness(double brightness){
        return roundingMethod.round(brightness, brightnessMap);
    }

    /**
     * Adds a new character to the brightness maps.
     * Recalculates normalization if the character's brightness extends the current range.
     * @param c The character to add
     */
    public void addChar(char c){
        if(brightnessMap.containsKey(c)) {
            return;
        }

        double rawBrightness = setBrightnessForChar(c);
        rawBrightnessMap.put(c, rawBrightness);
        double normalized = normalizeBrightness(rawBrightness);
        brightnessMap.put(c, normalized);

        if (rawBrightness <= maxBrightness && rawBrightness >= minBrightness) {
            return;
        }
        resetMap();
    }

    /**
     * Removes a character from the brightness maps and recalculates normalization.
     * @param c The character to remove
     */
    public void removeChar(char c){
        if(!brightnessMap.containsKey(c)) {
            return;
        }
        brightnessMap.remove(c);
        rawBrightnessMap.remove(c);

        resetMap();
    }

    /**
     * Resets the brightness maps by recalculating min/max values and renormalizing all values.
     */
    private void resetMap() {
        minBrightness = Double.MAX_VALUE;
        maxBrightness = Double.MIN_VALUE;

        for (double raw : rawBrightnessMap.values()) {
            if (raw < minBrightness) minBrightness = raw;
            if (raw > maxBrightness) maxBrightness = raw;
        }

        for (Map.Entry<Character, Double> entry : brightnessMap.entrySet()) {
            char key = entry.getKey();
            double raw = rawBrightnessMap.get(key);
            entry.setValue(normalizeBrightness(raw));
        }
    }

    /**
     * Calculates the brightness value for a given character.
     * @param ascciChar The character to calculate brightness for
     * @return The brightness value as a ratio of white pixels to total pixels
     */
    private double setBrightnessForChar(char ascciChar){
        boolean[][] boolArray = CharConverter.convertToBoolArray(ascciChar);
        int arraySize = boolArray.length*boolArray[0].length;
        int whiteCell = 0;
        for (int i = 0; i < boolArray.length; i++) {
            for (int j = 0; j < boolArray[i].length; j++) {
                if (boolArray[i][j]) {
                    whiteCell++;
                }
            }
        }
        return (double) whiteCell /(double) arraySize;
    }

    /**
     * Initializes the brightness maps with values for all characters.
     */
    private void setBrightnessMap() {
        for (Map.Entry<Character, Double> entry : brightnessMap.entrySet()) {
            char asciiChar = entry.getKey();
            double raw = setBrightnessForChar(asciiChar);
            rawBrightnessMap.put(asciiChar, raw);
            entry.setValue(raw);
            if (raw > maxBrightness) maxBrightness = raw;
            if (raw < minBrightness) minBrightness = raw;
        }

        for (Map.Entry<Character, Double> entry : brightnessMap.entrySet()) {
            double raw = rawBrightnessMap.get(entry.getKey());
            entry.setValue(normalizeBrightness(raw));
        }
    }

    /**
     * Normalizes a brightness value to the range [0,1].
     * @param brightness The raw brightness value to normalize
     * @return The normalized brightness value
     */
    private double normalizeBrightness(double brightness){
        return (brightness - minBrightness) / (maxBrightness - minBrightness);
    }

    /**
     * Converts a normalized brightness value back to its raw value.
     * @param brightness The normalized brightness value
     * @return The raw brightness value
     */
    private double deNormalizeBrightness(double brightness){
        return brightness*(maxBrightness-minBrightness) + minBrightness;
    }

    /**
     * Sets the rounding method used for character matching.
     * @param roundingMethod The rounding method to use ("abs", "up", or "down")
     */
    public void setRoundingMethod(String roundingMethod) {
        if (roundingMethod.equals("abs")) {
            this.roundingMethod = new AbsRound();
        } else if (roundingMethod.equals("up")) {
            this.roundingMethod = new UpRound();
        } else if (roundingMethod.equals("down")) {
            this.roundingMethod = new DownRound();
        }
    }

    /**
     * Gets the map of normalized brightness values.
     * @return The brightness map
     */
    public Map<Character, Double> getBrightnessMap() {
        return brightnessMap;
    }
}