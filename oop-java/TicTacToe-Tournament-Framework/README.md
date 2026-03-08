# TicTacToe Tournament Framework

An extensible Java game framework supporting single matches and multi-player tournament scheduling for Tic-Tac-Toe, with pluggable player types and rendering strategies.

---

## Problem Solved

Design a game framework that is open to extension — new AI strategies, player types, and renderers can be added without modifying existing code — while keeping the game, tournament, and UI layers cleanly decoupled.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Extensible player types | `Player` is an abstract base class; subclasses implement `playTurn(Board)` only — tournament and game logic never depend on concrete types |
| Open/closed player creation | `PlayerFactory` maps string names to player instances, so new player types register in one place without touching `Main` or `Tournament` |
| Renderer decoupling | `Renderer` interface allows both `ConsoleRenderer` (interactive) and `VoidRenderer` (silent tournament mode) to be swapped at runtime via `RendererFactory` |
| Win/draw detection | `Board` checks rows, columns, and both diagonals after each move; returns the winning mark or `BLANK` for a draw |
| Tournament result aggregation | `Tournament` runs n games and accumulates a win-count map; results are printed as a formatted leaderboard |

---

## Design Patterns

| Pattern | Where Applied |
|---|---|
| Abstract class / Polymorphism | `Player` abstract base; `HumanPlayer`, `RandomPlayer`, `CleverPlayer`, `GeniusPlayer` subclasses |
| Factory Method | `PlayerFactory`, `RendererFactory` — centralised object creation from string arguments |
| Strategy | Different `Renderer` implementations injected at runtime |

---

## Tech Stack & Concepts

- **Language:** Java (SE)
- **Key concepts:** OOP design, abstract classes, factory pattern, strategy pattern, polymorphism, game loop architecture

---

## Build & Run

```bash
javac *.java

# Single game (human vs random, console rendering)
java Main console human random

# Silent tournament (three random players)
java Main void random random random
```
