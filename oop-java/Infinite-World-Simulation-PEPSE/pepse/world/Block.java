package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a single block in the game world.
 * Blocks are immovable objects with a fixed size.
 */
public class Block extends GameObject {
    /**
     * The size (width and height) of the block in pixels.
     */
    public static final int SIZE = 30;

    /**
     * Constructs a new Block at the specified top-left corner with the given renderable.
     * The block is immovable and prevents intersections from all directions.
     *
     * @param topLeftCorner The position of the top-left corner of the block.
     * @param renderable The renderable representing the block's appearance.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}
