package pepse.world;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import danogl.GameObject;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.NameConstant;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

/**
 * Responsible for generating and managing the terrain in the game world.
 * Uses noise to create a natural-looking ground surface and generates
 * blocks to represent the terrain.
 */
public class Terrain {
    // Base color for the ground blocks
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    // Number of blocks in depth for the terrain
    private static final int TERRAIN_DEPTH = 20;
    // Frequency of the noise used for terrain generation
    private static final int NOISE_FREQUENCY = 6;

    /* This is the fiels for the class Terrain.
     * It is used to determine the height of the ground relative to the sky.
     * The value 0.66f means that the ground will be at 66% of the window height.
     */
    private static final float GROUND_SKY_RATIO = 0.66f;
    // The dimensions of the game window, used for terrain generation.
    private final Vector2 windowDimensions;
    // The height of the ground at x = 0, used as a reference point for terrain generation.
    private final int groundHeightAtX0;
    // Noise generator used to create the terrain's height variations.
    private final NoiseGenerator noiseGenerator;
    // Renderable object used to draw the ground blocks.
    private final Renderable groundRenderable;


    /**
     * Constructs a new Terrain object.
     *
     * @param windowDimensions The dimensions of the game window.
     * @param seed The seed for the noise generator to ensure consistent terrain generation.
     */
    public Terrain(Vector2 windowDimensions, int seed) {

        this.windowDimensions = windowDimensions;
        this.groundHeightAtX0 = (int)(windowDimensions.y() * GROUND_SKY_RATIO);
        this.noiseGenerator = new NoiseGenerator(seed, groundHeightAtX0);
        this.groundRenderable = new RectangleRenderable(
                ColorSupplier.approximateColor(BASE_GROUND_COLOR)
        );
    }

    /**
     * Returns the height of the ground at the specified x-coordinate.
     *
     * @param x The x-coordinate.
     * @return The y-coordinate of the ground at the given x.
     */
    public float groundHeightAt(float x){

        return (float)noiseGenerator.noise(x,Block.SIZE*NOISE_FREQUENCY) + groundHeightAtX0;
    }
    /**
     * Creates terrain blocks in the specified x-range.
     *
     * @param minX The minimum x-coordinate (inclusive).
     * @param maxX The maximum x-coordinate (exclusive).
     * @return A list of GameObjects representing the terrain blocks in the range.
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        List<GameObject> blocks = new ArrayList<>();
        for (int x = minX; x < maxX; x += Block.SIZE) {
            float groundHeight = groundHeightAt(x);
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                Vector2 pos = new Vector2(x, groundHeight + i * Block.SIZE);
                Block block = new Block(pos, groundRenderable);
                if (x == 0){
                    block.setTag(NameConstant.TERRAIN_TOP);
                } else {
                    block.setTag(NameConstant.TERRAIN);
                }
                blocks.add(block);
            }
        }
        return blocks;
    }
}