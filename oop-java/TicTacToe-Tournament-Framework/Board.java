/**
 * Represents a Tic Tac Toe game board.
 * The board can be initialized with a default size or a custom size.
 * It provides methods to place marks on the board and retrieve marks at
 * specific positions.
 * @author Tomer Kadosh
 * @see Mark, Player, Game
 */
public class Board {
    /**Default Board size */
    static final int SIZE = 4;
    /** The size of the board field. */
    private final int size;
    private final Mark[][] board;


    /**
     * Default constructor for the Board class.
     * Initializes the board with the default size and sets all positions to BLANK.
     */
    public Board(){
        this.size = SIZE;
        this.board = new Mark[SIZE][SIZE];
        for(int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                board[i][j] = Mark.BLANK;
            }
        }
    }


    /**
     * Constructor for the Board class with a custom size.
     * Initializes the board with the specified size and sets all positions to BLANK.
     * @param size The size of the board.
     */
    public Board(int size) {
        this.size = size;
        this.board = new Mark[size][size];
        for (int i = 0; i < size; i++) { // Correct: using size
            for (int j = 0; j < size; j++) { // Correct: using size
                board[i][j] = Mark.BLANK;
            }
        }
    }
    /**
     * Retrieves the size of the board.
     * @return The size of the board.
     */
    public int getSize(){
        return size;
    }
    /**
     * Places a mark on the board at the specified position.
     * @param mark The mark to place.
     * @param row The row index where the mark should be placed.
     * @param col The column index where the mark should be placed.
     * @return True if the mark was successfully placed, false otherwise.
     */
    boolean putMark(Mark mark, int row, int col){
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        if (board[row][col] != Mark.BLANK) {
            return false;
        }
        board[row][col] = mark;
        return true;
    }

    /**
     * Retrieves the mark at the specified position on the board.
     * @param row The row index of the position.
     * @param col The column index of the position.
     * @return The mark at the specified position, or BLANK if the position is invalid.
     */
    Mark getMark(int row,int col){
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return Mark.BLANK;
        }
        return board[row][col];
    }
}
