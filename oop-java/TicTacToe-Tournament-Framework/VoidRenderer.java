/**
 * A Renderer implementation that performs no rendering.
 * The VoidRenderer class is a placeholder implementation of the Renderer interface,
 * where the renderBoard method does nothing. This can be useful in scenarios
 * where rendering is not required or needs to be disabled.
 */
public class VoidRenderer implements Renderer {

    /**
     * Constructs a VoidRenderer instance.
     * This constructor initializes the VoidRenderer with no additional setup.
     */
    public VoidRenderer() {}

    /**
     * Renders the given game board.
     * This implementation does nothing and is intended to suppress rendering.
     *
     * @param board The game board to render (not used in this implementation).
     */
    public void renderBoard(Board board) {}
}