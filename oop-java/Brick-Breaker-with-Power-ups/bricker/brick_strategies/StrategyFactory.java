package bricker.brick_strategies;

import bricker.gameobjects.Lives;
import bricker.gameobjects.Paddle;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Ball;
import java.util.Random;

/**
 * A factory class that creates different collision strategies for bricks.
 * The factory can create basic strategies, special strategies (puck, extra paddle,
 * turbo, heart), and combinations of strategies. It uses a weighted random system
 * to determine which strategy to create.
 */
public class StrategyFactory {
    /** Maximum nesting depth for combined strategies */
    private static final int MAX_DEPTH = 2; // Depth 0 = 1 strategy, Depth 1 = 2, Depth 2 = 3

    /** Collection of all game objects for strategy management */
    private final GameObjectCollection gameObjects;
    /** Used to load images for visual elements */
    private final ImageReader imageReader;
    /** Used to load sound effects */
    private final SoundReader soundReader;
    /** Handles user input for interactive elements */
    private final UserInputListener inputListener;
    /** Window dimensions for positioning elements */
    private final Vector2 windowDimensions;
    /** Reference to the main game ball */
    private final Ball mainBall;
    /** Visual for the ball in turbo mode */
    private final Renderable redBallImage;
    /** Visual for the ball in normal mode */
    private final Renderable normalBallImage;
    /** Visual for spawned pucks */
    private final Renderable puckImage;
    /** Visual for heart pickups */
    private final Renderable heartImage;
    /** Sound effect for collisions */
    private final Sound collisionSound;
    /** Random number generator for strategy selection */
    private final Random random;
    /** Size of heart pickups */
    private final Vector2 heartDimensions;
    /** Reference to the lives counter */
    private final Lives livesCounter;
    /** Reference to the main paddle */
    private final Paddle mainPaddle;
    private TurboStrategy turboStrategy ;

    /**
     * Creates a new StrategyFactory with all necessary game components.
     *
     * @param gameObjects Collection of all game objects
     * @param imageReader For loading images
     * @param soundReader For loading sounds
     * @param inputListener For handling user input
     * @param windowDimensions Window size info
     * @param mainBall Reference to main ball
     * @param redBallImage Turbo ball visual
     * @param normalBallImage Normal ball visual
     * @param puckImage Puck visual
     * @param heartImage Heart pickup visual
     * @param collisionSound Collision sound effect
     * @param heartDimensions Size of heart pickups
     * @param livesCounter Lives tracking
     * @param mainPaddle Reference to main paddle
     */
    public StrategyFactory(GameObjectCollection gameObjects,
                           ImageReader imageReader,
                           SoundReader soundReader,
                           UserInputListener inputListener,
                           Vector2 windowDimensions,
                           Ball mainBall,
                           Renderable redBallImage,
                           Renderable normalBallImage,
                           Renderable puckImage,
                           Renderable heartImage,
                           Sound collisionSound,
                           Vector2 heartDimensions,
                           Lives livesCounter,
                           Paddle mainPaddle) {
        this.gameObjects = gameObjects;
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.mainBall = mainBall;
        this.redBallImage = redBallImage;
        this.normalBallImage = normalBallImage;
        this.puckImage = puckImage;
        this.heartImage = heartImage;
        this.collisionSound = collisionSound;
        this.heartDimensions = heartDimensions;
        this.livesCounter = livesCounter;
        this.mainPaddle = mainPaddle;
        this.turboStrategy = new TurboStrategy(gameObjects, mainBall,
                                               redBallImage, normalBallImage);
        this.random = new Random();
    }

    /**
     * Creates a new collision strategy with initial depth of 3.
     *
     * @return A randomly selected collision strategy
     */
    public CollisionStrategy getStrategy() {
        return getStrategy(3);
    }

    /**
     * Creates a collision strategy with specified nesting depth.
     * Has a 50% chance of basic strategy, 40% chance of special strategy,
     * and 10% chance of double strategy (if depth allows).
     *
     * @param depth Current nesting depth for combined strategies
     * @return A randomly selected collision strategy
     */
    private CollisionStrategy getStrategy(int depth) {
        int roll = random.nextInt(10); // 0–9

        // 0–4: 5/10 = ½ chance for Basic
        if (roll < 1) {
            return new BasicCollisionStrategy(gameObjects);
        }

        // 5–8: 4/10 = one special strategy
        if (roll < 9) {
          switch (roll) {
                case 5:
                    return new AddPuckStrategy(gameObjects, puckImage,
                            collisionSound,windowDimensions );
                case 6:
                    return new ExtraPaddleStrategy(gameObjects, imageReader,
                            inputListener, windowDimensions);
                case 7:
                    return new TurboStrategy(gameObjects, mainBall,
                            redBallImage, normalBallImage);
              case 8:
                    return new HeartStrategy(gameObjects, heartImage, heartDimensions,
                                             livesCounter, mainPaddle);
            }
        }
        // 9: 1/10 = DoubleStrategy, unless max depth reached
        if (depth >= MAX_DEPTH) {
            return new BasicCollisionStrategy(gameObjects );
        }

        // Randomly choose two different strategies (not double again)
        CollisionStrategy first = getStrategy(depth + 1);
        CollisionStrategy second;
        do {
            second = getStrategy(depth + 1);
        } while (second.getClass().equals(first.getClass()));

        return new DoubleStrategy(first, second);
    }




}