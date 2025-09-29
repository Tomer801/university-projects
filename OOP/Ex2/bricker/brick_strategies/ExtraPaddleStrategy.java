package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.ExtraPaddle;

/**
 * A collision strategy that spawns a temporary second paddle when a brick is hit.
 * Only one extra paddle can exist at a time. The extra paddle will disappear
 * after being hit by the ball four times.
 */
public class ExtraPaddleStrategy implements CollisionStrategy {

    /** Collection of all game objects to manage paddle spawning */
    private final GameObjectCollection gameObjects;
    /** Used to load the paddle's visual representation */
    private final ImageReader imageReader;
    /** Handles keyboard input for paddle movement */
    private final UserInputListener inputListener;
    /** Window dimensions for paddle positioning */
    private final Vector2 windowDimensions;

    /** Tracks whether an extra paddle currently exists in the game */
    private static boolean hasExtraPaddle = false;

    /**
     * Creates a new ExtraPaddleStrategy instance.
     *
     * @param gameObjects Collection of all game objects
     * @param imageReader For loading the paddle's image
     * @param inputListener For handling paddle movement input
     * @param windowDimensions Window size for paddle positioning
     */
    public ExtraPaddleStrategy(GameObjectCollection gameObjects,
                               ImageReader imageReader,
                               UserInputListener inputListener,
                               Vector2 windowDimensions) {
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
    }

    /**
     * Handles collision between a brick and another game object.
     * Removes the brick and attempts to spawn an extra paddle if one
     * doesn't already exist.
     *
     * @param brick The brick that was hit
     * @param other The object that collided with the brick
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        // Remove the brick itself
        gameObjects.removeGameObject(brick);

        // Only spawn if one is not already active
        if (!hasExtraPaddle) {
            Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);

            ExtraPaddle extra = new ExtraPaddle(
                    new Vector2(windowDimensions.x() / 2f - 50, windowDimensions.y() / 2f),
                    new Vector2(100, 20),
                    paddleImage,
                    inputListener,
                    windowDimensions.x(),
                    gameObjects,
                    () -> hasExtraPaddle = false   //  Reset flag when paddle removes itself
            );

            gameObjects.addGameObject(extra);
            hasExtraPaddle = true;
        }
    }
}