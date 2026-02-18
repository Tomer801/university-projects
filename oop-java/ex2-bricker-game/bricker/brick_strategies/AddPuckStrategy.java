package bricker.brick_strategies;
import bricker.gameobjects.Ball;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Puck;
import danogl.components.Component;

/**
 * A collision strategy that spawns two pucks when a brick is hit by a ball.
 * The pucks are special types of balls that move in random upward directions.
 * Implements the CollisionStrategy interface to define brick collision behavior.
 */
public class AddPuckStrategy implements CollisionStrategy {
    /** Collection of all game objects to manage puck spawning and brick removal */
    private final GameObjectCollection gameObjects;
    /** Visual representation to use for spawned pucks */
    private final Renderable puckImage;
    /** Sound to play when pucks collide with objects */
    private final Sound collisionSound;
    private Vector2 windowDimensions;

    /**
     * Creates a new AddPuckStrategy instance.
     *
     * @param gameObjects      Collection of all game objects
     * @param puckImage        Visual representation for spawned pucks
     * @param collisionSound   Sound to play on puck collisions
     * @param windowsDimension Dimensions of the game window
     */
    public AddPuckStrategy(GameObjectCollection gameObjects,
                           Renderable puckImage,
                           Sound collisionSound, Vector2 windowsDimension) {
        this.gameObjects = gameObjects;
        this.puckImage = puckImage;
        this.collisionSound = collisionSound;
        this.windowDimensions = windowsDimension;
    }

    /**
     * Handles collision between a brick and another game object.
     * If the collision is with a ball, spawns two pucks at the brick's position
     * and removes the brick.
     *
     * @param brick The brick that was hit
     * @param other The object that collided with the brick
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        if (!(other instanceof Ball)) return;

        Vector2 center = brick.getCenter();
        Puck puck1 = new Puck(center, 20, puckImage, collisionSound);
        Puck puck2 = new Puck(center, 20, puckImage, collisionSound);
        puck1.setTag("Puck");
        puck2.setTag("Puck");
        gameObjects.addGameObject(puck1);
        gameObjects.addGameObject(puck2);
        gameObjects.removeGameObject(brick);
        // Create dummy object just to hold the component
        GameObject cleaner = new GameObject(Vector2.ZERO, Vector2.ZERO, null);
        gameObjects.addGameObject(cleaner);

        Puck[] pucks = { puck1, puck2 };

        cleaner.addComponent(new Component() {
            @Override
            public void update(float deltaTime) {
                boolean allRemoved = true;
                for (int i = 0; i < pucks.length; i++) {
                    if (pucks[i] != null && pucks[i].getCenter().y() > windowDimensions.y()) {
                        gameObjects.removeGameObject(pucks[i]);
                        pucks[i] = null;
                    }
                    if (pucks[i] != null) {
                        allRemoved = false;
                    }
                }
                if (allRemoved) {
                    gameObjects.removeGameObject(cleaner); // Done, remove self
                }
            }
        });
    }

}