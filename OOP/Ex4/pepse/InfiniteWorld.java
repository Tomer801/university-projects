//package pepse.world;
//
//import danogl.GameObject;
//import danogl.util.Vector2;
//import pepse.world.trees.Flora;
//import pepse.world.Terrain;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//public class InfiniteWorld {
//
//    private static final int MURGINE = Block.SIZE*4;
//    Terrain terrain;
//    Flora flora;
//    int step;
//    Map<Integer,List<GameObject>> workdMap;
//    int maxX = 0; // Initialize currentX to the starting position
//    int minX = 0; // Initialize minX to the starting position
//    public InfiniteWorld(Flora flora, Terrain terrain,int step) {
//        // Constructor can be used to initialize any necessary components
//        // or properties related to the infinite world.
//        this.flora = flora;
//        this.terrain = terrain;
//        this.step = step;
//        this.workdMap = new HashMap<>(); // Initialize the world map if needed
//         // Initialize currentX to the starting position
//
//    }
//
//    public  List<GameObject> createWorldFront() {
//        List<GameObject> world = terrain.createInRange(maxX, maxX + step);
//        world.addAll( flora.createInRange(maxX + MURGINE, maxX + step - MURGINE));
//        workdMap.put(maxX*step,world);
//        maxX ++; // Update currentX for the next segment
//
//        return world;
//    }
//    public   List<GameObject> createWorldBack() {
//        List<GameObject> world = terrain.createInRange(minX - step,minX);
//        world.addAll( flora.createInRange(minX - step + MURGINE, minX - MURGINE));
//        workdMap.put((minX - 1)*step,world);
//        minX --; // Update currentX for the next segment
//        return world;
//    }
//    public Map<Integer,List<GameObject>> getWorkdMap() {
//        return workdMap;
//    }
//    public int getMaxX() {
//        return maxX* step;
//    }
//    public int getMinX() {
//        return minX*step;
//    }
//
//}
package pepse;

import danogl.GameObject;
import pepse.world.Block;
import pepse.world.Terrain;
import pepse.world.trees.Flora;

import java.util.*;

/**
 * Manages an infinite, procedurally generated world that creates and removes chunks
 * as the player moves through it. This system ensures efficient memory usage while
 * maintaining the illusion of an endless world.
 */
public class InfiniteWorld {

    /** Margin size in pixels to prevent flora generation at chunk edges */
    private static final int MARGIN = Block.SIZE * 4;

    /** Terrain generator for creating ground blocks */
    private final Terrain terrain;

    /** Flora generator for creating trees and vegetation */
    private final Flora flora;

    /** Width of each world chunk in pixels */
    private final int chunkWidth;

    /** Maps chunk indices to their contained game objects */
    private final Map<Integer, List<GameObject>> worldMap = new HashMap<>();

    /** Index of the leftmost active chunk */
    private int currentMinChunk = 0;

    /** Index of the rightmost active chunk */
    private int currentMaxChunk = 0;

    /**
     * Creates a new infinite world manager.
     *
     * @param flora Flora generator for creating vegetation
     * @param terrain Terrain generator for creating ground
     * @param chunkWidth Width of each world chunk in pixels
     */
    public InfiniteWorld(Flora flora, Terrain terrain, int chunkWidth) {
        this.flora = flora;
        this.terrain = terrain;
        this.chunkWidth = chunkWidth;
    }

    /**
     * Creates a new chunk of the world at the specified index.
     * Each chunk contains terrain and flora within its bounds.
     *
     * @param chunkIndex The index of the chunk to create
     * @return List of GameObjects that make up the chunk
     */
    public List<GameObject> createChunkAt(int chunkIndex) {
        int minX = chunkIndex * chunkWidth;
        int maxX = minX + chunkWidth + Block.SIZE;
        List<GameObject> chunk = terrain.createInRange(minX, maxX);


        // Add flora, leaving margins at chunk edges
        chunk.addAll(flora.createInRange(
                minX + MARGIN,
                maxX - MARGIN));

        // Store chunk and update bounds
        worldMap.put(chunkIndex, chunk);
        currentMinChunk = Math.min(currentMinChunk, chunkIndex);
        currentMaxChunk = Math.max(currentMaxChunk, chunkIndex);

        return chunk;
    }

    /**
     * Gets the currently active world chunks
     *
     * @return Map of chunk indices to their game objects
     */
    public Map<Integer, List<GameObject>> getWorldMap() {
        return worldMap;
    }

    /**
     * Gets the index of the leftmost active chunk
     *
     * @return Minimum chunk index
     */
    public int getMinChunk() {
        return currentMinChunk;
    }

    /**
     * Gets the index of the rightmost active chunk
     *
     * @return Maximum chunk index
     */
    public int getMaxChunk() {
        return currentMaxChunk;
    }
}
