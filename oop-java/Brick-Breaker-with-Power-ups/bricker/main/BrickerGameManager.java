package bricker.main;
import bricker.brick_strategies.CollisionStrategy;
import bricker.brick_strategies.StrategyFactory;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The BrickerGameManager class is responsible for managing the game state and
 * interactions in the Bricker game. It handles the initialization of game objects,
 * updates the game state, and manages user input.
 * @author Tomer Kdosh,
 * @author  Shay Bental
 *
 */
public class BrickerGameManager extends GameManager {
    private static final float BALL_SPEED = 200 ;
    private static final int DEF_ROW = 7;
    private static final int DEF_COL = 8;
    private static final int WALL_WIDTH = 20;
    private static final float BRICK_HIGHT= 15;
    private static final int BALL_SIZE = 20;
    private static final int MAX_LIFE = 4;
    private static final int START_LIFE = 3;
    private static final String PROMPT_GAME_OVER = "Game Over! Do you want to play again?";
    private static final int HEART_SIZE= 20;
    private static final String PROMPT_GAME_WON = "You Won! Do you want to play again?";
    // Constructor for the BrickerGameManager class
    private int brickRow;
    private int brickCol;
    private Ball ball;
    private Lives livesPanel;
    private int life = START_LIFE;
    private int maxLife = MAX_LIFE;
    private Vector2 windowDimensions;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private WindowController windowController;
    private StrategyFactory strategyFactory;
    private Paddle mainPaddle;
    private Renderable redBallImage, normalBallImage, puckImage, heartImage;
    private Sound collisionSound;


    /**
     * Constructor for the BrickerGameManager class.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions) {
        // Call the parent class constructor
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.brickRow = DEF_ROW;
        this.brickCol = DEF_COL;

    }
    /**
     * Constructor for the BrickerGameManager class with custom brick rows and columns.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param row              The number of rows of bricks.
     * @param col              The number of columns of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,int row, int col) {
        // Call the parent class constructor
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.brickRow = row;
        this.brickCol = col;

    }
    /**
     * Initializes the game by setting up the game objects and their properties.
     *
     * @param imageReader      The ImageReader object for loading images.
     * @param soundReader      The SoundReader object for loading sounds.
     * @param inputListener    The UserInputListener object for handling user input.
     * @param windowController The WindowController object for managing the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        /* ---------------- base initialisation ---------------- */
        this.imageReader      = imageReader;
        this.soundReader      = soundReader;
        this.inputListener    = inputListener;
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        Vector2 winDim = windowController.getWindowDimensions();

        /* ---------------- core game objects ------------------ */
        ball        = createBall(imageReader, soundReader, winDim);
        creatPaddle(inputListener, imageReader, winDim);          // sets mainPaddle field
        creatWall(winDim);
        livesPanel  = createLivesPanel();
        creatBackground(imageReader, winDim);

        /* ---------------- extra assets for strategies -------- */
        redBallImage    = imageReader.readImage("assets/redball.png",  true);
        normalBallImage = imageReader.readImage("assets/ball.png",     true);
        puckImage       = imageReader.readImage("assets/mockBall.png", true);
        heartImage      = imageReader.readImage("assets/heart.png",    true);
        collisionSound  = soundReader.readSound("assets/blop.wav");

        /* ---------------- build the factory  (MUST be before bricks!) */
        strategyFactory = new StrategyFactory(
                gameObjects(),
                imageReader,
                soundReader,
                inputListener,
                windowDimensions,
                ball,
                redBallImage,
                normalBallImage,
                puckImage,
                heartImage,
                collisionSound,
                new Vector2(HEART_SIZE, HEART_SIZE),   // heartDimensions
                livesPanel,
                mainPaddle
        );


