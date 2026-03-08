#################################################################
# FILE : battleship.py
# WRITER : Tomer Kadosh , tomer_kadosh , 209460005
# EXERCISE : intro2cs ex5 2024
# DESCRIPTION: program that find words in matrix
# STUDENTS I DISCUSSED THE EXERCISE WITH: no one
# WEB PAGES I USED: none
# NOTES: ...
#################################################################
import sys
import os


def read_wordlist(filename):
    """ this function get txt file and return string list"""
    with open(filename, "r") as file:
        new_list = [line[:-1] for line in file.readlines()]

        return new_list


def read_matrix(filename):
    """creat list of lists as mat"""
    with open(filename, "r") as file:
        file_line = file.readlines()
        mat_list = [[j for j in line[0::2]] for line in file_line]

        return mat_list


def search_right_left(matrix):
    """helper function that make list of strings from matrix from right to left"""
    list_search = []
    for i in range(len(matrix)):
        str1 = ""
        for j in range(len(matrix[0])):
            str1 += matrix[i][j]
        list_search.append(str1)

    return list_search


def search_up_down(matrix):
    """helper function that make list of strings from matrix from top to bottom"""
    list_search = []
    for j in range(len(matrix[0])):
        str1 = ""
        for i in range(len(matrix)):
            str1 += matrix[i][j]
        list_search.append(str1)
    return list_search


def search_diagonal1(matrix):
    """helper function that make list of strings from matrix from top left corner as up right"""
    list_search = []
    for i in range(len(matrix)):
        str1 = ""
        j = 0
        while j <= i and j < len(matrix[0]):
            str1 += matrix[i - j][j]
            j += 1
        list_search.append(str1)
    for j in range(1, len(matrix[0])):
        str1 = ""
        i = len(matrix) - 1
        while j < len(matrix[0]) and i >= 0:
            str1 += matrix[i][j]
            i -= 1
            j += 1
        list_search.append(str1)
    return list_search


def search_diagonal2(matrix):
    """helper function that make list of strings from matrix from top right corner as up left"""
    list_search = []

    for i in range(len(matrix)):
        str1 = ""
        j = 0
        while j <= i and j < len(matrix[0]):
            str1 += matrix[i - j][len(matrix[0]) - 1 - j]
            j += 1
        list_search.append(str1)

    for j in range(len(matrix[0]) - 2, -1, -1):
        str1 = ""
        i = len(matrix) - 1
        while j >= 0 and i >= 0:
            str1 += matrix[i][j]
            i -= 1
            j -= 1
        list_search.append(str1)
    return list_search


def reverse_matrix(mat_string):
    """helper function that revers the list of strings that it got from all
       the helper function that read the matrix from evry direction """
    opposite_direction = []
    for i in mat_string:
        reversed_i = i[::-1]
        opposite_direction.append(reversed_i)
    return opposite_direction


def string_by_direction(matrix, directions):
    """helper function that get matrix and directions as
       parameters and uses the function above to return
       lists by the requsted order"""
    directions = set(list(directions))
    list_of_all_directions = []
    if "r" in directions:
        list_of_all_directions.append(search_right_left(matrix))
    if "l" in directions:
        list_of_all_directions.append(reverse_matrix(search_right_left(matrix)))
    if "d" in directions:
        list_of_all_directions.append(search_up_down(matrix))
    if "u" in directions:
        list_of_all_directions.append(reverse_matrix(search_up_down(matrix)))
    if "w" in directions:
        list_of_all_directions.append(search_diagonal1(matrix))
    if "z" in directions:
        list_of_all_directions.append(reverse_matrix(search_diagonal1(matrix)))
    if "x" in directions:
        list_of_all_directions.append(search_diagonal2(matrix))
    if "y" in directions:
        list_of_all_directions.append(reverse_matrix(search_diagonal2(matrix)))

    return list_of_all_directions


def find_words(wordlist, matrix, directions):
    """get list of words and matrix and directions as str
      and return the words found and how much """
    words_found_count = []
    if not matrix or not wordlist:  # checks for empty files
        return words_found_count
    list_s = string_by_direction(matrix, directions)

    for word in wordlist:  #  run evry word by evry direction requsted

        count = 0
        for i in list_s:
            for j in i:
                if word in j:
                    check_again = j[j.index(word) + 1:]

                    while True: # make sure to finds the words taht may apear more then once
                        count += 1
                        if word in check_again:
                            check_again = check_again[check_again.index(word) + 1:]
                            continue
                        else:
                            break
        if count > 0:
            words_found_count.append((word, count))
    return words_found_count


def write_output(result, filename):
    """creat or rewrite the output file"""
    with open(filename, "w") as file:
        for i in result:
            file.write(i[0] + "," + str(i[1]))
            file.write("\n")
    return filename


def check_input():
    """helper function that checks if the user input is valid"""
    direction_leters = "udrlwxyz"

    if len(sys.argv) != 5:  # right nunber of parameters
        print("Program get only 4 parameters")
        return False
    if not os.path.exists(sys.argv[1]):  # existing of file
        print("file word_list dosnt exsist")
        return False
    if not os.path.exists(sys.argv[2]): # # existing of file
        print("file matrix dosnt exsist")
        return False

    for i in sys.argv[4]:  # checks for valid direction
        if i not in direction_leters:
            print("Invalid direction")
            return

    return True


def main():
    """main function for the program that get parametrs from the cmd"""
    if not check_input():  # checks for valid input
        return
    wordlist = read_wordlist(sys.argv[1])
    matrix = read_matrix(sys.argv[2])
    directions = sys.argv[4]

    final_list = find_words(wordlist, matrix, directions)

    write_output(final_list, sys.argv[3])



if __name__ == "__main__":
    main()
