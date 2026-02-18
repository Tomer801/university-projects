package image_char_matching;

/**
 * Implements a rounding strategy that rounds up to the nearest character brightness value.
 * This class finds the closest character whose brightness value is greater than or equal to
 * the target brightness value.
 * @author Tomer Kadosh
 */
public class UpRound implements RoundingMethod {

    /**
     * Rounds up to find the closest character with a brightness
     * value greater than or equal to the target.
     *
     * @param value The target brightness value to match
     * @param char_map Map containing characters and their corresponding brightness values
     * @return The character with the closest brightness value that is >= target value
     */
    @Override
    public char round(double value, java.util.Map<Character, Double> char_map) {
        // Initialize tracking variables for closest match
        double closestBrightness = 0;
        char closestChar = 0;
        boolean firstLoop = true;

        // Iterate through each character and its brightness value
        for (java.util.Map.Entry<Character, Double> entry : char_map.entrySet()) {
            // Calculate distance from target value to current brightness
            double distance = entry.getValue() - value;

            // For first iteration, set initial closest values
            if (firstLoop){
                closestBrightness = distance;
                closestChar = entry.getKey();
                firstLoop = false;
            }
            // Update closest match if current distance is smaller but still non-negative
            else if (distance < closestBrightness && distance >= 0){
                closestBrightness = distance;
                closestChar = entry.getKey();
            }
        }
        return closestChar;
    }
}