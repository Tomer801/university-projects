package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents the night cycle in the game.
 * Creates and manages a night overlay that transitions opacity to simulate day-night cycles.
 */
public class Night {
    /**
     * The color used for the night sky overlay.
     */
    public static final Color NIGHT_COLOR = Color.decode("#000033");

    /**
     * Maximum opacity value for midnight (darkest point of night).
     */
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates a night overlay object that gradually changes opacity to create a day-night cycle.
     *
     * @param windowDimensions The dimensions of the game window
     * @param cycleLength The length of one complete day-night cycle in seconds
     * @return A GameObject representing the night overlay
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,
                           windowDimensions,
                           new RectangleRenderable(NIGHT_COLOR));
        night.setTag("night");
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        Transition transition = new Transition<>(
                night,
                night.renderer()::setOpaqueness,
                0f,
                MIDNIGHT_OPACITY, // Transition to black at the end of the cycle
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null
        );

        // Additional properties or components can be added to the night object here
        return night;
    }
}