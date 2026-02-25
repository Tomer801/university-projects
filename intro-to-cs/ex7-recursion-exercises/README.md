# Intro2CS Exercise 7 – Recursion and Lists

Python implementation of recursive algorithms for numbers, strings, lists, and the Tower of Hanoi puzzle.

---

## Features
- **Recursive multiplication**:
  - `mult(x, y)` – multiplication using repeated addition.
  - `log_mult(x, y)` – efficient multiplication using divide & conquer (logarithmic recursion).
- **Number properties**:
  - `is_even(n)` – check if a number is even using recursion.
  - `is_power(b, x)` – check if `x` is a power of `b`.
- **Strings**:
  - `reverse(s)` – reverse a string recursively.
- **Classic recursion problem**:
  - `play_hanoi(Hanoi, n, src, dest, temp)` – solve the Tower of Hanoi.
- **Digit problems**:
  - `count_ones(n)` – count number of digit `1` in a number.
  - `number_of_ones(n)` – count total `1`s between 1 and `n`.
- **Lists**:
  - `compare_2d_lists(l1, l2)` – compare two 2D lists (check structure and values).
  - `magic_list(n)` – create recursive "magical" lists structure.

---

## Requirements
- Python 3.8+
- `ex7_helper.py` module provided by the course (with helpers like `add`, `subtract_1`, `divide_by_2`, `is_odd`, `append_to_end`).

---

## Usage
Import and call functions in Python:

```python
import ex7

print(ex7.mult(3, 4))          # 12
print(ex7.is_even(7))          # False
print(ex7.reverse("abcd"))     # "dcba"
print(ex7.is_power(2, 8))      # True
print(ex7.count_ones(101))     # 2
print(ex7.number_of_ones(15))  # total 1's from 1–15
```

Tower of Hanoi example:
```python
from ex7_helper import Hanoi
h = Hanoi(3)
ex7.play_hanoi(h, 3, 0, 2, 1)
```

---

## Key Functions
- `mult(x, y)`
- `log_mult(x, y)`
- `is_even(n)`
- `is_power(b, x)`
- `reverse(s)`
- `play_hanoi(Hanoi, n, src, dest, temp)`
- `count_ones(n)`
- `number_of_ones(n)`
- `compare_2d_lists(l1, l2)`
- `magic_list(n)`

---

## Limitations
- Requires helper functions from `ex7_helper.py`.
- Recursive definitions may hit recursion depth limits for very large inputs.

---

## License
Educational use. Add a license if you plan to publish broadly.
