/**
 * Represents a player in the Tic Tac Toe game.
 * The Player interface defines the contract for any player implementation,
 * requiring the implementation of the playTurn method.
 */
public interface Player {

    /**
     * Executes the player's turn in the game.
     * The implementation of this method should handle the logic for the player
     * to make a move on the given board using the specified mark.
     *
     * @param board The game board on which the player will place their mark.
     * @param mark The mark of the player .
     */
    void playTurn(Board board, Mark mark);
}