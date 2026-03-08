# Multi-Directional Word Search

A Python engine that searches for words in an n×m character matrix across all 8 directions, reporting every occurrence with its count.

---

## Problem Solved

Given a word list and a character matrix (both read from files), find every occurrence of each word when scanning horizontally, vertically, and diagonally in all directions. Return a frequency count per word per direction.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| 8-direction coverage | Horizontal (L↔R), vertical (T↔B), and 4 diagonal directions each handled by dedicated extraction functions; reverse directions use `reverse_matrix()` |
| Diagonal iteration | `search_diagonal1()` and `search_diagonal2()` extract all diagonals of both orientations by offsetting start indices across rows and columns |
| No library dependencies | Entire search implemented using list slicing and string operations only |

---

## Directions Searched

| ID | Direction |
|---|---|
| `r` | Left to right |
| `l` | Right to left |
| `u` | Bottom to top |
| `d` | Top to bottom |
| `w` | Diagonal ↖ |
| `x` | Diagonal ↗ |
| `y` | Diagonal ↙ |
| `z` | Diagonal ↘ |

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Key concepts:** Matrix traversal, diagonal iteration, string matching, file I/O

---

## Run

```bash
python3 wordsearch.py word_list.txt matrix.txt directions
# Example: python3 wordsearch.py words.txt grid.txt rlud
```

Output prints each word and its occurrence count per specified direction.
