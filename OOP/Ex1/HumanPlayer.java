/**
 * Represents a HumanPlayer in a Tic Tac Toe game.
 * The HumanPlayer interacts with the user to get input for their move.
 */
public class HumanPlayer implements Player  {
    /**
     * Default constructor for the HumanPlayer class.
     */
    HumanPlayer() {}
    /**
     * Plays a turn for the HumanPlayer.
     * The player is prompted to input coordinates for their move. The input is validated
     * to ensure it is within the bounds of the board and the position is not already occupied.
     * If the input is invalid, the player is prompted to try again.
     *
     * @param board The game board on which the player will place their mark.
     * @param mark The mark of the player.
     */
    @Override
    public void playTurn(Board board, Mark mark){
        System.out.println("Player " + mark + ", type coordinates: ");
        while (true) {
            int input = KeyboardInput.readInt();
            int row = input / 10; // row from the input
            int col = input % 10; // column from the input

            if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize()) {
                System.out.println("Invalid mark position. Please choose a valid position:");
                continue;
            }

            // Check if the move is valid
            if (!board.putMark(mark, row, col)) {
                System.out.println("Mark position is already occupied. Please choose a valid position: ");
                continue;
            }// Exit the loop if the move is valid
            break;
        }
    }
}
