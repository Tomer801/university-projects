package pepse.world.clouds;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.util.Random;

/**
 * Represents a cloud system in the game world.
 * Creates and manages cloud formations that move across the sky using a block-based pattern system.
 * Clouds are created off-screen and move horizontally in a continuous loop.
 */
public class Cloud {
    /** Base color for cloud blocks */
    private static final Color BASE_CLOUD_COLOR = new Color(255, 255, 255);

    /** Visual representation of cloud blocks with slight color variation */
    private static final Renderable CLOUD_RENDERABLE =
            new RectangleRenderable(ColorSupplier.approximateMonoColor(BASE_CLOUD_COLOR));

    /** Time in seconds for a cloud to complete one movement cycle */
    private static final float CLOUD_TIME = 30f;

    /** Size of each cloud block */
    public static final float SIZE = Block.SIZE / 2;

    /**
     * Cloud formation pattern represented as a 2D grid.
     * 1 represents a cloud block, 0 represents empty space.
     * Creates a realistic cloud shape with varying density.
     */
    private static List<List<Integer>> cloud = List.of(
            List.of(0, 1, 1, 0, 0, 0),  // Top edge of cloud
            List.of(1, 1, 1, 0, 1, 0),  // Upper middle section
            List.of(1, 1, 1, 1, 1, 1),  // Dense middle section
            List.of(1, 1, 1, 1, 1, 1),  // Dense middle section
            List.of(0, 1, 1, 1, 0, 0),  // Lower edge
            List.of(0, 0, 0, 0, 0, 0)   // Bottom spacing
    );

    /**
     * Creates a cloud formation that moves across the screen.
     * Clouds are created off-screen to the left and move rightward in a continuous loop.
     * Each cloud block follows the pattern defined in the cloud matrix and moves independently.
     *
     * @param windowDimensions The dimensions of the game window
     * @return List of GameObjects representing the cloud blocks
     */
    public static List<GameObject> create(Vector2 windowDimensions) {
        List<GameObject> objects = new ArrayList<>();

        // Calculate total cloud dimensions
        float cloudWidth = 6 * SIZE;
        float cloudHeight = 6 * SIZE;

        // Position cloud off-screen to create smooth entrance
        Vector2 startPos = new Vector2(-cloudWidth, SIZE * 4);

        // Create cloud blocks according to pattern
        for (int row = 0; row < cloud.size(); row++) {
            for (int col = 0; col < cloud.get(row).size(); col++) {
                if (cloud.get(row).get(col) == 1) {
                    // Calculate position for this cloud block
                    Vector2 pos = startPos.add(new Vector2(col * SIZE, row * SIZE));
                    GameObject cloudBlock = new Block(pos, CLOUD_RENDERABLE);
                    cloudBlock.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
                    objects.add(cloudBlock);
                    cloudBlock.setDimensions(new Vector2(SIZE, SIZE));

                    // Set up horizontal movement animation
                    Vector2 initialCenter = new Vector2(
                            pos.x() + SIZE / 2,
                            pos.y() + SIZE / 2
                    );

                    // Create looping horizontal movement
                    new Transition<>(
                        cloudBlock,
                        x -> cloudBlock.setCenter(new Vector2(x, initialCenter.y())),
                        initialCenter.x(),                              // Start position
                        initialCenter.x() + windowDimensions.x() + 100,// End position
                        Transition.LINEAR_INTERPOLATOR_FLOAT,           // Smooth movement
                        CLOUD_TIME,                                    // Duration of one cycle
                        Transition.TransitionType.TRANSITION_LOOP,      // Continuous movement
                        null                                             // No callback needed
                    );
                }
            }
        }

        return objects;
    }

}
