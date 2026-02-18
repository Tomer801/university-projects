/**
 * A enum for the Tic Tac Toe board marks.
 * @author Tomer Kadosh
 * @see Board
 */
public enum Mark {
    X , O, BLANK;
    /**
     * Returns the mark.
     * @return the mark
     */
    public String toString(){
        if (this == X) {
            return "X";
        } else if (this == O) {
            return "O";
        } else {
            return null;
        }
    }
}