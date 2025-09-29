package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a brick in the game. The brick can have different collision strategies
 * that define its behavior when it collides with other objects.
 */
public class Brick extends GameObject {
    CollisionStrategy collisionStrategy;
    private int collisionCounter;
    /**
     * Constructor for the Brick class.
     *
     * @param topLeftCorner The top-left corner of the brick.
     * @param dimensions    The dimensions of the brick.
     * @param renderable    The renderable object for the brick.
     * @param collisionStrategy The collision strategy to use for this brick.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy)  {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.collisionCounter = 0;

    }
    /**
     * Handles the collision event with another game object.
     * @param other The other game object involved in the collision.
     * @param collision The collision details.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        collisionStrategy.onCollision(this, other);
        collisionCounter++;
    }
}
