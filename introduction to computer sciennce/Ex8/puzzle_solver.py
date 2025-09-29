#################################################################
# FILE : puzzle_solver.py
# WRITER : Tomer Kadosh , tomer_kadosh , 209460005
# EXERCISE : intro2cs ex8 2024
# DESCRIPTION:
# STUDENTS I DISCUSSED THE EXERCISE WITH: no one
# WEB PAGES I USED: 
# NOTES: ...
#################################################################






from typing import List, Tuple, Set, Optional

# We define the types of a partial picture and a constraint (for type checking).
Picture = List[List[int]]
Constraint = Tuple[int, int, int]


def max_seen_cells(picture: Picture, row: int, col: int) -> int:
    """
    finds the maximum number of seen cells in given temp picture.
     by counting -1 as 0
     """
    seen_cells = 0
    if picture[row][col] == 0:  # in case that the cell is 0 return 0
        return seen_cells
    for i in picture[row][col:len(picture[0])]: # check the coordinate and the right side
        if i == 1 or i == -1:
            seen_cells += 1
        else:
            break
    if col != 0:
        for i in picture[row][col - 1::-1]: # check the left side
            if i == 1 or i == -1:
                seen_cells += 1
            else:
                break
    if row != len(picture) - 1:
        for j in range(row + 1, len(picture)):
            if picture[j][col] == 1 or picture[j][col] == -1: # above the coordinate
                seen_cells += 1
            else:
                break
    if row != 0:
        for i in range(row - 1, -1, -1):
            if picture[i][col] == 1 or picture[i][col] == -1:  # benith the coordinate
                seen_cells += 1
            else:
                break
    return seen_cells


def min_seen_cells(picture: Picture, row: int, col: int) -> int:
    """
     finds the minimum number of seen cells in given temp picture.
     by counting -1 as 1
     """
    seen_cells = 0
    if picture[row][col] == 0 or picture[row][col] == -1:  # in case that the cell is 0 return 0
        return seen_cells
    for i in picture[row][col:len(picture[0])]:
        if i == 1: # check the coordinate and the right side
            seen_cells += 1
        else:
            break
    if col != 0:  # check the left side
        for i in picture[row][col - 1::-1]:
            if i == 1:
                seen_cells += 1
            else:
                break
    if row != len(picture) - 1: # above the coordinate
        for j in range(row + 1, len(picture)):
            if picture[j][col] == 1:
                seen_cells += 1
            else:
                break
    if row != 0: # benith the coordinate
        for i in range(row - 1, -1, -1):
            if picture[i][col] == 1:
                seen_cells += 1
            else:
                break
    return seen_cells


def check_constraints(picture: Picture, constraints_set: Set[Constraint]) -> int:
    """
        Checks if the given picture satisfies the constraints.
        Returns 1 if all constraints are strictly satisfied.
        Returns 2 if constraints can be satisfied with further filling.
        Returns 0 if constraints cannot be satisfied.
        """
    checker = True
    for constraint in constraints_set:
        min_seen = min_seen_cells(picture, constraint[0], constraint[1])
        max_seen = max_seen_cells(picture, constraint[0], constraint[1])
        # If the constraint value is outside the range of seen cells, return 0

        if constraint[2] < min_seen or constraint[2] > max_seen:
            return 0
        if checker:
            if min_seen != max_seen:
                checker = False
    if checker:
        return 1
    else:
        return 2


def check_for_early_sol(picture: Picture, constraints_set: Set[Constraint]) -> bool:
    """
     helper function to  check if the given picture satisfies the constraints
     befor cheking evry cell
     """
    bool_val = False
    for i in constraints_set:

        if i[2] == 0:  # If constraint is 0, the cell is black
            picture[i[0]][i[1]] = 0
        if i[2] != 0:  # If constraint is not 0, the cell is white
            picture[i[0]][i[1]] = 1
            if i[2] == 1: # If constraint is 1, set surrounding cells to black
                if i[0] > 0: picture[i[0] - 1][i[1]] = 0
                if i[0] < len(picture) - 1: picture[i[0] + 1][i[1]] = 0
                if i[1] > 0: picture[i[0]][i[1] - 1] = 0
                if i[1] < len(picture[0]) - 1: picture[i[0]][i[1] + 1] = 0
            if check_constraints(picture, constraints_set) == 0:
               return bool_val
    return not bool_val


