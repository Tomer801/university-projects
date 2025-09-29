import java.util.Random;

/**
 * Represents a WhateverPlayer in a Tic Tac Toe game.
 * The WhateverPlayer places its mark randomly on the board until a valid position is found.
 */
public class WhateverPlayer implements Player {
    /**
     * Default constructor for the WhateverPlayer class.
     */
    public WhateverPlayer() {}
    /**
     * Plays a turn for the WhateverPlayer.
     * The player randomly selects a position on the board and places its mark.
     * If the selected position is invalid or already occupied, it continues to
     * randomly select positions until a valid move is made.
     *
     * @param board The game board on which the player will place its mark.
     * @param mark The mark of the player.
     */
    @Override
    public void playTurn(Board board, Mark mark) {
        Random random = new Random();
        int row = random.nextInt(board.getSize());
        int col = random.nextInt(board.getSize());
        // Check if the move is valid
        while(!board.putMark(mark,row,col)){
            row = random.nextInt(board.getSize());
            col = random.nextInt(board.getSize());
        }
    }
}
