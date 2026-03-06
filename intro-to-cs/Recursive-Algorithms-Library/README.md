# Recursive Algorithms Library

A Python library of recursive algorithm implementations covering arithmetic, number theory, string manipulation, list operations, and the classic Tower of Hanoi puzzle.

---

## Problem Solved

Demonstrate mastery of recursive thinking by solving ten distinct problems purely through recursion — including a divide-and-conquer approach to multiplication that reduces stack depth from O(n) to O(log n).

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Efficient multiplication | `log_mult(x, y)` uses divide-and-conquer: `log_mult(x, y) = 2 * log_mult(x, y//2)` — O(log n) depth vs. O(n) for naive repeated addition |
| Power detection | `is_power(b, x)` recursively divides `x` by `b` and checks for exact fit; handles edge cases (b=1, x=1) |
| Digit-sum accumulation | `number_of_ones(n)` counts all digit `1`s across every integer 1..n via recursive decomposition |
| 2D list comparison | `compare_2d_lists(l1, l2)` traverses rows and columns recursively without nested loops |
| Tower of Hanoi | Classic 3-peg solution with O(2ⁿ - 1) move count |

---

## Function Reference

| Function | Description |
|---|---|
| `mult(x, y)` | Recursive multiplication via repeated addition — O(n) |
| `log_mult(x, y)` | Divide-and-conquer multiplication — O(log n) |
| `is_even(n)` | Parity via recursive decrement |
| `is_power(b, x)` | Check if x = bᵏ for some integer k ≥ 0 |
| `reverse(s)` | Recursive string reversal |
| `play_hanoi(h, n, src, dest, temp)` | Tower of Hanoi solver |
| `count_ones(n)` | Count digit `1` in the decimal representation of n |
| `number_of_ones(n)` | Sum of `1`-digits across all integers 1..n |
| `compare_2d_lists(l1, l2)` | Structural equality check for 2D lists |
| `magic_list(n)` | Generate nested recursive list structure |

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Key concepts:** Divide-and-conquer, tail recursion patterns, structural recursion, recursive data structures

---

## Usage

```python
from ex7 import mult, log_mult, is_power, reverse, number_of_ones

print(mult(6, 7))           # 42
print(log_mult(6, 7))       # 42
print(is_power(2, 64))      # True
print(reverse("hello"))     # "olleh"
print(number_of_ones(20))   # 12
```
