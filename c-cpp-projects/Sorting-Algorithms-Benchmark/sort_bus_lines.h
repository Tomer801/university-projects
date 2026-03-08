#ifndef EX2_REPO_SORTBUSLINES_H
#define EX2_REPO_SORTBUSLINES_H
// write only between #define EX2_REPO_SORTBUSLINES_H and #endif //EX2_REPO_SORTBUSLINES_H
#include <string.h>
#define NAME_LEN 21
/**
 * bus struct including name distance and duration
 */
typedef struct BusLine {
    char name[NAME_LEN];
    int distance, duration;
} BusLine;

typedef enum SortType {
    DISTANCE,
    DURATION
} SortType;

/**
 * sorting the array by name using bubble sort
 */
void bubble_sort(BusLine *start, BusLine *end);

/**
 * sorting the array by distance or duration using quick sort
 */
void quick_sort(BusLine *start, BusLine *end, SortType sort_type);

/**
 * helper function to quick sort that split the array by the pivot
 */
BusLine *partition(BusLine *start, BusLine *end, SortType sort_type);

/*
 * helper to switch places in struct array
 */
void Xchange(BusLine *leftp, BusLine *rightp);


#endif //EX2_REPO_SORTBUSLINES_H
