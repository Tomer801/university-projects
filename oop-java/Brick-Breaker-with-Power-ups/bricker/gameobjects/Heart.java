package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Represents a visual heart object in the game that indicates one player life.
 * Each heart can be marked as "lost" to show when the player loses a life.
 * Used by the lives counter system to display remaining lives to the player.
 */
public class Heart extends GameObject {
    /** Tracks whether this heart has been lost (used up) */
    private boolean lostLife;

    /**
     * Creates a new Heart game object to represent one player life.
     *
     * @param topLeftCorner Position of the heart's top-left corner in the game window
     * @param dimensions Width and height of the heart sprite
     * @param renderable Visual representation (image/sprite) of the heart
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.lostLife = false;
    }

    /**
     * Marks this heart as lost, indicating the player has lost one life.
     */
    public void lostLife() {
        this.lostLife = true;
    }

    /**
     * Checks if this heart represents a lost life.
     *
     * @return true if this heart has been marked as lost, false if it's still active
     */
    public boolean getLostLife() {
        return lostLife;
    }
}