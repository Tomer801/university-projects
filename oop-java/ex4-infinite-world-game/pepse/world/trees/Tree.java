package pepse.world.trees;

import danogl.GameObject;
import danogl.gui.rendering.OvalRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.NameConstant;
import pepse.world.Block;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a tree in the game world.
 * Creates and manages tree objects including trunk, leaves, and fruits.
 */
public class Tree {
    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private static final Color LEAF_COLOR = new Color(50, 200, 30);
    private static final int LEAF_ROWS = 5;
    private static final int LEAF_COLS = 6;
    /** Width of the trunk in blocks */
    public static final int TRUNK_WIDTH = 2;
    /** Height of the trunk in blocks */
    public static final int LEAF_SIZE = 15;
    /** Size of each leaf block */
    private static final Renderable LEAF_RENDERABLE = new RectangleRenderable(LEAF_COLOR);
    /** Renderable for the fruit, using an oval shape with a specific color */
    private static final Renderable FRUIT_RENDERABLE =
            new OvalRenderable(new Color(207, 13, 85, 255));
    /** Size of the fruit block */
    public static final int FRUIT_SIZE = 15;
    /** Size of rows in the leaf area */
    public static final float SIZE = 15f;

    /**
     * Creates a complete tree object at the specified position.
     *
     * @param basePosition The base position of the tree
     * @param trunkHeight The height of the tree trunk
     * @return List of GameObjects that make up the tree (trunk, leaves, and fruit)
     */
    public static List<GameObject> create(Vector2 basePosition, int trunkHeight) {
        List<GameObject> allTree = new ArrayList<>();

        // Create trunk blocks
        for (int y = 0; y < trunkHeight; y++) {
            for (int x = 0; x < TRUNK_WIDTH; x++) {
                Vector2 pos = basePosition
                        .subtract(new Vector2(x * SIZE , (trunkHeight  - y) * SIZE));
                GameObject trunkBlock = new Block(pos, new RectangleRenderable(TRUNK_COLOR));
                trunkBlock.setDimensions(new Vector2(SIZE, SIZE));
                trunkBlock.setTag(NameConstant.TREE);
                allTree.add(trunkBlock);
            }
        }

        // Calculate the position of the trunk's top center
        Vector2 topOfTrunk = basePosition
                .subtract(new Vector2(SIZE / 2f, trunkHeight * (SIZE) / 1.5f));

        // Create leaf area with multiple rows and columns
        float leafWidth = LEAF_COLS * LEAF_SIZE - SIZE - 10;
        // Adjust width to fit within trunk bounds
        float leafHeight = LEAF_ROWS * LEAF_SIZE - SIZE;
        // Adjust height to fit within trunk bounds
        Vector2 leafTopLeft =
                topOfTrunk.subtract(new Vector2(leafWidth / TRUNK_WIDTH, leafHeight));
        List<GameObject> leaves = createLeaves(leafTopLeft);
        allTree.addAll(leaves);

        // Add a randomly positioned fruit within the leaf area
        Random rand = new Random();
        float fruitX = leafTopLeft.x() + rand.nextInt((int) leafWidth - FRUIT_SIZE);
        float fruitY = leafTopLeft.y() + rand.nextInt((int) leafHeight - FRUIT_SIZE);
        Fruit fruit = new Fruit(new Vector2(fruitX, fruitY),
                      new Vector2(FRUIT_SIZE, FRUIT_SIZE),
                      FRUIT_RENDERABLE);
        fruit.setTag(NameConstant.FRUIT);
        allTree.add(fruit);

        return allTree;
    }

    /**
     * Creates the leaf blocks for the tree's canopy.
     *
     * @param topLeft The top-left position where leaves should start
     * @return List of leaf GameObjects
     */
    private static List<GameObject> createLeaves(Vector2 topLeft) {
        List<GameObject> leaves = new ArrayList<>();
        for (int row = 0; row < LEAF_ROWS; row++) {
            for (int col = 0; col < LEAF_COLS; col++) {
                Vector2 pos = topLeft.add(new Vector2(col * LEAF_SIZE, row * LEAF_SIZE));
                Leaf leaf = new Leaf(pos, new Vector2(LEAF_SIZE, LEAF_SIZE), LEAF_RENDERABLE);
                leaf.setTag(NameConstant.LEAF);
                leaves.add(leaf);
            }
        }
        return leaves;
    }
}
