import java.util.Random;
/**
 * Represents a clever player in a Tic Tac Toe game.
 * The CleverPlayer attempts to place its mark strategically by first checking
 * for adjacent cells with its own mark. If no such move is possible, it places
 * its mark in a random empty cell.
 */
public class CleverPlayer implements Player {
    /**
     * Plays a turn for the CleverPlayer.
     * The player first tries to place its mark in a cell adjacent to one of its
     * own marks. If no such move is possible, it selects a random empty cell.
     *
     * @param board The game board on which the player will place its mark.
     * @param mark The mark of the player.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        // Try to place the mark next to an existing mark of the same type
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Check if the current cell is empty and has a neighbor with the same mark
                if (board.getMark(row, col) == Mark.BLANK && hasNeighbor(board, row, col, mark)) {
                    if (board.putMark(mark, row, col)) {
                        return;
                    }
                }
            }
        }
        // If no strategic move is possible, choose a random empty cell
        locationRandom(board, mark);
    }
    /**
     * Checks if a given cell has a neighboring cell with the same mark.
     *
     * @param board The game board to check.
     * @param row The row index of the cell to check.
     * @param col The column index of the cell to check.
     * @param mark The mark to look for in neighboring cells.
     * @return True if a neighboring cell contains the same mark, false otherwise.
     */
    private boolean hasNeighbor(Board board, int row, int col, Mark mark) {
        int size = board.getSize();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // because uts the cell in question skip the cell itself
                if (i == 0 && j == 0) {
                    continue;
                }
                int rowCell = row + i;
                int colCell = col + j;
                if (rowCell >= 0 && rowCell < size && colCell >= 0 && colCell < size) {
                    if (board.getMark(rowCell, colCell) == mark) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * Places the mark in a random empty cell on the board.
     *
     * @param board The game board on which the player will place its mark.
     * @param mark The mark of the player.
     */
    private void locationRandom(Board board, Mark mark) {
        Random rand = new Random();
        int size = board.getSize();
        int row = rand.nextInt(size);
        int col = rand.nextInt(size);
        while (!board.putMark(mark, row, col)) {
            row = rand.nextInt(size);
            col = rand.nextInt(size);

        }
    }
}
