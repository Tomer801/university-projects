#include "test_bus_lines.h"
//TODO add implementation here
int is_sorted_by_distance (const BusLine *start, const BusLine *end) {
    int n = end - start;
    //looping over the array comparing name
    for (int i = 0; i < n-1; i++) {
        if (start[i].distance > start[i+1].distance) {
            return 0;

        }
    }return 1;
}


int is_sorted_by_duration (const BusLine *start, const BusLine *end) {
    //looping over the array comparing name
     int n = end - start;
    for (int i = 0; i < n-1; i++) {
        if (start[i].duration > start[i+1].duration) {
            return 0;

        }
    }return 1;
}

int is_sorted_by_name (const BusLine *start, const BusLine *end) {
    //looping over the array comparing name
    int n = end - start;
    for (int i = 0; i < n-1; i++) {
        if (strcmp(start[i].name , start[i+1].name) > 0) {
            return 0;
        }

    }return 1;

}
int is_equal (const BusLine *start_sorted,
              const BusLine *end_sorted,
              const BusLine *start_original,
              const BusLine *end_original)
{

 int n_sorted = end_sorted - start_sorted;
 int n_original = end_original - start_original;
    // check for the same number of elements
    if (n_sorted != n_original) {
        return 0;
    }
    // check that the names equal
    for (int i = 0; i < n_sorted; i++) {
        for (int j = 0; j < n_original; j++) {
            if (strcmp(start_sorted[i].name , start_original[j].name) == 0) {
                break;
            }
            if (j==n_original-1) {
                return 0;
            }
        }


    }return 1;
}
