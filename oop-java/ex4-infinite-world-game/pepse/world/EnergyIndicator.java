package pepse.world;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * A visual indicator that displays the avatar's current energy level.
 * This class creates and manages an energy display that follows the camera view.
 */
public class EnergyIndicator extends GameObject {
    /** The text renderer used to display the energy value */
    private TextRenderable textRenderable;

    /**
     * Creates a new energy indicator at the specified position.
     *
     * @param topLeftCorner The top-left position where the indicator should appear
     * @param dimensions The size of the indicator display
     */
    public EnergyIndicator(Vector2 topLeftCorner,
                           Vector2 dimensions) {
        super(topLeftCorner, dimensions, new RectangleRenderable(Color.GREEN));
        this.textRenderable = new TextRenderable("0");

        this.renderer().setRenderable(textRenderable);
        this.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Updates the displayed energy value.
     *
     * @param currentEnergy The current energy level to display (will be shown as percentage)
     */
    public void updateEnergy(float currentEnergy) {
        textRenderable.setString(String.valueOf((int)currentEnergy) + "%");
    }
}
