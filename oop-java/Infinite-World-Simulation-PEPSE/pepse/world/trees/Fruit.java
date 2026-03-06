package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.NameConstant;

import java.awt.*;
import java.util.function.Consumer;

/**
 * Represents a collectible fruit object in the game world.
 * When collected by the avatar, the fruit temporarily disappears and reappears after a delay.
 */
public class Fruit extends GameObject {
    /** Standard size for fruit objects */
    private static final int FRUIT_SIZE = 15;

    /** Stores the fruit's visual representation for reuse after respawning */
    private final Renderable renderable;

    /**
     * Creates a new fruit object at the specified position.
     *
     * @param position The position where the fruit should appear
     * @param size The dimensions of the fruit
     * @param renderable The visual representation of the fruit
     */
    public Fruit(Vector2 position, Vector2 size, Renderable renderable) {
        super(position, size, renderable);
        this.setTag(NameConstant.FRUIT);
        this.renderable = renderable;
    }

    /**
     * Handles collision events with other game objects.
     * When the avatar collects the fruit:
     * 1. The fruit becomes invisible
     * 2. Its collision tag is disabled
     * 3. It reappears after 30 seconds
     *
     * @param other The object colliding with this fruit
     * @param collision Information about the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (NameConstant.AVATAR.equals(other.getTag()) && NameConstant.FRUIT.equals(this.getTag())) {
            // Hide immediately, then reappear after 30 seconds
            this.renderer().setRenderable(null);
            this.setTag("null");

            new ScheduledTask(
                    this,
                    30f,
                    false,
                    () -> {
                        this.renderer().setRenderable(renderable);
                        this.setTag(NameConstant.FRUIT);
                    }
            );
        }
    }

}
