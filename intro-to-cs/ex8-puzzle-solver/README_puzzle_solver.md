# Intro2CS Exercise 8 – Puzzle Solver

Python program that solves logic puzzles defined by constraints on how many "seen" cells appear from specific positions in a grid.  
The solver uses recursion and backtracking to fill the grid, validate constraints, and count possible solutions.

---

## Features
- **Constraint checking**:
  - `max_seen_cells(picture, row, col)` – compute maximum visible cells from a position (count `-1` as white).
  - `min_seen_cells(picture, row, col)` – compute minimum visible cells from a position (count `-1` as black).
  - `check_constraints(picture, constraints_set)` – verify if constraints are satisfied (`0` = invalid, `1` = fully satisfied, `2` = partially valid).
- **Puzzle solving**:
  - `solve_puzzle(constraints_set, n, m)` – return a solved puzzle if one exists.
  - `helper_solve_puzzle(...)` – recursive backtracking engine.
- **Counting solutions**:
  - `how_many_solutions(constraints_set, n, m)` – count how many valid solutions exist for given constraints.
- **Puzzle generation**:
  - `generate_puzzle(picture)` – stub for creating a puzzle from a solved grid (currently minimal).

---

## Requirements
- Python 3.8+

---

## Usage
Import and call functions in Python:

```python
import puzzle_solver

constraints = {(0, 0, 1), (1, 1, 2)}  # example constraints
solution = puzzle_solver.solve_puzzle(constraints, 3, 3)
print(solution)

num_solutions = puzzle_solver.how_many_solutions(constraints, 3, 3)
print("Number of solutions:", num_solutions)
```

Constraints are defined as a set of tuples `(row, col, value)` meaning the cell at `(row, col)` must see exactly `value` cells in its row/column directions.

---

## Key Functions
- `max_seen_cells(picture, row, col)`
- `min_seen_cells(picture, row, col)`
- `check_constraints(picture, constraints_set)`
- `solve_puzzle(constraints_set, n, m)`
- `helper_solve_puzzle(temp_picture, constraints_set, i, j)`
- `how_many_solutions(constraints_set, n, m)`
- `helper_how_many_solutions(...)`
- `generate_puzzle(picture)`

---

## Limitations
- `generate_puzzle` is not fully implemented (placeholder).
- Backtracking can be slow for large grids or complex constraints.

---

## License
Educational use. Add a license if you plan to publish broadly.
