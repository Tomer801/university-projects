#################################################################
# FILE : battleship.py
# WRITER : Tomer Kadosh , tomer_kadosh , 209460005
# EXERCISE : intro2cs ex4 2024
# DESCRIPTION: battle ship game
# STUDENTS I DISCUSSED THE EXERCISE WITH: no one
# WEB PAGES I USED: none
# NOTES: ...
#################################################################

import helper


def init_board(rows, columns):
    """create board by rows and columns"""
    board = [[helper.WATER for j in range(columns)] for i in range(rows)]
    return board


def user_input():
    """check if user input is valid by syntax and return the str if yes"""
    loc = helper.get_input("Enter your move: ")
    if len(loc) >= 2 and loc[0].isalpha() and helper.is_int(loc[1:]):  # not less than two and for digit and leters
        return loc
    else:
        return False


def cell_loc(name):
    """converts the str to coordinats  tupple
    checks for range and return them if in range"""

    loc = (int(name[1:]) - 1, ord(name[0].upper()) - 65)  # co
    if loc[0] in range(helper.NUM_ROWS):
        if loc[1] in range(helper.NUM_COLUMNS):
            return loc
    else:
        return False


def valid_ship(board, size, loc):
    """gets boars size and coordinats and checks if you can place the ship return True if it is"""
    a, b = loc
    if a in range(len(board)) and b in range(len(board[0])):  # coordinats in board?
        if a + size <= len(board):  # ship not crosing the board limit
            for i in range(size):
                if board[a + i][b] != helper.WATER:  # there isnt ship in this location
                    return False
            return True
        return False
    else:
        return False


def create_player_board(rows, columns, ship_sizes):
    """gets row columns and list of ship sizes and
    get input from the user where to place them"""
    board = init_board(rows, columns)
    for size in ship_sizes:
        while True:
            helper.print_board(board)  # prints the board
            name = user_input()  # asks for input
            if name and cell_loc(name) in posible_ship_loc(board,size):  # checks validty of location and if it posible to
                loc = cell_loc(name)                                                      # place ship by helper function loc
                for i in range(size):
                    board[loc[0] + i][loc[1]] = helper.SHIP  # place ship on board
                break  # braek the loop to go bake to for loop


            else:
                print("invalid input")  # let the user no that the input us not valid
    return board


def fire_torpedo(board, loc):
    """fire torpedo change the given coordinats for hit on water or ship"""
    if loc in posible_fire_loc(board):  # checks by helper function if you can fire in this coordinats
        if board[loc[0]][loc[1]] == helper.WATER:  # hit water if fire onn water
            board[loc[0]][loc[1]] = helper.HIT_WATER
            return board
        elif board[loc[0]][loc[1]] == helper.SHIP:  # hit ship if fire on ship
            board[loc[0]][loc[1]] = helper.HIT_SHIP
            return board
    else:
        return board


def posible_ship_loc(board, size):
    """helper founction that return set of posible ships location"""
    list_of_locations = []
    for q in range(len(board)):
        for w in range(len(board[q])):
            if valid_ship(board, size, (q, w)):  # checks with valid ship and put the location in the list if its valid
                list_of_locations.append((q, w))

    return set(list_of_locations)


def posible_fire_loc(board):
    """helper founction that return set of posible fire location"""
    cell_can_be_fired = []
    for a in range(len(board)):
        for s in range(len(board[a])):
            if board[a][s] == helper.WATER or board[a][
                s] == helper.SHIP:  # checks  board for the coordinats that is ship or water
                cell_can_be_fired.append((a, s))
    return set(cell_can_be_fired)


def creat_computer_board():
    """helper founction that creats board for the computer"""
    comp_board = init_board(helper.NUM_ROWS, helper.NUM_COLUMNS)
    for size in helper.SHIP_SIZES:
        set_of_loc = posible_ship_loc(comp_board, size)  # get posible location by helper function
        row, columns = helper.choose_ship_location(comp_board, size,
                                                   set_of_loc)  # choose randomly from the set where place ship
        for cell in range(size):  # place the ship
            comp_board[row + cell][columns] = helper.SHIP
    return comp_board


def check_for_result(board1, board2):
    """checks if the game over with bool values"""
    comp_board_ship = False
    p_board_ship = False

    for i in range(helper.NUM_ROWS):  # checks computer board for ship coordinats
        for j in range(helper.NUM_COLUMNS):
            if board1[i][j] == helper.SHIP:
                comp_board_ship = True

    for i in range(helper.NUM_ROWS):  # checks player board for ship coordinats
        for j in range(helper.NUM_COLUMNS):
            if board2[i][j] == helper.SHIP:
                p_board_ship = True
    return comp_board_ship, p_board_ship,


def keep_play(result):
    """declare who is the winner and asks the user for another game"""
    while True:
        ans = helper.get_input("The winner is {} do you want to play again?".format(result))
        if ans == "Y":
            return True
        elif ans == "N":
            return False
        else:
            continue


def hide_board(board):  # hides the comp board from the user
    hidden_board = init_board(helper.NUM_ROWS, helper.NUM_COLUMNS)
    for i in range(helper.NUM_ROWS):
        for j in range(helper.NUM_COLUMNS):
            hidden_board[i][j] = helper.WATER
    return hidden_board


def main():
    """main founction for the stream of the game"""
    while True:  # in case the list of ship sizes is empty
        if not helper.SHIP_SIZES:
            if keep_play("Tie"):
                continue
            else:
                return
        p_board = create_player_board(helper.NUM_ROWS, helper.NUM_COLUMNS, helper.SHIP_SIZES)  # board for player
        comp_board = creat_computer_board()  # board for computer
        hidden_board = hide_board(comp_board)  # hide the board
        helper.print_board(p_board, hidden_board)  # prints the boards
        while True:  #  flow of the game after placing the ships
            name = user_input()
            loc = cell_loc(name) # get user input for place to fire

            if name and loc in posible_fire_loc(comp_board):
                fire_torpedo(comp_board, loc)  # plyaer fire
                hidden_board[loc[0]][loc[1]] = comp_board[loc[0]][loc[1]] #  change copy in the same coordinats
                locations = posible_fire_loc(p_board) # in this part the computer is playing
                loc_c = helper.choose_torpedo_target(p_board, locations)
                fire_torpedo(p_board, loc_c)

                a, b = check_for_result(p_board, comp_board)  # checks if there is a winner and ending the game if needed
                if a and b:
                    helper.print_board(p_board, hidden_board)
                else:
                    helper.print_board(p_board, comp_board)
                    if not a and not b:
                        result = "Tie"
                    elif a and not b:
                        result = "player"
                    else:
                        result = "computer"
                    if keep_play(result): # want to play again?
                        break
                    else:
                        return
            else:
                print("invalid value")  # print unvalid value if the input is not valid
                continue


if __name__ == "__main__":
    main()
