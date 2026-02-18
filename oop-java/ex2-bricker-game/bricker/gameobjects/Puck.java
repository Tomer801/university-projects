package bricker.gameobjects;

import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Represents a special type of ball in the game that behaves like a hockey puck.
 * The puck is smaller than a regular ball and moves in random upward directions.
 * Inherits collision behavior from the Ball class.
 */
public class Puck extends Ball {

    /** Size ratio compared to regular ball diameter */
    public static final float SIZE_RATIO = 0.75f;
    /** Movement speed of the puck in pixels per second */
    public static final float SPEED = 200f;

    /**
     * Creates a new puck object with a random upward velocity.
     *
     * @param center Initial center position of the puck
     * @param diameter Base diameter before applying size ratio
     * @param renderable Visual representation of the puck
     * @param collisionSound Sound to play on collision
     */
    public Puck(Vector2 center,
                float diameter,
                Renderable renderable,
                Sound collisionSound) {

        /* call Ball-ctor so the puck is treated as a Ball */
        super(center,
                new Vector2(diameter * SIZE_RATIO, diameter * SIZE_RATIO),
                renderable,
                collisionSound);

        setInitialVelocity();
    }

    /**
     * Sets a random initial velocity for the puck.
     * The velocity will always be in an upward direction within a 180-degree arc.
     */
    private void setInitialVelocity() {
        Random rnd = new Random();
        double angle = rnd.nextDouble() * Math.PI;          // 0 .. π  (upper half-circle)

        float vx = (float) Math.cos(angle) * SPEED;
        float vy = (float) Math.sin(angle) * SPEED * -1;    // negative => upward

        setVelocity(new Vector2(vx, vy));
    }
}