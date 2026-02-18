import static java.lang.Integer.parseInt;
/**
 * Represents a Tournament in the Tic Tac Toe game.
 * The Tournament class manages multiple rounds of games between two players,
 * tracks the number of wins for each player, and displays the results.
 */
public class Tournament{
    private int rounds; // Number of rounds to be played in the tournament
    private Renderer renderer; // Renderer for displaying the game board
    private Player player1; // The first player in the tournament
    private Player player2; // The second player in the tournament

    /**
     * Constructs a Tournament with the specified number of rounds, renderer, and players.
     *
     * @param rounds The number of rounds to be played.
     * @param renderer The renderer used to display the game board.
     * @param player1 The first player in the tournament.
     * @param player2 The second player in the tournament.
     */
    Tournament(int rounds, Renderer renderer,
               Player player1, Player player2){
        this.rounds = rounds;
        this.renderer = renderer;
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Plays the tournament by running multiple games between the two players.
     * Alternates the starting player for each game and tracks the number of wins and ties.
     * Displays the results of the tournament at the end.
     *
     * @param size The size of the game board.
     * @param winStreak The number of marks in a row needed to win a game.
     * @param playerName1 The name of the first player.
     * @param playerName2 The name of the second player.
     */
    void playTournament(int size, int winStreak,
                        String playerName1, String playerName2){
        int player1Wins = 0; // Number of games won by player 1
        int player2Wins = 0; // Number of games won by player 2
        boolean isPlayer1X = true; // Tracks whether player 1 is 'X' in the current game
        int ties = 0; // Number of games that ended in a tie

        while (rounds > 0) {
            Game game;
            // Alternate the starting player for each game
            if (rounds % 2 == 0) {
                game = new Game(player1, player2, size,
                        winStreak, renderer);
            }else{
                game = new Game(player2, player1, size,
                        winStreak, renderer);
            }
            // Run the game and determine the winner
            Mark winner = game.run();

            if (winner == Mark.X) {
                if (isPlayer1X) {
                    player1Wins++;
                } else {
                    player2Wins++;
                }
            } else if (winner == Mark.O) {
                if (isPlayer1X) {
                    player2Wins++;
                } else {
                    player1Wins++;
                }
            } else if (winner == Mark.BLANK) {
                ties++;
            }
            isPlayer1X = !isPlayer1X;
            rounds--;
        }
        System.out.println("Player 1, " + playerName1 + " won: " + player1Wins +"\n"
                         + "Player 2, " + playerName2 + " won: " + player2Wins +"\n"
                         + "Ties: " + ties);

        }

    /**
     * The main method to run the Tournament.
     * Parses command-line arguments to configure the tournament and starts it.
     *
     * @param args Command-line arguments specifying the tournament configuration:
     *             args[0] - Number of rounds
     *             args[1] - Board size
     *             args[2] - Win streak
     *             args[3] - Renderer type
     *             args[4] - Player 1 type
     *             args[5] - Player 2 type
     */
    public static void main(String[] args){
        PlayerFactory playerFactory = new PlayerFactory();
        RendererFactory rendererFactory = new RendererFactory();
        // arguments: rounds, size, winStreak, rendererType, player1Type, player2Type
        int rounds = parseInt(args[0]);
        int size = parseInt(args[1]);
        int winStreak = parseInt(args[2]);
        String rendererType = args[3];
        String player1Type = args[4];
        String player2Type = args[5];
        // Create the renderer and players based on the provided types
        Renderer renderer = rendererFactory.buildRenderer(rendererType, size);
        // Create players based on the specified types
        Player player1 = playerFactory.buildPlayer(player1Type);
        Player player2 = playerFactory.buildPlayer(player2Type);
        //Creat and run tournament the tournament
        Tournament tournament = new Tournament(rounds, renderer,
                player1,
                player2);
        tournament.playTournament(size, winStreak,
                player1Type, player2Type);
    }
}
