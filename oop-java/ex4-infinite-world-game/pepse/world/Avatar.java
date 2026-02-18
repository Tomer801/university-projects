package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.NameConstant;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the player character in the game world.
 * Handles movement, animations, energy management and collisions.
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -500;
    private static final float GRAVITY = 600;
    /** Maximum energy level the avatar can have */
    private static final float MAX_ENERGY = 100f;
    private static final Color AVATAR_COLOR = Color.DARK_GRAY;
    private static final float AVATAR_SIZE = 30f;
    /** Energy cost for movement actions */
    public static final float ENERGY_MOVMENT_LOSS = 0.5f;
    /** Energy cost for jumping */
    public static final float ENERGY_JUMP_LOSS = 10f;


    private final List<Runnable> jumpBehaviors = new ArrayList<>();
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private AnimationRenderable idleAnimation;
    private AnimationRenderable runAnimation;
    private AnimationRenderable jumpAnimation;
    private float energy;
    private Renderable renderable;

    /**
     * Creates a new Avatar instance at the specified position.
     *
     * @param topLeftCorner Initial position of the avatar
     * @param inputListener Listener for user input
     * @param imageReader Reader for loading avatar animations
     */
    public Avatar(Vector2 topLeftCorner,
                  UserInputListener inputListener,
                  ImageReader imageReader) {
        super(topLeftCorner,
              Vector2.ONES.mult(AVATAR_SIZE), // Assuming avatar size is 30x30
              null); // Renderable will be set later);
        this.inputListener = inputListener;
        this.renderable = imageReader.readImage("assets/idle_0.png", true);
        this.imageReader = imageReader;
        this.energy = MAX_ENERGY; // Initialize energy to maximum
        // Initial energy value

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        renderer().setRenderable(renderable);
        setIdleAnimation();
        setRunAnimation();
        setJumpAnimation();
    }

    /**
     * Sets up the idle animation sequence for the avatar
     */
    private void setIdleAnimation() {
        idleAnimation = new AnimationRenderable(
                new Renderable[] {
                    imageReader.readImage("assets/idle_0.png", true),
                    imageReader.readImage("assets/idle_1.png", true),
                    imageReader.readImage("assets/idle_2.png", true),
                    imageReader.readImage("assets/idle_3.png", true)
                },
                0.2f
        );

    }

    /**
     * Sets up the running animation sequence for the avatar
     */
    private void setRunAnimation() {
        runAnimation = new AnimationRenderable(
                new Renderable[] {
                    imageReader.readImage("assets/run_0.png", true),
                    imageReader.readImage("assets/run_1.png", true),
                    imageReader.readImage("assets/run_2.png", true),
                    imageReader.readImage("assets/run_3.png", true),
                    imageReader.readImage("assets/run_4.png", true),
                    imageReader.readImage("assets/run_5.png", true)
                },
                0.1f
        );
    }

    /**
     * Sets up the jumping animation sequence for the avatar
     */
    private  void  setJumpAnimation() {
        jumpAnimation = new AnimationRenderable(
                new Renderable[] {
                    imageReader.readImage("assets/jump_0.png", true),
                    imageReader.readImage("assets/jump_1.png", true),
                    imageReader.readImage("assets/jump_2.png", true),
                    imageReader.readImage("assets/jump_3.png", true)
                },
                0.15f
        );
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(NameConstant.FRUIT)) {
            // Reset energy when landing on the ground or tree
            setEnergy(ENERGY_JUMP_LOSS);
        }
        if(other.getTag().equals(NameConstant.TERRAIN_TOP)){
            this.transform().setVelocityY(0);
        }

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        if (energy >= ENERGY_MOVMENT_LOSS) {
            if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
                xVel -= VELOCITY_X;
                renderer().setRenderable(runAnimation);
                renderer().setIsFlippedHorizontally(true);
            }
            if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
                xVel += VELOCITY_X;
                renderer().setRenderable(runAnimation);
                renderer().setIsFlippedHorizontally(false);
            }
            if (xVel != 0) {
                setEnergy(-ENERGY_MOVMENT_LOSS); // Decrease energy when moving
            }
        }

        transform().setVelocityX(xVel);
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0 && energy > 10) {
            transform().setVelocityY(VELOCITY_Y);
            setEnergy(-ENERGY_JUMP_LOSS);
            renderer().setRenderable(jumpAnimation);
            for (Runnable behavior : jumpBehaviors) {
                behavior.run();
            }
        }

        if (!inputListener.isKeyPressed(KeyEvent.VK_LEFT) &&
                !inputListener.isKeyPressed(KeyEvent.VK_RIGHT) &&
                !inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                getVelocity().y() == 0) {
            if (energy < MAX_ENERGY) {
                energy++; // Increase energy when not moving
            }
            renderer().setRenderable(idleAnimation);
        }
    }

    /**
     * Adds a behavior to be executed when the avatar jumps
     *
     * @param behavior The behavior to execute on jump
     */
    public void addJumpBehavior(Runnable behavior) {
        jumpBehaviors.add(behavior);
    }

    /**
     * Gets the current energy level of the avatar
     *
     * @return Current energy value
     */
    public float getEnergy() {
        return energy;
    }

    /**
     * Modifies the avatar's current energy level
     *
     * @param energyDelta The amount to change energy by (positive or negative)
     */
    public void setEnergy(float energyDelta) {
        if (energyDelta + this.energy > MAX_ENERGY) {
            this.energy = MAX_ENERGY;
        } else if (energyDelta + this.energy < 0) {
            this.energy = 0;
        } else {
            this.energy += energyDelta;
        }
    }
}
