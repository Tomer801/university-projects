package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a leaf object in the game world.
 * Implements animated behavior including swaying and size pulsing effects.
 */
public class Leaf extends GameObject {
    /** The standard size for leaf objects */
    private static final float LEAF_SIZE = 15f;

    /** Random number generator for leaf animations */
    private Random rand;

    /**
     * Creates a new animated leaf object.
     *
     * @param topLeftCorner Initial position of the leaf
     * @param dimensions Size of the leaf
     * @param renderable Visual representation of the leaf
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.rand = new Random();
        this.setTag("leaf");
        leafMovment();
    }

    /**
     * Initializes the leaf's animation behavior.
     * Creates two animation effects:
     * 1. A swaying motion with random angles (-5 to +5 degrees)
     * 2. A pulsing size effect (80% to 100% of original size)
     * Both animations run continuously with random timing.
     */
    private void leafMovment() {
        float waitTime = 0.5f + (float) Math.random() * 1.5f;
        Vector2 originalSize = new Vector2(this.getDimensions());
        new ScheduledTask(
                this,
                waitTime,
                true,
                () -> {
                    float targetAngle = -5f + rand.nextFloat() * 10f;
                    new Transition<>(
                            this,
                            angle -> this.renderer().setRenderableAngle((Float) angle),
                            0f,
                            targetAngle,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            1.5f,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null
                    );
                }
        );

        new ScheduledTask(
                this,
                waitTime,
                true,
                () -> {
                    new Transition<>(
                            this,
                            scale -> this.setDimensions(originalSize.mult((Float) scale)),
                            1f,
                            0.8f,
                            Transition.LINEAR_INTERPOLATOR_FLOAT,
                            1.5f,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null
                    );
                }
        );
    }


}
