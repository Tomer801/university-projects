from typing import Tuple, List, Dict

Coordinates = Tuple[int, int]


class Car:
    """
    Add a class description here.
    Write briefly about the purpose of the class.
    """



    def __init__(self, name: str, length: int, location: Coordinates,
                 orientation: int) -> None:
        """
        A constructor for a Car object.
        :param name: A string representing the car's name.
        :param length: A positive int representing the car's length.
        :param location: A tuple representing the car's head location (row,col).
        :param orientation: One of either 0 (VERTICAL) or 1 (HORIZONTAL).
        """
        # Note that this function is required in your Car implementation.
        # implement your code and erase the "pass"
       

        self.name = name
        self.length = length
        self.location = location
        self.orientation = orientation



    def car_coordinates(self) -> List[Coordinates]:
        """
        :return: A list of coordinates the car is in.
        """
        # creating the list by divison to vertical and horizantal
        car_coordinates_list = []
        if self.orientation == 0:
            for i in range(0, self.length):
                car_coordinates_list.append((self.location[0] + i, self.location[1]))

        elif self.orientation == 1:
            for i in range(0, self.length):

                car_coordinates_list.append((self.location[0], self.location[1] + i))
        return car_coordinates_list

    def possible_moves(self) -> Dict[str, str]:
        """
        :return: A dictionary of strings describing possible movements
                 permitted by this car.
        """
        if self.orientation == 0:
            return {"u": "because the car is vertical", "d": "because the car is vertical"}
        elif self.orientation == 1:
            return {"r": "because the car is horizontal", "l": "because the car is horizontal"}
        else:
            return {}

        # For this car type, keys are from 'udrl'
        # The keys for vertical cars are 'u' and 'd'.
        # The keys for horizontal cars are 'l' and 'r'.
        # You may choose appropriate strings to describe each movements.
        # For example: a car that supports the commands 'f', 'd', 'a' may return
        # the following dictionary:
        # {'f': "cause the car to fly and reach the Moon",
        #  'd': "cause the car to dig and reach the core of Earth",
        #  'a': "another unknown action"}
        #
        # implement your code and erase the "pass"

    def movement_requirements(self, move_key: str) -> List[Coordinates]:
        """
        :param move_key: A string representing the key of the required move.
        :return: A list of cell locations which must be empty in order for
                 this move to be legal.
        """

        if move_key == "u":
            return [(self.location[0] - 1, self.location[1])]
        if move_key == "d":
            return [(self.location[0] + self.length, self.location[1])]
        if move_key == "r":
            return [(self.location[0], self.location[1] + self.length)]
        if move_key == "l":
            return [(self.location[0], self.location[1] - 1)]

        # For example, a car in locations [(1,2),(2,2)] requires [(3,2)] to
        # be empty in order to move down (with a key 'd').
        # implement your code and erase the "pass"

    def move(self, move_key: str) -> bool:
        """
        This function moves the car.
        :param move_key: A string representing the key of the required move and move the car if posible.
        :return: True upon success, False otherwise
        """
        # implement your code and erase the "pass"

        if move_key == "u" and self.orientation == 0:
            self.location = (self.location[0] - 1, self.location[1])
            return True
        elif move_key == "d" and self.orientation == 0:
            self.location = (self.location[0] + 1, self.location[1])
            return True
        elif move_key == "r" and self.orientation == 1:
            self.location = (self.location[0], self.location[1]+1)

            return True
        elif move_key == "l" and self.orientation == 1:
            self.location = (self.location[0], self.location[1] - 1)
            return True
        return False




    def get_name(self) -> str:
        """
        :return: The name of this car.
        """

        return self.name
