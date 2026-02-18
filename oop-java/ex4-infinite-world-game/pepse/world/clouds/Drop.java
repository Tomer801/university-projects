package pepse.world.clouds;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.NameConstant;

import java.awt.*;

/**
 * Represents a rain drop in the game world.
 * A drop falls at a constant velocity and gradually fades out over its lifetime.
 */
public class Drop extends GameObject {
    /** Vertical falling speed in pixels per second */
    private static final float VELOCITY_Y = 100f;

    /** Color of the rain drop with full opacity */
    private static final Color TRANSPARENT_BLUE = new Color(18, 58, 255, 255);

    /** Visual representation of the rain drop */
    private static final Renderable DROP_REBDERABLE = new RectangleRenderable(TRANSPARENT_BLUE);

    /** Size of the rain drop in pixels */
    private static final float DROP_SIZE = 10f;

    /** Duration in seconds before the drop completely fades out */
    private static final float LIFE_TIME = 5f;

    /**
     * Creates a new rain drop at the specified position.
     * The drop will fall downward and fade out over its lifetime.
     *
     * @param topLeftCorner Initial position of the rain drop
     */
    public Drop(Vector2 topLeftCorner) {
        super(topLeftCorner, Vector2.ONES.mult(DROP_SIZE), DROP_REBDERABLE);
        this.setVelocity(new Vector2(0, VELOCITY_Y));
        this.setTag(NameConstant.DROP);

        // Create fade-out transition
        new Transition<Float>(
                this,
                alpha -> renderer().setOpaqueness(alpha),
                1f,    // Start fully visible
                0f,    // Fade to completely transparent
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                LIFE_TIME,
                Transition.TransitionType.TRANSITION_ONCE,
                null   // Remove drop after transition completes
        );
    }
}
