package image_char_matching;

import java.util.Map;

/**
 * Implements a rounding strategy that finds the absolute closest character brightness value.
 * This class finds the character whose brightness value has the smallest absolute difference
 * from the target brightness value. If two characters have the same distance, it picks the
 * character with the higher ASCII value.
 * @author Tomer Kadosh
 */
public class AbsRound implements RoundingMethod {

    /**
     * Finds the character with the closest brightness value using absolute difference.
     *
     * @param value The target brightness value to match
     * @param char_map Map containing characters and their corresponding brightness values
     * @return The character with the closest brightness value by absolute difference
     */
    @Override
    public char round(double value, Map<Character, Double> char_map) {
        // Initialize tracking variables for closest match
        double closestBrightness = 0;
        char closestChar = 0;
        boolean firstLoop = true;

        // Iterate through each character and its brightness value
        for (Map.Entry<Character, Double> entry : char_map.entrySet()) {
            // Calculate absolute distance from target value to current brightness
            double distance = Math.abs(entry.getValue() - value);

            // For first iteration, set initial closest values
            if (firstLoop){
                closestBrightness = distance;
                closestChar = entry.getKey();
                firstLoop = false;
            }
            // Update closest match if current distance is smaller
            else if (distance < closestBrightness){
                closestBrightness = distance;
                closestChar = entry.getKey();
            }
            // If distances are equal, prefer the character with higher ASCII value
            else if (distance == closestBrightness && entry.getKey() > closestChar){
                closestChar = entry.getKey();
            }
        }
        return closestChar;
    }
}