/**
 * Factory class for creating Player instances.
 * The PlayerFactory class provides a method to build different types of players
 * based on the specified type. It supports creating HumanPlayer, WhateverPlayer,
 * CleverPlayer, and GeniusPlayer instances.
 */
public class PlayerFactory {

    /**
     * Default constructor for the PlayerFactory class.
     */
    public PlayerFactory(){}
    /**
     * Builds a Player instance based on the specified type.
     * The method returns a new instance of the corresponding player type.
     * If the type is not recognized, it returns null.
     *
     * @param type The type of player to create. Supported types are:
     *             "human" - Creates a HumanPlayer.
     *             "whatever" - Creates a WhateverPlayer.
     *             "clever" - Creates a CleverPlayer.
     *             "genius" - Creates a GeniusPlayer.
     * @return A Player instance of the specified type, or null if the type is invalid.
     */
    public Player buildPlayer(String type){
        switch (type) {
            case "human":
                return new HumanPlayer();
            case "whatever":
                return new WhateverPlayer();
            case  "clever":
                return new CleverPlayer();
            case  "genius":
                return new GeniusPlayer();
            default:
                return null;
        }
    }
}
