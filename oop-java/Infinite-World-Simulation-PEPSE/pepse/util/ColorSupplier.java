package pepse.util;

import java.awt.*;
import java.util.Random;

/**
 * Provides procedurally-generated colors around a base color value.
 * Used to create natural-looking variations in terrain, clouds, and other game elements.
 *
 * @author Dan Nirel
 */
public final class ColorSupplier {
    /** Maximum color variation from base color when not specified */
    private static final int DEFAULT_COLOR_DELTA = 10;

    /** Random number generator for color variations */
    private final static Random random = new Random();

    /**
     * Returns a color similar to baseColor, with a default delta.
     * Each RGB channel is varied independently.
     *
     * @param baseColor A color that we wish to approximate
     * @return A color with random variations within DEFAULT_COLOR_DELTA
     */
    public static Color approximateColor(Color baseColor) {
        return approximateColor(baseColor, DEFAULT_COLOR_DELTA);
    }

    /**
     * Returns a color similar to baseColor, where all RGB channels
     * are varied by the same amount for a consistent tone.
     *
     * @param baseColor A color that we wish to approximate
     * @param colorDelta Maximum variation allowed per channel
     * @return A monochromatic variation of the base color
     */
    public static Color approximateMonoColor(Color baseColor, int colorDelta) {
        int channel = randomChannelInRange(
            baseColor.getRed() - colorDelta,
            baseColor.getRed() + colorDelta
        );
        return new Color(channel, channel, channel);
    }

    /**
     * Returns a monochromatic color variation using the default delta.
     * Useful for creating subtle grayscale variations.
     *
     * @param baseColor A color that we wish to approximate
     * @return A monochromatic variation of the base color
     */
    public static Color approximateMonoColor(Color baseColor) {
        return approximateMonoColor(baseColor, DEFAULT_COLOR_DELTA);
    }

    /**
     * Returns a color similar to baseColor with specified maximum variation.
     * Each RGB channel is varied independently within the delta range.
     *
     * @param baseColor A color that we wish to approximate
     * @param colorDelta Maximum variation allowed per channel
     * @return A color with random variations within colorDelta
     */
    public static Color approximateColor(Color baseColor, int colorDelta) {
        return new Color(
                randomChannelInRange(baseColor.getRed()-colorDelta, baseColor.getRed()+colorDelta),
                randomChannelInRange(baseColor.getGreen()-colorDelta, baseColor.getGreen()+colorDelta),
                randomChannelInRange(baseColor.getBlue()-colorDelta, baseColor.getBlue()+colorDelta));
    }

    /**
     * Generates a random color channel value within specified bounds.
     * Ensures the value stays within valid color channel range (0-255).
     *
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @return A random value between min and max, clamped to 0-255
     */
    private static int randomChannelInRange(int min, int max) {
        min = Math.max(0, min);
        max = Math.min(255, max);
        return random.nextInt(max-min+1) + min;
    }
}
