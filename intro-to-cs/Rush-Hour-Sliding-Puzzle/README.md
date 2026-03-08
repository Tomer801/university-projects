# Rush Hour Sliding Puzzle

A full OOP implementation of the classic Rush Hour board game in Python. Cars slide along a 7×7 grid; the objective is to move the red car (`R`) to the exit at `(3,7)`.

---

## Problem Solved

Model the complete Rush Hour game rules in three clean, decoupled classes — `Car`, `Board`, and `Game` — with JSON-driven configuration, move validation, legal-move generation, and win/loss detection.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Orientation-aware movement | `Car` stores orientation (horizontal=1, vertical=0); `possible_moves()` returns only the valid direction keys for that orientation |
| Collision detection | `movement_requirements(move_key)` returns the cells the car needs empty before it can move; `Board` checks each against current occupancy |
| Encapsulated board state | `Board` maintains a dict mapping cell coordinates → car names; `add_car()` atomically validates and registers all cells a car occupies |
| Decoupled game loop | `Game` loads config and constructs objects, then drives the loop — UI logic is fully separated from model logic |
| JSON configuration | Car definitions loaded from a file: `{"R": [length, [row, col], orientation], ...}` |

---

## Class Overview

```
Car
├── car_coordinates()          → list of occupied cells
├── possible_moves()           → dict of {direction: description}
├── movement_requirements(key) → cells that must be free
└── move(key)                  → update head position in-place

Board  (7×7 grid + exit at (3,7))
├── add_car(car)               → validate and place car
├── possible_moves()           → all legal (car_name, direction) pairs
├── move_car(name, direction)  → execute move and update state
└── cell_content(coord)        → car name at coord, or None

Game
├── create_game(config)        → parse JSON, populate board
└── play()                     → game loop with win/quit detection
```

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Key concepts:** OOP design, state machines, move validation, JSON parsing, game loop architecture

---

## Run

```bash
python3 game.py car_config.json
```

**JSON config example:**
```json
{
  "R": [2, [3, 0], 1],
  "O": [3, [0, 4], 0]
}
```

Input during play: `<CarName>,<direction>` (e.g., `R,r`). Type `!` to quit.
