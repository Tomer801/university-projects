package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.world.Terrain;

import java.awt.*;

/**
 * Represents the sun in the game world.
 * Creates and manages a sun object that moves in a circular path to simulate day-night cycles.
 */
public class Sun {
    /**
     * The radius of the sun object in pixels.
     */
    public static final int SUN_RADIUS = 50;

    /**
     * The color used to render the sun.
     */
    public static final Color SUN_COLOR = Color.YELLOW;
    /**
     * The horizontal offset for the initial sun position from the center.
     */
    public static final float ANGLE = 200;
    /**
     * The ratio of the ground height to the total window height.
     * This is used to determine where the sun starts in relation to the ground.
     */
    private static final float GROUND_SKY_RATIO = 0.66f;

    /**
     * Creates a sun object that moves in a circular path across the sky.
     *
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength The length of one complete day-night cycle in seconds
     * @return A GameObject representing the sun
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        float groundHeight = windowDimensions.y() *GROUND_SKY_RATIO;
        Vector2 cycleCenter = new Vector2(
                windowDimensions.x() / 2, // Center horizontally
                groundHeight // Start at the bottom of the screen
        );
        Vector2 initialSunCenter = new Vector2(
                windowDimensions.x() / 2f + ANGLE,  // Right of center
                groundHeight                      // At ground height
        );
        GameObject sun = new GameObject(initialSunCenter,
                new Vector2(SUN_RADIUS, SUN_RADIUS),
                new OvalRenderable(SUN_COLOR));
        sun.setTag("sun");

        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Transition transition = new Transition<>(
                sun,
                (Float angle) -> sun.setCenter
                        (initialSunCenter.subtract(cycleCenter)
                                .rotated(angle)
                                .add(cycleCenter)),
                0f, // Start with the sun at the horizon
                360f, // Complete a full circle at the end of the cycle
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );
        return sun;
    }
}