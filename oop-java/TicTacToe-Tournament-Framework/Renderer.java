/**
 * Interface for rendering a Tic Tac Toe board.
 * The Renderer interface defines a method for displaying the game board.
 * Implementations of this interface are responsible for rendering the board
 * in a specific format (e.g., console output, GUI, etc.).
 */
public interface Renderer {

    /**
     * Renders the given game board.
     * The implementation of this method should display the current state of the board
     * using the provided Board instance.
     *
     * @param board The game board to render.
     */
    void renderBoard(Board board);
}