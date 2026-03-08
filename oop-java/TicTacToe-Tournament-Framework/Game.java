/**
 * Represents the main game logic for a Tic Tac Toe game.
 * The Game class manages the players, board, renderer, and game rules.
 * It supports different board sizes and win streak configurations.
 */
public class Game {
    final static int DEFAULT_WIN_STREAK = 3; // Default number of marks in a row needed to win
    private Player playerX; // Player representing 'X'
    private Player playerO; // Player representing 'O'
    private Board board; // The game board
    private Renderer renderer; // Renderer for displaying the board
    private int winStreak; // Number of marks in a row needed to win
    private Mark turn; // Current player's turn (X or O)

    /**
     * Constructs a Game with default board size and win streak.
     *
     * @param playerX The player representing 'X'.
     * @param playerO The player representing 'O'.
     * @param renderer The renderer used to display the board.
     */
    Game(Player playerX,Player
            playerO, Renderer renderer){
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.board = new Board();
        this.winStreak = DEFAULT_WIN_STREAK;
        this.turn = Mark.X;
    }
    /**
     * Constructs a Game with a custom board size and win streak.
     *
     * @param playerX The player representing 'X'.
     * @param playerO The player representing 'O'.
     * @param size The size of the board.
     * @param winStreak The number of marks in a row needed to win.
     * @param renderer The renderer used to display the board.
     */
    Game(Player playerX,Player
            playerO, int size, int
                 winStreak,Renderer renderer){
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        this.board = new Board(size);
        this.winStreak = winStreak;
        this.turn = Mark.X;
    }
    /**
     * Gets the win streak required to win the game.
     *
     * @return The number of marks in a row needed to win.
     */
    public int getWinStreak() {
        return winStreak;
    }

    /**
     * Gets the size of the game board.
     *
     * @return The size of the board.
     */
    public int getBoardSize(){
        return board.getSize();
    }

    /**
     * Runs the game loop until there is a winner or the board is full.
     *
     * @return The mark of the winning player (X or O), or BLANK if the game is a draw.
     */
    Mark run(){
        int counter = 0;
        while (counter < getBoardSize() * getBoardSize()){
            // check if the game is over and someone won
            renderer.renderBoard(board);
            if (turn == Mark.X) {
                playerX.playTurn(board, turn);
            } else {
                playerO.playTurn(board, turn);
            }

            if (isWin(turn)) {
                return turn;
            }
            turn =(Mark.X== turn) ? Mark.O : Mark.X;
            counter++;
        }
        return Mark.BLANK;
    }
    /**
     * Checks if the specified mark has won the game.
     *
     * @param mark The mark to check for a win (X or O).
     * @return True if the specified mark has won, false otherwise.
     */
    private boolean isWin(Mark mark) {
        int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                if (board.getMark(i, j) != mark) {
                    continue;
                }
                for (int[] direction : directions) {
                    int rowDirection = direction[0];
                    int colDirection = direction[1];
                    if (checkStreak(i, j, rowDirection, colDirection)){
                        return true;
                    }

                }
            }
        }
        return false;
    }

    /**
     * Checks if there is a streak of marks starting from a given position in a specific direction.
     *
     * @param row The starting row of the streak.
     * @param col The starting column of the streak.
     * @param rowDirection The row direction to check.
     * @param colDirection The column direction to check.
     * @return True if a streak of the required length is found, false otherwise.
     */
    private boolean checkStreak(int row, int col, int rowDirection, int colDirection) {
        int currentStreak = 0;
        while(board.getMark(row, col) == turn) {
            row += rowDirection;
            col += colDirection;
            currentStreak++;
            if (currentStreak == getWinStreak()) {
                return true;
            }
        }
        return false;
    }
}
