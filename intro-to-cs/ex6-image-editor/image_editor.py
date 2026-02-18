#################################################################
# FILE : image_editor.py
# WRITER : Tomer_Kadosh , tomer_kadosh , 209460005
# EXERCISE : intro2cs ex6 2024
# DESCRIPTION: A simple program that edit photos
# STUDENTS I DISCUSSED THE EXERCISE WITH:
# WEB PAGES I USED:
# NOTES: ...
#################################################################
import math
from typing import List

import ex6_helper
##############################################################################
#                                   Imports                                  #
##############################################################################
from ex6_helper import *

import sys


##############################################################################
#                                  Functions                                 #
##############################################################################


def separate_channels(image: ColoredImage) -> List[SingleChannelImage]:
    """function that maks 3 different color picture from 3 colored picture"""
    separate_k = []  # what about one chunel?
    for k in range(len(image[0][0])):
        separate_k.append([])
        for i in range(len(image)):
            separate_k[k].append([])
            for j in range(len(image[0])):
                separate_k[k][i].append(image[i][j][k])
    return separate_k


def combine_channels(channels: List[SingleChannelImage]) -> ColoredImage:
    """function that combines 3 different color picture from 3 colored picture"""
    combined = []
    for i in range(len(channels[0])):
        combined.append([])
        for j in range(len(channels[0][0])):
            combined[i].append([])
            for k in range(len(channels)):
                combined[i][j].append(channels[k][i][j])
    return combined


def RGB2grayscale(colored_image: ColoredImage) -> SingleChannelImage:
    """"converts RGB image to grayscale"""
    grayscale_image = []
    for i in range(len(colored_image)):
        grayscale_image.append([])
        for j in range(len(colored_image[0])):
            average_rgb = colored_image[i][j][0] * 0.299 + \
                          colored_image[i][j][1] * 0.587 + colored_image[i][j][2] * 0.114
            average_rgb = round(average_rgb)
            grayscale_image[i].append(average_rgb)
    return grayscale_image


def blur_kernel(size: int) -> Kernel:  ############
    """creat kernel for blurring or some other uses"""
    return [[1 / pow(size, 2) for j in range(size)] for i in range(size)]


def apply_kernel(image: SingleChannelImage, kernel: Kernel) -> SingleChannelImage:
    """loop over the image and apply the kernel"""
    row = len(image)
    col = len(image[0])
    new_image = []
    range_check = len(kernel) // 2
    #  looping over the given image
    for i in range(row):
        new_image.append([])
        for j in range(col):
            sum_of_kernel = 0
            #  looping over evry coordinate un the image
            for k in range(len(kernel)):
                for l in range(len(kernel[0])):

                    y = i + k - range_check
                    x = j + l - range_check
                    # check for out of range
                    if 0 <= y < row and 0 <= x < col:
                        sum_of_kernel += image[y][x] * kernel[k][l]
                    #  if out of range multipling in curent [i][j]
                    else:
                        sum_of_kernel += image[i][j] * kernel[k][l]
            sum_of_kernel = round(sum_of_kernel)  #rounding and makes sure 0<value<255
            if sum_of_kernel < 0:
                sum_of_kernel = 0
            elif sum_of_kernel > 255:
                sum_of_kernel = 255
            new_image[i].append(sum_of_kernel)

    return new_image


def bilinear_interpolation(image: SingleChannelImage, y: float, x: float) -> int:
    """formula that gives bilinear interpolation"""
    #  creathbg the four coordinats closest
    y_s = math.floor(y)
    x_s = math.floor(x)
    # checking for range
    if y == len(image) - 1:
        y_e = y_s
    else:
        y_e = y_s + 1
    if x == len(image[0]) - 1:
        x_e = x_s
    else:
        x_e = x_s + 1
    # sign the coordinates
    a = image[y_s][x_s]
    b = image[y_e][x_s]
    c = image[y_s][x_e]
    d = image[y_e][x_e]
    #  delta
    delta_y = y - y_s
    delta_x = x - x_s

    new_val = a * (1 - delta_y) * (1 - delta_x) + b * delta_y * (1 - delta_x) + c * delta_x * (
            1 - delta_y) + d * delta_y * delta_x
    new_val = round(new_val)
    return new_val


def resize(image: SingleChannelImage, new_height: int, new_width: int) -> SingleChannelImage:  ############
    """function that resizes the image"""
    new_image = []
    for i in range(new_height):

        new_image.append([])
        #  relitive loction
        y = float((i / (new_height - 1)) * (len(image) - 1))
        for j in range(new_width):
            x = float((j / (new_width - 1)) * (len(image[0]) - 1))
            # givs th corners same value as original
            if i == new_height - 1 and j == new_width - 1:
                new_val = image[len(image) - 1][len(image[0]) - 1]
            elif i == new_height - 1 and j == 0:
                new_val = image[len(image) - 1][j]
            elif i == 0 and j == new_width - 1:
                new_val = image[0][len(image[0]) - 1]
            elif i == 0 and j == 0:
                new_val = image[i][j]
            else:
                new_val = bilinear_interpolation(image, y, x)
            new_image[-1].append(new_val)

    return new_image


def rotate_90(image: Image, direction: str) -> Image:
    """rotate image by 90 degrees"""
    if direction == "R":
        return [[image[len(image) - 1 - i][j] for i in range(len(image))] for j in
                range(len(image[0]))]
    elif direction == "L":
        return [[image[i][len(image[0]) - 1 - j] for i in range(len(image))] for j in
                range(len(image[0]))]


