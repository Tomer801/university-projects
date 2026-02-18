package bricker.gameobjects;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
/**
 * Represents a ball in the game. The ball can collide with other objects and
 * will bounce off them. It also plays a sound when it collides with an object.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter;
    /**
     * Constructor for the Ball class.
     *
     * @param topLeftCorner The top-left corner of the ball.
     * @param dimensions    The dimensions of the ball.
     * @param renderable    The renderable object for the ball.
     * @param collisionSound The sound to play on collision.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.collisionCounter = 0;
    }
    /**
     * Determines if the ball should collide with another game object.
     *
     * @param other The other game object to check for collision.
     * @return true if the ball should collide with the other object, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return super.shouldCollideWith(other) && !(other instanceof Heart);
    }

    /**
     * @return ball's collision counter
     */
     public int getCollisionCounter() {
        return collisionCounter;
    }
    /**
     * Called when the ball collides with another game object.
     *
     * @param other     The other game object that the ball collided with.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        Vector2 newVel = this.getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionCounter++;
        collisionSound.play();
    }


}