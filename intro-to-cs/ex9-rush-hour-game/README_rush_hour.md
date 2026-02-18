# Intro2CS Exercise 9 – Rush Hour Game

Python implementation of the classic **Rush Hour** puzzle.  
The game is built using three main classes: `Car`, `Board`, and `Game`.  
Cars are placed on a 7×7 board with a single exit at cell `(3,7)`. The goal is to move the red car (`R`) to the exit.

---

## Files
- **`car.py`** – defines the `Car` class, representing a car on the board.
- **`board.py`** – defines the `Board` class, representing the game board.
- **`game.py`** – defines the `Game` class, manages user interaction and gameplay.

---

## Features
- **Car class**:
  - Stores car name, length, head location, orientation.
  - Provides possible moves (`u`, `d`, `l`, `r`) depending on orientation.
  - Returns required empty cells for a move.
  - Updates its location when moved.
- **Board class**:
  - Maintains a 7×7 board plus exit `(3,7)`.
  - Tracks car positions and validates moves.
  - Adds cars only if placement is legal (inside board & not overlapping).
  - Generates all possible moves for all cars.
  - Executes valid car moves and updates board state.
- **Game class**:
  - Loads initial board configuration from a JSON file.
  - Handles the game loop, user input, and victory condition.
  - User can type `!` to quit at any time.

---

## Requirements
- Python 3.8+
- A valid **JSON configuration file** with car definitions:
  ```json
  {
    "R": [2, [3, 0], 1],
    "O": [3, [0, 4], 0]
  }
  ```
  Format per car: `[length, [row, col], orientation]`  
  - `length` ∈ {2,3,4}  
  - `orientation` = 0 (vertical), 1 (horizontal)  
  - `row, col` = head position

---

## Usage
Run from the command line:
```bash
python3 game.py car_config.json
```

### Example game loop
```
[_ ,_ ,_ ,_ ,_ ,_ ,_ ,#]
...
[R ,R ,_ ,_ ,_ ,_ ,_ ,E]

input (car name , direction) in this format:
R,r
```

- Valid directions:
  - `u` = up
  - `d` = down
  - `l` = left
  - `r` = right
- Type `!` to quit.

Game ends when:
- The red car (`R`) reaches the exit `(3,7)`.
- No legal moves remain.
- User quits.

---

## Key Classes and Methods
- **Car**
  - `car_coordinates()`
  - `possible_moves()`
  - `movement_requirements(move_key)`
  - `move(move_key)`
- **Board**
  - `cell_list()`
  - `cell_content(coord)`
  - `add_car(car)`
  - `possible_moves()`
  - `move_car(name, move_key)`
- **Game**
  - `play()`
  - `__single_turn()`
  - `create_game(config)`

---

## Limitations
- JSON file must be well-formed; invalid cars are skipped silently.
- Only standard input/output supported (no GUI).
- Assumes cars have unique names.

---

## License
Educational use. Add a license if you plan to publish broadly.
