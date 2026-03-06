package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Represents a player-controlled paddle in the game that can move horizontally.
 * The paddle responds to left and right arrow key inputs and is constrained
 * to stay within the game window boundaries.
 */
public class Paddle extends GameObject {

    /** Movement speed of the paddle in pixels per second */
    private static final float MOVEMENT_SPEED = 300f;

    /** Listener for handling keyboard input */
    private final UserInputListener inputListener;
    /** Width of the game window for boundary checking */
    private final float windowWidth;

    /**
     * Creates a new paddle object that can be controlled by the player.
     *
     * @param topLeftCorner Initial position of the paddle's top-left corner
     * @param dimensions Width and height of the paddle
     * @param renderable Visual representation of the paddle
     * @param inputListener Listener for handling keyboard input
     * @param windowWidth Width of the game window for boundary checking
     */
    public Paddle(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener,
                  float windowWidth) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowWidth = windowWidth;
    }

    /**
     * Updates the paddle's position based on user input and ensures it stays within bounds.
     * Responds to left and right arrow keys for movement.
     *
     * @param deltaTime Time elapsed since the last update
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // Handle horizontal movement based on arrow key input
        Vector2 dir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT))  dir = dir.add(Vector2.LEFT);
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) dir = dir.add(Vector2.RIGHT);
        setVelocity(dir.mult(MOVEMENT_SPEED));

        // Ensure paddle stays within window boundaries
        float halfWidth = getDimensions().x() / 2f;
        float clampedX = Math.max(halfWidth,
                Math.min(getCenter().x(), windowWidth - halfWidth));

        if (clampedX != getCenter().x()) {
            setCenter(new Vector2(clampedX, getCenter().y()));
        }
    }
}