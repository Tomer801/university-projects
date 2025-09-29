package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.Component;
import danogl.gui.rendering.Renderable;
import bricker.gameobjects.Ball;

/**
 * TurboStrategy is a collision strategy that temporarily increases the speed of the ball
 * and changes its appearance when a brick is hit. The turbo mode lasts for a fixed number
 * of collisions before reverting to the normal state.
 */
public class TurboStrategy implements bricker.brick_strategies.CollisionStrategy {
    private final GameObjectCollection gameObjects; // Collection of all game objects in the game.
    private final Ball mainBall; // The main ball object affected by the turbo mode.
    private final Renderable redBallImage; // The image used to represent the ball in turbo mode.
    private final Renderable normalBallImage; // The image used to represent the ball in normal mode.

    private static final float TURBO_MULTIPLIER = 1.4f; // Multiplier for the ball's speed in turbo mode.
    private static final int TURBO_HITS = 6; // Number of collisions before turbo mode deactivates.

    private static boolean turboActive = false; // Indicates whether turbo mode is currently active.
    private static Component activeComponent = null; // The component responsible for monitoring turbo mode.

    /**
     * Constructor for TurboStrategy.
     *
     * @param gameObjects The collection of game objects in the game.
     * @param mainBall The main ball object affected by the turbo mode.
     * @param redBallImage The image used to represent the ball in turbo mode.
     * @param normalBallImage The image used to represent the ball in normal mode.
     */
    public TurboStrategy(GameObjectCollection gameObjects,
                         Ball mainBall,
                         Renderable redBallImage,
                         Renderable normalBallImage) {
        this.gameObjects = gameObjects;
        this.mainBall = mainBall;
        this.redBallImage = redBallImage;
        this.normalBallImage = normalBallImage;
    }

    /**
     * Handles the collision event between a brick and another game object.
     * If the other object is a ball, the brick is removed, and turbo mode is activated
     * if it is not already active.
     *
     * @param brick The brick involved in the collision.
     * @param other The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject brick, GameObject other) {
        if (!(other instanceof Ball)) return;

        gameObjects.removeGameObject(brick);

        if (!turboActive) {
            activateTurboMode();
        }
    }

    /**
     * Activates turbo mode by increasing the ball's speed, changing its appearance,
     * and adding a component to monitor when turbo mode should deactivate.
     */
    private void activateTurboMode() {
        turboActive = true;
        int targetCollisionCount = mainBall.getCollisionCounter() + TURBO_HITS;

        mainBall.setVelocity(mainBall.getVelocity().mult(TURBO_MULTIPLIER));
        mainBall.renderer().setRenderable(redBallImage);

        activeComponent = new Component() {
            @Override
            public void update(float deltaTime) {
                if (mainBall.getCollisionCounter() >= targetCollisionCount) {
                    deactivateTurboMode();
                    mainBall.removeComponent(activeComponent);
                    activeComponent = null;
                }
            }
        };

        mainBall.addComponent(activeComponent);
    }

    /**
     * Deactivates turbo mode by resetting the ball's speed and appearance to their normal state.
     */
    private void deactivateTurboMode() {
        turboActive = false;
        mainBall.setVelocity(mainBall.getVelocity().mult(1 / TURBO_MULTIPLIER));
        mainBall.renderer().setRenderable(normalBallImage);
    }
}