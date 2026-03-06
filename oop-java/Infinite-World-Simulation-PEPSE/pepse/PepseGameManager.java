package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import danogl.collisions.Layer;
import pepse.world.*;
import pepse.world.clouds.Drop;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;
import pepse.world.clouds.Cloud;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Main game manager for the PEPSE (Procedural Endless Platformer Smooth Experience) game.
 * Handles game initialization, world generation, and ongoing game systems including:
 * - Day/night cycle
 * - Dynamic world chunk loading
 * - Weather effects (clouds and rain)
 * - Energy system management
 */
public class PepseGameManager extends GameManager {
    /** Duration of one complete day-night cycle in seconds */
    public static final float DAY_NIGHT_CYCLE_LENGTH = 30f;
    /** Number of frames per second for the game */
    public static final int TWO = 2;
    /** Ratio of ground height to sky height in the game world */
    public static final float GROUND_SKY_RATIO = 0.66f;

    /** The dimensions of the game window */
    private  Vector2 windowDimensions;

    /** Maximum energy level for the avatar */
    private static final float MAX_ENERGY = 100f;




    /**
     * Initializes all game components and systems.
     * Sets up the following game elements:
     * - Sky background
     * - Terrain and flora generation
     * - Day/night cycle with sun and halo
     * - Avatar with camera tracking
     * - Energy indicator UI
     * - Cloud system with rain effects
     * - Infinite world chunk management
     *
     * @param imageReader For loading game assets and sprites
     * @param soundReader For loading game audio (currently unused)
     * @param inputListener For handling player input
     * @param windowController For managing the game window
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowDimensions = windowController.getWindowDimensions();
        // set sky
        GameObject sky = pepse.world.Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
        // set ground
        Terrain ground = new Terrain(windowDimensions, 1234);
        Flora flora = new Flora(1234, ground::groundHeightAt);





        // set night
        GameObject night = Night.create(windowDimensions,
                DAY_NIGHT_CYCLE_LENGTH);

        gameObjects().addGameObject(night, Layer.UI);
        // set sun
        GameObject sun = Sun.create(windowDimensions, DAY_NIGHT_CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        GameObject sunHalo = SunHalo.create(sun);
        sunHalo.addComponent((float deltaTime) ->
                sunHalo.setCenter(sun.getCenter())
        );

        gameObjects().addGameObject(sunHalo, Layer.BACKGROUND);
        //set Avatar
        Vector2 initialAvatarLocation = new Vector2(windowDimensions.x()/ TWO,
                windowDimensions.y()* GROUND_SKY_RATIO / TWO );
        Avatar avatar = new Avatar(initialAvatarLocation, inputListener, imageReader);
        avatar.setTag("avatar");
        gameObjects().addGameObject(avatar, Layer.DEFAULT);
        setCamera(new Camera(avatar,
        new Vector2(0, -100),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));


        //set indicator
        EnergyIndicator indicator = new EnergyIndicator(Vector2.ZERO,
                                                   new Vector2(50, 20));

        indicator.setTag("energyIndicator");
        gameObjects().addGameObject(indicator, Layer.UI);
        avatar.addComponent((float deltaTime) ->
                (indicator).updateEnergy(avatar.getEnergy())
        );


        // set clouds
        List<GameObject> clouds = Cloud.create(windowDimensions);
        for (GameObject cloud : clouds) {
            gameObjects().addGameObject(cloud, Layer.BACKGROUND);
        }
        avatar.addJumpBehavior(() -> rainDrop(clouds));

        int step = (int)windowDimensions.x() ;

        InfiniteWorld infiniteWorld = new InfiniteWorld(
                flora, ground,  step);
        avatar.addComponent((float deltaTime) -> {
            createChunks(infiniteWorld, avatar, step);
        });





    }


    /**
     * Creates rain drops from clouds when the avatar jumps.
     * Randomly determines which clouds will produce drops, and removes drops
     * that fall below the screen boundary.
     *
     * @param cloud List of cloud objects that can produce rain drops
     * @return List of created rain drop objects
     */
    public List<GameObject> rainDrop(List<GameObject> cloud) {
        Random rand = new Random();
        List<GameObject> drops = new ArrayList<>();
        for (GameObject item : cloud) {
            if (rand.nextFloat() < 0.4f) continue;
            Vector2 position = item.getCenter();
            GameObject drop = new Drop(position);
            drop.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            drop.setTag("drop");
            gameObjects().addGameObject(drop);
            drop.addComponent((float deltaTime) -> {
                        if (drop.getCenter().y() > windowDimensions.y()) {
                            gameObjects().removeGameObject(drop);
                        }
                    }
            );
        }
        return drops;


    }
    

/**
     * Manages the dynamic creation and cleanup of world chunks around the avatar.
     * This method performs two main functions:
     * 1. Creates new chunks in a radius around the player's current position
     * 2. Removes far chunks to prevent excessive memory usage
     *
     * @param infiniteWorld The world manager that handles chunk creation and storage
     * @param avatar The player character used to determine chunk loading center
     * @param step The width of each chunk in pixels
     */
    public void createChunks(InfiniteWorld infiniteWorld, Avatar avatar, int step) {
        float avatarX = avatar.getCenter().x();

        int currentChunk = (int) Math.floor(avatarX / step);

        // Number of chunks to load on each side of the player
        int radius = TWO;

        // Create new chunks within radius of player
        for (int i = -radius; i <= radius; i++) {
            int chunkIndex = currentChunk + i;
            if (!infiniteWorld.getWorldMap().containsKey(chunkIndex)) {
                List<GameObject> newChunk = infiniteWorld.createChunkAt(chunkIndex);
                extracted(newChunk); // Add objects to the game
            }
        }

        // Remove chunks when there are too many to prevent memory issues
        if (infiniteWorld.getWorldMap().size() > 6) {
            // Determine which edge chunk to remove based on player position
            int farChunk = avatarX < (infiniteWorld.getMinChunk() +
                                     infiniteWorld.getMaxChunk()) * step / TWO
                    ? infiniteWorld.getMaxChunk()
                    : infiniteWorld.getMinChunk();

            List<GameObject> toRemove = infiniteWorld.getWorldMap().remove(farChunk);
            if (toRemove != null) {
                for (GameObject obj : toRemove) {
                    gameObjects().removeGameObject(obj);
                }
            }
        }
    }



    /**
     * Adds game objects to their appropriate rendering layers based on their type.
     * Different objects are assigned to specific layers for proper visual ordering:
     * - Trees and terrain go to STATIC_OBJECTS layer
     * - Fruits go to DEFAULT layer
     * - Leaves go to BACKGROUND-1 layer (slightly in front of background)
     *
     * @param chunks List of game objects to be added to the game world
     */
    private void extracted(List<GameObject> chunks) {
        for (GameObject chunk : chunks) {
            if (chunk.getTag().equals(NameConstant.TREE)) {

                gameObjects().addGameObject(chunk, Layer.STATIC_OBJECTS);
            }
            if (chunk.getTag().equals(NameConstant.FRUIT)) {
                gameObjects().addGameObject(chunk, Layer.DEFAULT);
            }
            if (chunk.getTag().equals(NameConstant.LEAF)) {
                gameObjects().addGameObject(chunk, Layer.DEFAULT-1);
            }
            if (chunk.getTag().equals(NameConstant.TERRAIN)) {

                gameObjects().addGameObject(chunk, Layer.STATIC_OBJECTS);
            }
        }
    }
/**
     * Main entry point for the PEPSE game.
     * Initializes the game manager and starts the game loop.
     *
     * @param args Command line arguments (not used)
     */
     public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