def get_edges(image: SingleChannelImage, blur_size: int, block_size: int,
              c: float) -> SingleChannelImage:  ################
    """convret image to edges of black and white image"""
    # bluring the image
    blured_kernel = blur_kernel(blur_size)
    blurred_image = apply_kernel(image, blured_kernel)
    #  get an averge of the colore srouding
    avarege_kernel = blur_kernel(block_size)
    avarege_image = apply_kernel(blurred_image, avarege_kernel)
    for i in range(len(image)):
        for j in range(len(image[0])):
            if blurred_image[i][j] < avarege_image[i][j] - c:
                avarege_image[i][j] = 0
            else:
                avarege_image[i][j] = 255
    new_image = avarege_image
    return new_image


def quantize(image: SingleChannelImage, N: int) -> SingleChannelImage:  ############
    """function that quantizes black and white image"""
    new_image = []
    for i in range(len(image)):
        new_image.append([])
        for j in range(len(image[0])):
            qimg = round(math.floor(image[i][j] * (N / 256)) * (255 / (N - 1)))
            new_image[i].append(qimg)
    return new_image


def quantize_colored_image(image: ColoredImage, N: int) -> ColoredImage:  #########
    """function that quantizes colored image"""
    quant_image = []
    separated_colores = separate_channels(image)
    for i in separated_colores:
        quant_image.append(quantize(i, N))
    return combine_channels(quant_image)


def get_input():
    """helper function to get user input for the requsted operrate and check validity"""
    while True:
        user_input = input("1.Convert RGB to grayscale"
                           "\n2.blur image"
                           "\n3.resize image"
                           "\n4.rotate image 90 deg"
                           "\n5.get edges"
                           "\n6.quantize image"
                           "\n7.show image"
                           "\n8.quit program"
                           "\nchoose: ")
        if user_input not in "12345678":
            print("Unsupported input please enter a number between 1 and 8")
            continue
        else:
            return user_input


def natural_number(string: str) -> int:
    """"checks if string is number and positive and natural"""
    if string.isdigit() and int(string) > 0:
        return True
    else:
        return False


def is_float(string: str):
    try:
        float(string)
        return True
    except:
        return False


def main():
    """main function for runing the program"""
    #  checks for one argument
    if len(sys.argv) != 2:
        print("not allowed more then one object or less")
        return
    image = ex6_helper.load_image(sys.argv[1])
    #  main loop
    while True:
        operate = get_input()
        # convert RGB to grayscale
        if operate == "1":

            if isinstance(image[0][0], list):
                image = RGB2grayscale(image)

            else:
                print("picture already converted")


        elif operate == "2":  # blur the image RGB and grayscale

            kernel_size = input("enter kernel size")
            if natural_number(kernel_size):  # check int for kernel size
                kernel_size = int(kernel_size)
                if kernel_size % 2 != 0:

                    kernel = blur_kernel(kernel_size)
                    if isinstance(image[0][0], list):  # RGB
                        channels = separate_channels(image)

                        for i in range(len(channels)):
                            channels[i] = apply_kernel(channels[i], kernel)
                        image = combine_channels(channels)
                    else:  #  grayscale
                        image = apply_kernel(image, kernel)
                    continue
            print("invalid input make sure for natural odd number")

        elif operate == "3":  # resizing
            new_size = input("pleas input and in the folowing format: (new width,new hight)")
            if "," in new_size:  #  checks format validity
                new_size = new_size.split(",")
                if len(new_size) == 2 and natural_number(new_size[0]) and natural_number(new_size[1]):
                    hight, width = new_size
                    hight = int(hight)
                    width = int(width)
                    if hight != 1 and width != 1:  # biger than one

                        if isinstance(image[0][0], list):  # RGB
                            channels = separate_channels(image)

                            for i in range(len(channels)):
                                channels[i] = resize(channels[i], hight, width)
                            image = combine_channels(channels)

                        else:  # grayscale
                            image = resize(image, hight, width)

                        continue
            print("invalid input pleas try again at the requested format")

        elif operate == "4":  # rotate image
            direction = input("pleas input L or R:")  # choosing direction
            if direction in ["L", "R"]:
                image = rotate_90(image, direction)
            else:
                print("try again with L or R only")

        elif operate == "5":  # get edges
            input_edges = input("pleas enter (blur_size, block_size, c):")
            if "," in input_edges:  # checking format
                input_edges = input_edges.split(",")
                if len(input_edges) == 3:

                    blur_size, block_size, c = input_edges
                    if natural_number(blur_size) and natural_number(block_size) and is_float(c):
                        blur_size = int(blur_size); block_size = int(block_size); c = eval(c)
                        if blur_size % 2 != 0 and block_size % 2 != 0 and c >= 0:
                            if isinstance(image[0][0], list): #  RGB
                                image = RGB2grayscale(image)
                                image = get_edges(image, blur_size, block_size, c)
                            else:  # grayscale
                                image = get_edges(image, blur_size, block_size, c)
                            continue
            print("invalid input pleas try again")

        elif operate == "6":  # change variety number
            variety_num = input("pleas enter number of variety")
            if natural_number(variety_num) and int(variety_num) > 1:
                if isinstance(image[0][0], list): # RGB
                    image = quantize_colored_image(image, int(variety_num))
                else:  #grayscale
                    image = quantize(image, int(variety_num))
            else:
                print("invalid input")

        elif operate == "7": # showing image using helper
            ex6_helper.show_image(image)
        elif operate == "8":  # saving image after editing end quit program
            ex6_helper.save_image(image, input("enter path: "))
            quit()


if __name__ == '__main__':
    main()
