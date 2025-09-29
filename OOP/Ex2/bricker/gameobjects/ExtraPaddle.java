package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * The ExtraPaddle class is a subclass of Paddle that represents a temporary paddle
 * in the game. It keeps track of how many times it has been hit by the ball and
 * removes itself from the game after a certain number of hits.
 */
public class ExtraPaddle extends Paddle {
    /** The maximum number of hits the ExtraPaddle can take before it disappears */
    private static final int MAX_HITS = 4;
    /** Collection of all game objects to enable self-removal */
    private final GameObjectCollection gameObjects;
    /** Callback to notify the strategy when this paddle is removed */
    private final Runnable onRemove;
    /** Counter to track the number of ball collisions */
    private int hitCounter = 0;

    /**
     * Creates a new ExtraPaddle instance.
     *
     * @param topLeftCorner Initial position of the paddle's top-left corner
     * @param dimensions Width and height of the paddle
     * @param renderable Visual representation of the paddle
     * @param inputListener Listener for handling user input
     * @param windowWidth Width of the game window for boundary checking
     * @param gameObjects Collection of all game objects
     * @param onRemove Callback function to execute when paddle is removed
     */
    public ExtraPaddle(Vector2 topLeftCorner,
                       Vector2 dimensions,
                       Renderable renderable,
                       UserInputListener inputListener,
                       float windowWidth,
                       GameObjectCollection gameObjects,
                       Runnable onRemove) {

        super(topLeftCorner, dimensions, renderable, inputListener, windowWidth);
        this.gameObjects = gameObjects;
        this.onRemove = onRemove;
    }

    /**
     * Handles collision events with other game objects.
     * Increments hit counter when colliding with a ball and removes itself
     * after reaching maximum hits.
     *
     * @param other The object this paddle collided with
     * @param collision Information about the collision
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);

        /* Only count collisions with real Ball objects */
        if (other instanceof Ball) {
            hitCounter++;
            System.out.println("Extra-paddle hits: " + hitCounter);

            if (hitCounter >= MAX_HITS) {
                gameObjects.removeGameObject(this);
                if (onRemove != null) {
                    onRemove.run();
                }
            }
        }
    }
}