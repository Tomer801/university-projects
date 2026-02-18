package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Creates and manages the sky background in the game world.
 * The sky is a static background that follows the camera.
 */
public class Sky {
    /** The default color of the sky */
    private static final Color BASIC_SKY_COLOR = Color.decode("#80C6E5");

    /**
     * Creates the sky game object that fills the game window.
     * The sky follows the camera and always remains in view.
     *
     * @param windowDimensions The width and height of the game window
     * @return GameObject representing the sky background
     */
    public static GameObject create(Vector2 windowDimensions){
        GameObject sky = new GameObject(Vector2.ZERO,
                         windowDimensions,
                         new RectangleRenderable(BASIC_SKY_COLOR));
        sky.setTag("sky");
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);

        return sky;
    }

}
