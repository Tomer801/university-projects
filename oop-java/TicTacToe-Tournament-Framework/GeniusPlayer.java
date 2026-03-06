import java.util.Random;
/**
 * Represents a GeniusPlayer in a Tic Tac Toe game.
 * The GeniusPlayer attempts to make the best possible move by analyzing the board
 * and extending streaks of its own marks. If no optimal move is found, it places
 * its mark in a random empty cell.
 */
public class GeniusPlayer implements Player {
    /**
     * Plays a turn for the GeniusPlayer.
     * The player analyzes the board to find the best move that extends its streak
     * of marks. If no such move is found, it places its mark in a random empty cell.
     *
     * @param board The game board on which the player will place its mark.
     * @param mark The mark of the player (e.g., X or O).
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        // Initialize variables to track the best move
        int bestRow = 0;
        int bestCol = 0;
        int bestStreak = -1;
        // Directions to check for streaks: right, down, diagonal down-right, diagonal down-left
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        // Iterate through the board to find the best move
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Check if the current cell is not the player's mark
                if (board.getMark(row, col) != mark) {
                    continue;
                }
                // Check each direction for streaks
                for (int[] direction : directions) {
                    int rowDirection = direction[0];
                    int colDirection = direction[1];
                    int streak = 1;

                    int currentRowForword = row + rowDirection;
                    int currentCollForword = col + colDirection;
                    // Continue the streak in the current direction
                    while (inBounds(currentRowForword, currentCollForword, size) &&
                            board.getMark(currentRowForword, currentCollForword) == mark) {
                        streak++;
                        currentRowForword += rowDirection;
                        currentCollForword += colDirection;
                    }
                    // Check if the next cell in the current direction is empty
                    if (inBounds(currentRowForword, currentCollForword, size)
                        && board.getMark(currentRowForword, currentCollForword) == Mark.BLANK
                        && streak >=bestStreak) {
                        bestStreak = streak;
                        bestRow = currentRowForword;
                        bestCol = currentCollForword;

                    }
                }
            }
        }
        // Place the mark in the best position or choose a random position if no streak is found

        if ( bestStreak > 0) {
            board.putMark(mark, bestRow, bestCol);
            return;
        }
        // If no optimal move is found, place the mark in a random empty cell
        locationRandom(board, mark);

    }

    /**
     * Checks if a given position is within the bounds of the board.
     *
     * @param row The row index to check.
     * @param col The column index to check.
     * @param size The size of the board.
     * @return True if the position is within bounds, false otherwise.
     */
    private boolean inBounds(int row, int col, int size) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Places the player's mark in a random empty cell on the board.
     *
     * @param board The game board on which the mark will be placed.
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

