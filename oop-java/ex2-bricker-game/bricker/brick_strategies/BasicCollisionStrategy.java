
package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import bricker.gameobjects.Ball;
/**
 * Implements the basic collision behavior for bricks in the game.
 * This strategy simply removes the brick when it is hit by a ball.
 * Serves as the base strategy that other more complex strategies can build upon.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    /** Collection of all game objects to enable brick removal */
    private final GameObjectCollection collection;

    /**
     * Creates a new BasicCollisionStrategy instance.
     *
     * @param collection The collection of game objects to manage brick removal
     */
    public BasicCollisionStrategy(GameObjectCollection collection) {
        this.collection = collection;
    }

    /**
     * Handles collision between a brick and another game object.
     * If the collision is with a ball, removes the brick from the game.
     *
     * @param object_1 The brick that was hit
     * @param object_2 The object that collided with the brick
     */
    @Override
    public void onCollision(GameObject object_1, GameObject object_2) {
        if (object_2 instanceof Ball) {
            System.out.println("collision with brick detected");
            collection.removeGameObject(object_1);
        }
    }
}