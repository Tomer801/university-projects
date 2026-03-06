#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
#include "sort_bus_lines.h"
#include "test_bus_lines.h"

#define ARG_COUNT 2
#define ARG_1   argv[1]

#define MAX_OBJ 20
#define MAX_LINE_LEN 60

#include <stdio.h>
#include "sort_bus_lines.h"


void print_bus_lines(const BusLine *arr, int size) {
    for (int i = 0; i < size; i++) {
        printf("%s,%d,%d\n",
                 arr[i].name, arr[i].distance, arr[i].duration);
    }
}


int check_input_line_number(const char *buffer, BusLine *bus_line) {
    // Parse the input string
    if (sscanf(buffer, "%[^,],%d,%d", bus_line->name, &bus_line->distance, &bus_line->duration) != 3) {
        char *ptr;
        char *first_comma = strchr(buffer, ',');
        char *second_comma = strrchr(buffer, ',');
        *second_comma = '\0';
        strtol(first_comma, &ptr , 10);
        if (*ptr != '\0') {
            printf("Error: distance should be an integer between 0 and 1000 (includes)\n");
        }
        return EXIT_FAILURE;
    }

    // Check the name's validity
    int name_length = 0;
    for (int i = 0; bus_line->name[i] != '\0'; i++) {
        if (!(islower(bus_line->name[i]) || isdigit(bus_line->name[i]) || bus_line->name[i] == ' ')) {
            printf("Error: bus name should contains only digits and small chars\n");
            return EXIT_FAILURE;
        }

        // Count only non-space characters
        if (bus_line->name[i] != ' ') {
            name_length++;
        }
    }

    if (name_length > 20) {
        printf("Error: Name is too long. Maximum length is 20 non-space characters.\n");
        return EXIT_FAILURE;
    }

    // Check the distance's validity
    if (bus_line->distance < 0 || bus_line->distance > 1000 || (bus_line->distance % 1) != 0) {

        printf("Error: distance should be an integer between 0 and 1000 (includes)\n");
        return EXIT_FAILURE;
    }

    // Check the duration's validity
    if (bus_line->duration < 10 || bus_line->duration > 100) {
        printf("Error: duration should be an integer between 10 and 100 (includes)\n");
        return EXIT_FAILURE;
    }

    // If all checks pass
    return EXIT_SUCCESS;
}

void tests_c(const BusLine *start, const BusLine *end) {
    int size = end - start;
    BusLine *sorted_copy = malloc(sizeof(BusLine) * size);
    if (sorted_copy == NULL) {
        return;
    }
    // Copy the original array
    for (int i = 0; i < size; i++) {
        sorted_copy[i] = start[i];
    }


    // Test 1: Sort by distance
    quick_sort(sorted_copy, sorted_copy + size, DISTANCE);


    if (!is_sorted_by_distance(sorted_copy, sorted_copy + size)) {
        printf("TEST 1 FAILED: Not sorted by distance\n");
    } else {
        printf("TEST 1 PASSED: The array is sorted by distance\n");
    }
    if (!is_equal(sorted_copy, sorted_copy + size, start, end)) {
        printf("TEST 2 FAILED: Arrays are not equal after sorting by distance\n");
    } else {
        printf("TEST 2 PASSED: The array has the same items after sorting\n");
    }

    // Test 2: Sort by duration
    quick_sort(sorted_copy, sorted_copy + size, DURATION);


    if (!is_sorted_by_duration(sorted_copy, sorted_copy + size)) {
        printf("TEST 3 FAILED: Not sorted by duration\n");
    } else {
        printf("TEST 3 PASSED: The array is sorted by duration\n");
    }
    if (!is_equal(sorted_copy, sorted_copy + size, start, end)) {
        printf("TEST 4 FAILED: Arrays are not equal after sorting by duration\n");
    } else {
        printf("TEST 4 PASSED: The array has the same items after sorting\n");
    }
    // Test 3: Sort by name
    bubble_sort(sorted_copy, sorted_copy + size);


    if (!is_sorted_by_name(sorted_copy, sorted_copy + size)) {
        printf("TEST 5 FAILED: Not sorted by name\n");
    } else {
        printf("TEST 5 PASSED: The array is sorted by name\n");
    }
    if (!is_equal(sorted_copy, sorted_copy + size, start, end)) {
        printf("TEST 6 FAILED: Arrays are not equal after sorting by name\n");
    } else {
        printf("TEST 6 PASSED: The array has the same items after sorting\n");
    }

    // Clean up
    free(sorted_copy);
}
/*
 *choosing what to use by cli
 */
void excute_program(char argv[], BusLine *array, const int size) {

    if (!strcmp(argv, "by_duration")) {
        quick_sort(array, array + size, DURATION);
        print_bus_lines(array,size);
    }

    if (!strcmp(argv, "by_distance")) {
        quick_sort(array, array + size, DISTANCE);
        print_bus_lines(array,size);
    }
    if (!strcmp(argv, "test")) {
        tests_c(array, array + size);
    }
    if (!strcmp(argv, "by_name")) {
        bubble_sort(array, array + size);
        print_bus_lines(array,size);
    }
}

/**
 * main function
 */
int main(int argc, char *argv[]) {
    //checks if the input is one arg
    if (argc != ARG_COUNT) {
        //print err and exit prog
        printf("Usage: pleas insert one arg ");
        return EXIT_FAILURE;
    }
    //check if the input is supported
    if (!strcmp(ARG_1, "by_duration") && !strcmp(ARG_1, "by_distance") &&
        !strcmp(ARG_1, "test") && !strcmp(ARG_1, "by_name")) {
        //if not print err and exit prg
        printf("Usage: pleas insert supported cmd ");
        return EXIT_FAILURE;
    }
    int num; // var to keep number of lines
    while (1) {
        //loop to get valid input of lines
        fprintf(stdout, "Enter number of lines. Then enter\n");
        char input[MAX_LINE_LEN];
        fgets(input, MAX_LINE_LEN, stdin);
        if (!(sscanf(input, "%d", &num) == 1) || num <= 0) {
            printf("Error: Number of lines should be a positive integer\n");
            continue;
        }
        break;
    }
    BusLine *arr = calloc(num, sizeof(BusLine)); //alocate space in heap for arrey
    if (arr == NULL) {
        return EXIT_FAILURE;
    }
    int i = 0;
    while (i < num) {
        printf("Enter line info. Then enter\n");
        char input2[MAX_LINE_LEN];
        fgets(input2, MAX_LINE_LEN, stdin);

        if (!(check_input_line_number(input2, arr + i))) {
            i++;

        }
    }
    excute_program(ARG_1, arr, num);
    //free memory
    free(arr);
    return EXIT_SUCCESS;
}
