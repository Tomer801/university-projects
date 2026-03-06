package image_char_matching;

import java.util.Map;

/**
 * Interface for rounding methods used in character matching.
 * This interface defines a method for rounding a brightness value
 * to the nearest character based on a provided mapping.
 * @author Tomer Kadosh
 */
public interface RoundingMethod {
    /**
     * Rounds a given brightness value to the nearest character based on the provided mapping.
     * @param value The brightness value to round
     * @param char_map The mapping of characters to their corresponding brightness values
     * @return The character that best matches the given brightness value
     */
    char round(double value, Map<Character, Double> char_map);
}