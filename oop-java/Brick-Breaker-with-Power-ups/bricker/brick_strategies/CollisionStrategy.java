package bricker.brick_strategies;

import danogl.GameObject;

/**
 * Defines the interface for different collision handling strategies in the game.
 * Each strategy implements a different behavior for what happens when a brick
 * is hit by another game object (typically a ball).
 * Strategy implementations include:
 * - Basic removal of the brick (BasicCollisionStrategy)
 * - Spawning pucks on collision (AddPuckStrategy)
 * - Temporarily speeding up the ball (TurboStrategy)
 */
public interface CollisionStrategy {
   /**
    * Handles a collision between two game objects, typically a brick and a ball.
    * The specific behavior depends on the implementing strategy.
    *
    * @param object_1 The first object in the collision (typically the brick)
    * @param object_2 The second object in the collision (typically the ball)
    */
   void onCollision(GameObject object_1, GameObject object_2);
}