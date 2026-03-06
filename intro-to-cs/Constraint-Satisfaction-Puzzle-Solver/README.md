# Constraint Satisfaction Puzzle Solver

A Python backtracking solver for a visibility-constraint logic puzzle, supporting both single-solution finding and exhaustive solution counting.

---

## Problem Solved

Given an n×m grid and a set of visibility constraints — each specifying that a cell at `(row, col)` must "see" exactly `k` white cells in its row and column — fill the grid with black (`0`) and white (`-1`) cells such that all constraints are satisfied.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Constraint evaluation on partial grids | `check_constraints()` returns 3 states: `0` (already violated), `1` (fully satisfied), `2` (still partial) — enabling early pruning during backtracking |
| Upper/lower bound estimation | `max_seen_cells()` counts all undecided + white cells (optimistic upper bound); `min_seen_cells()` counts only confirmed white cells (conservative lower bound) — used together to prune infeasible branches early |
| Systematic backtracking | `helper_solve_puzzle()` fills cells left-to-right, top-to-bottom; at each step places both `0` and `-1`, recurses, and backtracks if constraints become violated |
| Solution counting | `how_many_solutions()` explores the full search tree, accumulating a count rather than stopping at the first solution |

---

## Constraint Model

```python
constraints = {(row, col, count), ...}
# Each tuple means: the cell at (row, col) must see exactly `count` white cells
# (counting in all 4 cardinal directions until a black cell or grid boundary)
```

Grid values:
- `-1` = white (visible)
- `0` = black (blocks sight)

---

## API

```python
from puzzle_solver import solve_puzzle, how_many_solutions

constraints = {(0, 0, 2), (1, 2, 1)}

# Find one solution (or None if unsolvable)
solution = solve_puzzle(constraints, n=3, m=3)

# Count all valid solutions
count = how_many_solutions(constraints, n=3, m=3)
```

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Key concepts:** Constraint satisfaction, backtracking search, branch-and-bound pruning, partial solution validation
