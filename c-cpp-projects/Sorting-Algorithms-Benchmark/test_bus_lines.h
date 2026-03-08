#ifndef EX2_REPO_TESTBUSLINES_H
#define EX2_REPO_TESTBUSLINES_H
// write only between #define EX2_REPO_TESTBUSLINES_H and #endif //EX2_REPO_TESTBUSLINES_H
#include "sort_bus_lines.h"

/**
 * check if the array sort by distance was successful
 */
int is_sorted_by_distance (const BusLine *start, const BusLine *end);

/**
 * check if the array sort by duration was successful
 */
int is_sorted_by_duration (const BusLine *start, const BusLine *end);

/**
 * check if the array sort by name was successful
 */
int is_sorted_by_name (const BusLine *start, const BusLine *end);

/**
 * check if the array before and after sorting are identical
 * by using poinret to the sort an un sorted array
 */
int is_equal (const BusLine *start_sorted,
              const BusLine *end_sorted,
              const BusLine *start_original,
              const BusLine *end_original);
// write only between #define EX2_REPO_TESTBUSLINES_H and #endif //EX2_REPO_TESTBUSLINES_H
#endif //EX2_REPO_TESTBUSLINES_H