        /* ---------------- now we can safely create the bricks -------- */
        creatBricks(brickRow, brickCol, imageReader, winDim);
    }

    /**
     * Resets the ball to the center of the screen and assigns it a new random velocity.
     *
     * @param ball The ball object to reset.
     */
    private void resetBall(Ball ball) {
        // Reset the ball's position to the center of the screen
        ball.setCenter(windowDimensions.mult(0.5f));

        // Reset the ball's velocity to a random direction
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) ballVelX *= -1;
        if (rand.nextBoolean()) ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));

        // Reset the ball's image and turbo mode if modified
    }

    /**
     * Updates the game state based on the elapsed time.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double ballHight = ball.getCenter().y();

        if (ballHight > (windowDimensions.y())) {
           handleBallOutOfBounds();
        }
//        handelOutOfBoundsObjects();
        checkWin();

    }
    /**
     * Checks if the game has been won and prompts the user for a new game.
     */
    private void checkWin(){
        if (noBricksLeft() || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            if (windowController.openYesNoDialog(PROMPT_GAME_WON)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }
    /**
     * Handles the event when the ball goes out of bounds.
     */
    private void handleBallOutOfBounds() {
        livesPanel.loseLife();
        if (livesPanel.getCurrentLives() == 0) {
            if(windowController.openYesNoDialog(PROMPT_GAME_OVER)){
                this.windowController.resetGame();
            }
            else{
                this.windowController.closeWindow();
            }
        }
        resetBall(ball);

    }

    /**
     * Creates a Lives panel to display the number of lives remaining.
     *
     * @return The Lives panel object.
     */
    private Lives createLivesPanel(){
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        Lives livesPanel = new Lives(new Vector2(0, windowDimensions.y()-HEART_SIZE-1 ),
                new Vector2(HEART_SIZE, HEART_SIZE),
                heartImage,
                life,maxLife,
                gameObjects());
        gameObjects().addGameObject(livesPanel,Layer.BACKGROUND);
        return livesPanel;
    }

    /**
     * Creates a background for the game.
     *
     * @param imageReader      The ImageReader object for loading images.
     * @param windowDimensions The dimensions of the game window.
     */
    private void creatBackground(ImageReader imageReader, Vector2 windowDimensions) {
        Renderable backgroundImage = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject backgroundObject = new GameObject(new Vector2(0, 0),
                windowDimensions,
                backgroundImage);
        backgroundObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(backgroundObject, Layer.BACKGROUND);
    }

    /**
     * Creates a brick object and adds it to the game.
     *
     * @param imageReader The ImageReader object for loading images.
     * @param width       The width of the brick.
     * @param location    The location of the brick in the game world.
     */
    private void creatBrick(ImageReader imageReader, int width, Vector2 location) {
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        CollisionStrategy collisionStrategy = strategyFactory.getStrategy(); // << NEW LINE

        GameObject brick = new Brick(location,
                new Vector2(width, 15),
                brickImage,
                collisionStrategy);  // << USE IT HERE

        gameObjects().addGameObject(brick);
    }
    /**
     * Creates a grid of bricks in the game.
     *
     * @param row            The number of rows of bricks.
     * @param col            The number of columns of bricks.
     * @param imageReader    The ImageReader object for loading images.
     * @param windowDimensions The dimensions of the game window.
     */
    private void creatBricks(int row, int col,ImageReader imageReader, Vector2 windowDimensions){
        int brickWidth = (int)(windowDimensions.x()-2*WALL_WIDTH - 2*(col+1) )/ col;

        float startX = WALL_WIDTH + 2;
        float startY = WALL_WIDTH + 2;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                creatBrick(imageReader,brickWidth,new Vector2(startX ,startY ));
                startX = startX + brickWidth + 2;

            }
            startY = startY + BRICK_HIGHT + 2;
            startX = WALL_WIDTH + 2;
        }
    }
    /**
     * Creates a paddle object and adds it to the game.
     *
     * @param inputListener    The UserInputListener object for handling user input.
     * @param imageReader      The ImageReader object for loading images.
     * @param windowDimensions The dimensions of the game window.
     */
    private void creatPaddle(UserInputListener inputListener,
                             ImageReader imageReader,
                             Vector2 windowDimensions) {

        Renderable paddleImg = imageReader.readImage("assets/paddle.png", true);

        mainPaddle = new Paddle(
                new Vector2(0, 0),
                new Vector2(100, 20),
                paddleImg,
                inputListener,
                windowDimensions.x()          // ← NEW 5-th arg
        );

        mainPaddle.setCenter(new Vector2(windowDimensions.x()/2,
                windowDimensions.y()-30));

        gameObjects().addGameObject(mainPaddle);
    }


    /** Creates a ball object and adds it to the game.
     *
     * @param imageReader      The ImageReader object for loading images.
     * @param soundReader      The SoundReader object for loading sounds.
     * @param windowDimensions The dimensions of the game window.
     * @return The created ball object.
     */
    private Ball createBall(ImageReader imageReader, SoundReader soundReader, Vector2 windowDimensions) {
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Ball ball = new Ball(new Vector2(0, 0),
                new Vector2(BALL_SIZE, BALL_SIZE),
                ballImage,collisionSound);
        ball.setTag("mainBall");
        this.gameObjects().addGameObject(ball);
        ball.setCenter(windowDimensions.mult(0.5f));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()){
            ballVelX *= -1;
            ballVelY *= -1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        return ball;

    }
    /**
     * Creates walls for the game window.
     *
     * @param windowDimensions The dimensions of the game window.
     */
    private void creatWall(Vector2 windowDimensions) {
        // Create a new wall object - Left wall
        GameObject leftWall = new GameObject(new Vector2(0, 0),
                new Vector2(WALL_WIDTH, windowDimensions.y()),
                null);
        // Right wall
        GameObject rightWall = new GameObject(new Vector2(windowDimensions.x() - WALL_WIDTH, 0),
                new Vector2(WALL_WIDTH, windowDimensions.y()), null);
        // Top wall
        GameObject topWall = new GameObject(new Vector2(0, 0),
                new Vector2(windowDimensions.x(), WALL_WIDTH),
                null);

        gameObjects().addGameObject(leftWall);
        gameObjects().addGameObject(rightWall);
        gameObjects().addGameObject(topWall);
    }

    private boolean noBricksLeft() {
        for (GameObject obj : gameObjects()) {
            if (obj instanceof Brick) {
                return false;
            }
        }
        return true;
    }

    /**
     * The main method to run the game.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        // Create a new instance of the game manager with cmd line args
        if(args.length == 2){
            new BrickerGameManager("bouncing ball",
                    new Vector2(700,500),Integer.parseInt(args[0]),Integer.parseInt(args[1])).run();
        }else{
            // create a new instance of the game manager with default values
            new BrickerGameManager("bouncing ball",
                    new Vector2(700,500)).run();
        }
    }
}
