package pepse.world.daynight;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the halo effect around the sun in the game world.
 * Creates and manages a translucent circular glow that surrounds the sun object.
 */
public class SunHalo {
    /**
     * A semi-transparent yellow color used for the sun's halo effect.
     */
    private final static Color TRANSPARENT_YELLOW = new Color(255, 255, 0, 50);

    /**
     * The ratio determining how much larger the halo is compared to the sun.
     */
    private final static float SUN_HALO_RATIO = 2f;

    /**
     * Creates a halo effect GameObject that surrounds the sun.
     *
     * @param sun The sun GameObject that this halo will surround
     * @return A GameObject representing the sun's halo
     */
    public static GameObject create(GameObject sun) {
        GameObject sunHalo = new GameObject(Vector2.ZERO,
                sun.getDimensions().mult(SUN_HALO_RATIO),
                new OvalRenderable(TRANSPARENT_YELLOW));
        sunHalo.setTag("sun-halo");
        sunHalo.setCoordinateSpace(sun.getCoordinateSpace());
        return sunHalo;
    }
}