def solve_puzzle(constraints_set: Set[Constraint], n: int, m: int) -> Optional[Picture]:
    """
        Solves the puzzle by filling the picture based on the given constraints.
        Returns the solved picture if a solution exists, otherwise returns None.
        """

    picture = [[-1] * m for _ in range(n)]  # Initialize the picture with -1 (unfilled cells)

    if not check_for_early_sol(picture, constraints_set):
       return
    if helper_solve_puzzle(picture, constraints_set, 0, 0):
       return picture
    return


def helper_solve_puzzle(temp_picture, constraints_set: Set[Constraint], i, j) -> Optional[bool]:
    """
        Recursive helper function for solving the puzzle.
        Returns True if a solution is found, otherwise False.
        """
    n = len(temp_picture)
    m = len(temp_picture[0])
    if j == m:
        j = 0
        i += 1
    if i == n:
        if check_constraints(temp_picture, constraints_set) == 0:
            return False
        else:
            return True

    if temp_picture[i][j] == -1:

        temp_picture[i][j] = 0 # Try black
        if check_constraints(temp_picture, constraints_set) != 0:
            if helper_solve_puzzle(temp_picture, constraints_set, i, j + 1):
                return True
        temp_picture[i][j] = 1  # Try white
        if check_constraints(temp_picture, constraints_set) != 0:
            if helper_solve_puzzle(temp_picture, constraints_set, i, j + 1):
                return True
        temp_picture[i][j] = -1  # Reset cell to unfilled if both attempts fail
        return False
    return helper_solve_puzzle(temp_picture, constraints_set, i, j + 1)



def helper_how_many_solutions(temp_picture, constraints_set: Set[Constraint], i:int, j:int) ->  Optional[int]:
    """
        Recursive helper function for counting the number of solutions to the puzzle.
        Returns the number of valid solutions.
        """
    n = len(temp_picture)
    m = len(temp_picture[0])
    if j == m:
        j = 0
        i += 1
    if i == n:

        if check_constraints(temp_picture, constraints_set) == 0:
            return 0
        if check_constraints(temp_picture, constraints_set) == 1:
            return 1
    solution_sum = 0
    if temp_picture[i][j] == -1:

        temp_picture[i][j] = 0  # Try black
        if check_constraints(temp_picture, constraints_set) != 0:
            solution_sum += helper_how_many_solutions(temp_picture, constraints_set, i, j + 1)
            temp_picture[i][j] = -1


        temp_picture[i][j] = 1 # Try white
        if check_constraints(temp_picture, constraints_set) != 0:
            solution_sum += helper_how_many_solutions(temp_picture, constraints_set, i, j + 1)
            temp_picture[i][j] = -1
        if check_constraints(temp_picture, constraints_set) == 0:
            temp_picture[i][j] = -1
            return solution_sum

        return solution_sum
    return helper_how_many_solutions(temp_picture, constraints_set, i, j + 1)


def how_many_solutions(constraints_set: Set[Constraint], n: int, m: int)->int:
    """
        Counts the number of valid solutions to the puzzle based on the given constraints.
        Returns the number of valid solutions.
        """
    picture = [[-1] * m for _ in range(n)] # Initialize the picture with -1 (unfilled cells)
    if not check_for_early_sol(picture, constraints_set): # if there is no solution
        return 0

    if helper_how_many_solutions(picture, constraints_set, 0, 0) > 0:
        return helper_how_many_solutions(picture, constraints_set, 0, 0)
    return 0



def generate_puzzle(picture: Picture) -> Set[Constraint]:
    return {(0,0,0)}
