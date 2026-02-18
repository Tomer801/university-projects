package bricker.brick_strategies;

import danogl.GameObject;

/**
 * A collision strategy that combines two other strategies.
 * When a collision occurs, both strategies will be triggered in order.
 */
public class DoubleStrategy implements CollisionStrategy {
    private final CollisionStrategy first;
    private final CollisionStrategy second;

    /**
     * Constructs a new DoubleStrategy with two sub-strategies.
     *
     * @param first  The first strategy to execute.
     * @param second The second strategy to execute.
     */
    public DoubleStrategy(CollisionStrategy first, CollisionStrategy second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Calls both sub-strategies' onCollision methods.
     *
     * @param thisObj  The GameObject (usually a brick) that was collided with.
     * @param otherObj The GameObject that collided with it (usually the ball).
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        first.onCollision(thisObj, otherObj);
        second.onCollision(thisObj, otherObj);
    }
}
