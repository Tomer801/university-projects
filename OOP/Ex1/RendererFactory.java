/**
 * Represents the game board for a Tic Tac Toe game.
 * The Board class manages the grid where players place their marks
 * and provides methods to interact with the board.
 */
public class RendererFactory {
    /**
     * Creates a Renderer based on the specified type and size.
     *
     * @param type The type of renderer to create ("console", "void").
     * @param size The size of the board for the renderer.
     * @return A Renderer object of the specified type, or null if the type is not recognized.
     */
    public  Renderer buildRenderer(String type, int size){
        switch (type) {
            case "console":
                return new ConsoleRenderer(size);
            case "void":
                return new VoidRenderer();
            default:
                return null;
        }
    }
}
