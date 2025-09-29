package bricker.brick_strategies;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Heart;
import bricker.gameobjects.Lives;
import bricker.gameobjects.Paddle;

/**
 * A collision strategy that spawns a falling heart when a brick is destroyed.
 * If the player catches the heart with the paddle, they gain an extra life.
 * The heart disappears if it falls too far below the paddle.
 */
public class HeartStrategy implements CollisionStrategy {
    /** Collection of all game objects to manage heart spawning */
    private final GameObjectCollection gameObjects;
    /** Visual representation of the heart pickup */
    private final Renderable heartImage;
    /** Size of the heart pickup */
    private final Vector2 heartDimensions;
    /** Reference to the lives counter to add lives when hearts are collected */
    private final Lives livesCounter;
    /** Reference to the main paddle for collision detection */
    private final Paddle mainPaddle;

    /**
     * Creates a new HeartStrategy instance.
     *
     * @param gameObjects Collection of all game objects
     * @param heartImage Visual representation for spawned hearts
     * @param heartDimensions Width and height of the heart pickup
     * @param livesCounter Lives counter to increment when hearts are collected
     * @param mainPaddle Main paddle for collision detection with hearts
     */
    public HeartStrategy(GameObjectCollection gameObjects,
                         Renderable heartImage,
                         Vector2 heartDimensions,
                         Lives livesCounter,
                         Paddle mainPaddle) {
        this.gameObjects = gameObjects;
        this.heartImage = heartImage;
        this.heartDimensions = heartDimensions;
        this.livesCounter = livesCounter;
        this.mainPaddle = mainPaddle;
    }

    /**
     * Handles collision between a brick and another game object.
     * Removes the brick and spawns a falling heart at its position.
     *
     * @param brick The brick that was hit
     * @param other The object that collided with the brick
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        gameObjects.removeGameObject(brick);

        Vector2 heartPos = brick.getCenter();
        Heart fallingHeart = new Heart(heartPos, heartDimensions, heartImage);
        fallingHeart.setVelocity(Vector2.DOWN.mult(100));

        // Add update component to heart
        fallingHeart.addComponent(deltaTime -> {
            // 1. If heart falls too low (off screen)
            if (fallingHeart.getTopLeftCorner().y() > mainPaddle.getTopLeftCorner().y() + 200) {
                gameObjects.removeGameObject(fallingHeart);
                return;
            }

            // 2. If heart intersects with main paddle (manual AABB collision)
            if (intersects(fallingHeart, mainPaddle)) {
                livesCounter.addLife();
                gameObjects.removeGameObject(fallingHeart);
            }
        });

        gameObjects.addGameObject(fallingHeart);
    }

    /**
     * Checks if two game objects' bounding boxes intersect using AABB collision detection.
     *
     * @param a First game object to check
     * @param b Second game object to check
     * @return true if the objects' bounding boxes intersect, false otherwise
     */
    private boolean intersects(GameObject a, GameObject b) {
        Vector2 aPos = a.getTopLeftCorner();
        Vector2 aSize = a.getDimensions();
        Vector2 bPos = b.getTopLeftCorner();
        Vector2 bSize = b.getDimensions();

        return aPos.x() < bPos.x() + bSize.x() &&
                aPos.x() + aSize.x() > bPos.x() &&
                aPos.y() < bPos.y() + bSize.y() &&
                aPos.y() + aSize.y() > bPos.y();
    }
}