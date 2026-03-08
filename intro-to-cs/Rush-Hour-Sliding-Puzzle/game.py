import helper
from board import Board
from car import Car

from helper import load_json
import sys


KEY_DIRECTIONS = ["l", "r", "u", "d"]
LEGAL_NAME = ["Y", " B", "O", "G", "W", "R"]
POSITION=[0,1]
CAR_LENGTH = [2,3,4]



class Game:
    """
    This class is managing the game logic
    by aplying the car and board class
    """

    def __init__(self, board: Board) -> None:
        """
        Initialize a new Game object.
        :param board: An object of type board
        """
        self.__board = board

        # You may assume board follows the API
        # implement your code and erase the "pass"

    def __single_turn(self)-> bool:
        """
        Note - this function is here to guide you and it is *not mandatory*
        to implement it.

        The function runs one round of the game :
            1. Get user's input of: what color car to move, and what
                direction to move it.
            2. Check if the input is valid.
            3. Try moving car according to user's input.

        Before and after every stage of a turn, you may print additional
        information for the user, e.g., printing the board. In particular,
        you may support additional features, (e.g., hints) as long as they
        don't interfere with the API.
        single turn return falls if the game should stop
        """

        print(self.__board.__str__()) # prints the board
        player_input = input("input (car name , direction) in this format: ")# get user input and checks validity
        print(player_input)
        if player_input == "!":  # stop the game
            print("game over AS requested")
            return False
        player_input = player_input.split(",")
        if len(player_input) == 2:
            car_color, direction = player_input
            if car_color in LEGAL_NAME and direction in KEY_DIRECTIONS: # checks move validity
                for move in self.__board.possible_moves():
                    if move[0] == car_color and move[1] == direction:
                        if self.__board.move_car(car_color, direction):
                            return True
            print("name and direction not supported") # if input invalid
            return True

        else:
            print("invalid input")
            return True

    def play(self) -> None:
        """
        The main driver of the Game. Manages the game until completion.
        :return: None
        """
        target_e = self.__board.target_location()
        while True:

            if len(self.__board.possible_moves()) == 0:  # checks if there is moves to play

                break

            if self.__board.cell_content(target_e) is not None:  #if ther is a car in the target

                print("win!")
                break
            if self.__single_turn():
                continue
            else:
                print(self.__board.__str__())
                break

def create_game(args) -> Game:

    """
    This function creates a new Game object.
    place the car on board and pass if not posible
    :return Game: A new Game object.
    """
    board = Board()

    config_board = helper.load_json(sys.argv[1])

    for car_name, car_data in config_board.items():
        if car_name not in LEGAL_NAME: # checks for name validity
            continue
        if (car_data[1][0], car_data[1][1]) not in board.cell_list(): # checks for location validity
            continue
        if car_data[0] not in CAR_LENGTH: # checks for length
            continue
        if car_data[2] not in POSITION: # check orintetion
            continue
        car = Car(car_name, car_data[0], (car_data[1][0], car_data[1][1]), car_data[2])
        if not board.add_car(car): # try adding the car
            continue
    return Game(board)







if __name__ == "__main__":
    # Your code here
    # All access to files, non API constructors, and such must be in this
    # section, or in functions called from this section.
    # implement your code and erase the "pass"
    game = create_game(sys.argv[1])
    game.play()



