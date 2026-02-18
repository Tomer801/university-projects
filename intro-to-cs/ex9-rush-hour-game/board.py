from typing import Tuple, List, Optional
from car import Car

Coordinates = Tuple[int, int]
VERTICAL = 0
HORIZONTAL = 1

class Board:
    """
    Add a class description here.
    Write briefly about the purpose of the class.
    """

    def __init__(self) -> None:
        """
        A constructor for a Board object.
        """

        board = []
        for i in range(7):
            if i != 3:
                board.append(7 * ["_"] + ["#"])
            else:
                board.append(7 * ["_"] + ["E"])
        self.__board = board
        self.cars = {}

        # Note that this function is required in your Board implementation.
        # implement your code and erase the "pass"

    def __str__(self) -> str:
        """
        This function is called when a board object is to be printed.
        :return: A string representing the current status of the board.
        """
        str_board = ""
        for i in self.__board:
            str_board += "[" + ",".join(i) + "]" + "\n"
        return str_board

        # The game may assume this function returns a reasonable representation
        # of the board for printing, but may not assume details about it.
        # implement your code and erase the "pass"

    def cell_list(self) -> List[Coordinates]:
        """
        This function returns the coordinates of cells in this board.
        :return: list of coordinates.
        """
        coord_list = [(3,7)]
        for i in range(7):
            for j in range(7):
                coord_list.append((i, j))

        return coord_list

        # In this board, returns a list containing the cells in the square
        # from (0,0) to (6,6) and the target cell (3,7)
        # implement your code and erase the "pass"


    def possible_moves(self) -> List[Tuple[str, str, str]]:
        """
        This function returns the legal moves of all cars in this board.
        :return: list of tuples of the form (name, move_key, description)
                 representing legal moves. The description should briefly
                 explain what is the movement represented by move_key.
        """
        list_moves = []

        for car in self.cars: # iterate the cars
            car = self.cars[car]
            if car.possible_moves() is None or car.possible_moves == {}:
                break
            for move_key,description in car.possible_moves().items():  # checking if the car set to move in tgis direction
                coord = car.movement_requirements(move_key)[0] # return the needed coord
                if coord in self.cell_list() and self.cell_content(coord) is None: # checks validity of the coord
                    list_moves.append((car.get_name(), move_key, description))

        return list_moves

        # From the provided example car_config.json file, the return value could be
        # [('O','d',"description"), ('R','r',"description"), ('O','u',"description")]
        # implement your code and erase the "pass"

    def target_location(self) -> Coordinates:
        """
        This function returns the coordinates of the location that should be
        filled for victory.
        :return: (row, col) of the goal location.
        """
        return 3, 7
        # In this board, returns (3,7)
        # implement your code and erase the "pass"

    def cell_content(self, coordinates: Coordinates) -> Optional[str]:
        """
        Checks if the given coordinates are empty.
        :param coordinates: tuple of (row, col) of the coordinates to check.
        :return: The name of the car in "coordinates", None if it's empty.
        """
        # implement your code and erase the "pass"
        if self.__board[coordinates[0]][coordinates[1]] in ["E", "_"]:
            return None
        else:

            return self.__board[coordinates[0]][coordinates[1]]

    def add_car(self, car: Car) -> bool:
        """
        Adds a car to the game.
        :param car: car object to add.
        :return: True upon success, False if failed.
        """


        coord_list = car.car_coordinates()  # the coord to place the car

        for coord in coord_list:
            if coord not in self.cell_list():
                return False
            if self.cell_content(coord) is not None:
                return False
        for key in self.cars:
            if key == car.get_name():
                return False


        for coord in coord_list: # place the car
            self.__board[coord[0]][coord[1]] = car.get_name()
        self.cars[car.get_name()] = car  # add to dict
        return True

        # Remember to consider all the reasons adding a car can fail.
        # You may assume the car is a legal car object following the API.
        # implement your code and erase the "pass"

    def move_car(self, name: str, move_key: str) -> bool:
        """
        Moves car one step in a given direction.
        :param name: name of the car to move.
        :param move_key: the key of the required move.
        :return: True upon success, False otherwise.
        """

        for car in self.cars:

            if car == name: # chose the car from dict
                car = self.cars[name]
                if move_key in car.possible_moves(): # check move validity
                    for i in self.possible_moves():
                        old_coord = car.car_coordinates()
                        if name == i[0] and i[1] == move_key and car.move(move_key):
                            # move the car by changing the old location to empty and updating the new
                            for j in old_coord:
                                self.__board[j[0]][j[1]] = "_"
                            for j in car.car_coordinates():
                                self.__board[j[0]][j[1]] = car.get_name()

                            return True
                return False

        return False


