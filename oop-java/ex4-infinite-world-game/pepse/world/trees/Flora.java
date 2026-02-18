package pepse.world.trees;

import danogl.GameObject;
import danogl.components.Transition;
import danogl.components.ScheduledTask;
import danogl.util.Vector2;
import pepse.world.Block;
import pepse.world.Terrain;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Manages the generation and placement of flora (trees) in the game world.
 * Uses a seeded random number generator to ensure consistent generation
 * across game sessions with the same seed.
 */
public class Flora {
    private static final int MIN_TREE_HEIGHT = 5; // Minimum height of a tree trunk
    private static final int MAX_TO_ADD = 3; // Maximum height of a tree trunk
    private static final float TREE_GENERATION_CHANCE = 0.1f; // 30% chance to generate a tree
    private static final int TREE_WIDTH = 1; // Width of the tree trunk
    private static final int TREE_SPACING = 3; // Spacing between trees
    /** Seed used for random number generation */
    private final int seed;

    /** Random number generator for consistent flora placement */
    private final Random rand;

    /** Function that provides the ground height at any given x coordinate */
    private final Function<Float, Float> positionSupplier;

    /**
     * Creates a new Flora manager for generating trees.
     *
     * @param seed The seed for random number generation
     * @param positionSupplier Function that returns ground height for any x coordinate
     */
    public Flora(int seed, Function<Float, Float> positionSupplier) {
        this.seed = seed;
        this.rand = new Random(seed);
        this.positionSupplier = positionSupplier;
    }

    /**
     * Creates trees (trunk + leaves) in the given X range.
     * Trees are generated with a 30% probability at regular intervals.
     * Each tree has a random height between 5 and 7 blocks.
     *
     * @param minX The leftmost x-coordinate to start generating trees
     * @param maxX The rightmost x-coordinate to stop generating trees
     * @return List of all created GameObjects (trunks, leaves, and fruits)
     */
    public List<GameObject> createInRange(int minX, int maxX) {
        List<GameObject> allObjects = new ArrayList<>();
        float step = Block.SIZE;

        for (int x = minX; x < maxX; x += step*3) {
            if (rand.nextFloat() > TREE_GENERATION_CHANCE) continue; // 10% chance to generate a tree

            float groundY = positionSupplier.apply((float) x);
            int trunkHeight = MIN_TREE_HEIGHT + rand.nextInt(MAX_TO_ADD);
            allObjects.addAll(Tree.create(new Vector2(x, groundY), trunkHeight));
        }
        return allObjects;
    }
}
