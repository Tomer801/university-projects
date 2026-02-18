package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Represents a visual lives display panel in the game UI.
 * Shows both a numeric counter and heart icons to indicate remaining lives.
 * The numeric display changes color based on the number of lives remaining.
 */
public class Lives extends GameObject {
    /** Maximum number of lives that can be displayed */
    private final int maxLives;
    /** Current number of lives remaining */
    private int currentLives;
    /** Initial number of lives at game start */
    private int startingLives;
    /** Array holding the heart objects that visually represent lives */
    private final Heart[] hearts;
    /** Collection managing all game objects */
    private final GameObjectCollection colection;
    /** Position of the lives panel in the UI */
    private final Vector2 panelPosition;
    /** Size of each heart icon */
    private final Vector2 heartDimensions;
    /** Visual representation of heart icons */
    private final Renderable heartRenderable;
    /** Text display showing the numeric life count */
    private TextRenderable textRenderable;

    /**
     * Creates a new lives display panel.
     *
     * @param panelPosition Position of the panel in the UI
     * @param heartDimensions Size of each heart icon
     * @param heartRenderable Visual representation for heart icons
     * @param currentLives Starting number of lives
     * @param maxLives Maximum number of lives that can be displayed
     * @param collection Collection managing game objects
     */
    public Lives(Vector2 panelPosition,
                 Vector2 heartDimensions,
                 Renderable heartRenderable,
                 int currentLives,
                 int maxLives,
                 GameObjectCollection collection) {
        super(panelPosition, heartDimensions, null);
        this.panelPosition = panelPosition;
        this.heartDimensions = heartDimensions;
        this.maxLives = maxLives;
        this.currentLives = currentLives;
        this.hearts = new Heart[maxLives];
        this.colection = collection;
        this.heartRenderable = heartRenderable;
        this.startingLives = currentLives;
        this.textRenderable = new TextRenderable(String.valueOf(currentLives));
        createPanel();
    }

    /**
     * Creates the initial visual elements of the lives panel.
     * Sets up both the numeric display and heart icons.
     */
    private void createPanel() {
        int heartWidth = (int) heartDimensions.x();
        int heartHeight = (int) heartDimensions.y();
        Vector2 heartSize = new Vector2(heartWidth, heartHeight);

        // Create the numeric display
        this.textRenderable = new TextRenderable("" + currentLives);
        textRenderable.setColor(Color.green);
        colection.addGameObject(new GameObject(panelPosition, heartSize, textRenderable),
                Layer.UI);
        panelPosition.add(heartSize);

        // Create the heart icons
        for (int i = 0; i < startingLives; i++) {
            Vector2 heartPos = new Vector2(panelPosition.x() + (i+1) * heartWidth, panelPosition.y());
            Heart heart = new Heart(heartPos, heartSize, heartRenderable);
            hearts[i] = heart;
            colection.addGameObject(hearts[i], Layer.UI);
        }
    }

    /**
     * Removes one life and updates both the numeric display and heart icons.
     * Changes the display color based on remaining lives.
     */
    public void loseLife() {
        if (currentLives == 0) {
            return;
        }
        currentLives--;
        hearts[currentLives].lostLife();
        colection.removeGameObject(hearts[currentLives], Layer.UI);
        setColorByLives();
    }

    /**
     * Adds one life if below maximum and updates the display.
     * Creates a new heart icon and updates the numeric counter.
     */
    public void addLife() {
        if (currentLives < maxLives) {
            Vector2 heartPos = new Vector2(panelPosition.x() + (currentLives+1) * heartDimensions.x(),
                    panelPosition.y());
            Heart heart = new Heart(heartPos, heartDimensions, heartRenderable);
            hearts[currentLives] = heart;
            colection.addGameObject(heart, Layer.UI);
            currentLives++;
            setColorByLives();
        }
    }

    /**
     * Updates the color of the numeric display based on remaining lives.
     * Green: At or above starting lives
     * Yellow: Two lives remaining
     * Red: One or zero lives remaining
     */
    private void setColorByLives() {
        this.textRenderable.setString("" + currentLives);
        if (currentLives >= startingLives) {
            textRenderable.setColor(Color.green);
        } else if (currentLives == 2) {
            textRenderable.setColor(Color.yellow);
        } else {
            textRenderable.setColor(Color.red);
        }
    }

    /**
     * Gets the current number of lives remaining.
     *
     * @return The number of lives the player has left
     */
    public int getCurrentLives() {

        return currentLives;
    }
}