# Sorting Algorithms Benchmark — Bus Lines

A C program demonstrating and validating two classic sorting algorithms (bubble sort and quicksort) on structured records, with a full correctness-verification test suite.

---

## Problem Solved

Parse a sequence of bus-line records from stdin and sort them by different criteria — lexicographic name, numeric distance, or numeric duration — using the algorithm best suited to each case. Validate every sort result with automated post-condition checks.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Algorithm selection per sort key | Bubble sort for lexicographic (stable, natural comparator); quicksort for numeric keys (O(n log n) average) |
| Reusable partition function | `partition()` accepts a comparator function pointer, making it reusable for both distance and duration keys without code duplication |
| Input validation | Re-prompts on malformed lines; enforces range constraints (distance 0–1000, duration 10–100) |
| Test correctness without a framework | `is_sorted_by_*` and `is_equal` helpers verify sort post-conditions; prints `PASSED`/`FAILED` per case |

---

## Data Model

```c
typedef struct {
    char name[MAX_LINE_LEN]; // up to 20 chars, lexicographic sort key
    int  distance;           // integer, 0 – 1000
    int  duration;           // integer, 10 – 100
} BusLine;
```

---

## Algorithms

| Mode | Algorithm | Average Complexity |
|---|---|---|
| `by_name` | Bubble sort (stable, lexicographic) | O(n²) |
| `by_distance` | Quicksort with comparator | O(n log n) |
| `by_duration` | Quicksort with comparator | O(n log n) |

---

## Tech Stack & Concepts

- **Language:** C (C11)
- **Build:** `gcc -std=c11 -Wall -Wextra -Werror -O2`
- **Key concepts:** Comparator function pointers, in-place sorting, input validation, unit testing in plain C

---

## Build & Run

```bash
gcc -std=c11 -Wall -Wextra -Werror -O2 main.c sort_bus_lines.c test_bus_lines.c -o buslines

./buslines by_name       # lexicographic bubble sort
./buslines by_distance   # numeric quicksort
./buslines by_duration   # numeric quicksort
./buslines test          # run full validation suite
```

**Input format (stdin, CSV):**
```
3
line1,100,20
line2,200,50
line3,150,30
```
