# Battleship Game Engine

A terminal-based two-player Battleship game implemented in Python, featuring a board state machine, interactive ship placement, and legal-move generation.

---

## Problem Solved

Implement the full rules of Battleship on a 2D grid: ship placement with overlap and boundary validation, torpedo firing with hit/miss tracking, and game-over detection — all within a clean state-machine model.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Board state machine | Each cell holds one of four states: `WATER`, `SHIP`, `HIT_WATER`, `HIT_SHIP` — transitions are explicit and irreversible |
| Coordinate parsing | Converts alphanumeric input like `"A1"` into `(row, col)` tuples; validates against board dimensions |
| Legal placement validation | `possible_ship_loc()` checks grid boundaries and checks for overlap before placing any ship |
| Legal fire validation | `possible_fire_loc()` prevents re-firing at previously hit cells |

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Key concepts:** 2D list representation, state machines, coordinate parsing, input validation, game loop design

---

## Run

```bash
python3 battleship.py
```

Follow the interactive prompts to place ships and take turns firing torpedoes.
