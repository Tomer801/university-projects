# Tic-Tac-Toe Tournament – Java Project

Java implementation of a flexible **Tic-Tac-Toe (Noughts and Crosses)** game and tournament framework.  
Supports different player types, renderers, and repeated matches.

---

## Files
- **Main.java** – Entry point. Parses command-line arguments, builds players and renderers using factories, and launches a game or tournament.  
- **Board.java** – Represents the 3×3 game board. Tracks cell marks, validates moves, checks win/draw conditions.  
- **Mark.java** – Enum-like class for marks (`X`, `O`, `BLANK`).  
- **Game.java** – Runs a single match between two players on a board with a renderer. Handles turn-taking and win/draw detection.  
- **Player.java** – Abstract base class for players. Subclasses must implement `playTurn(Board)` to choose moves.  
- **PlayerFactory.java** – Factory to create players from string names/arguments. Supports human players, random players, etc.  
- **Tournament.java** – Runs multiple games between players in a round-robin or sequential format. Collects results.  
- **Renderer.java** – Interface for board renderers.  
- **RendererFactory.java** – Factory to create renderers (console, void/silent, etc.).  
- **VoidRenderer.java** – No-op renderer (used for silent tournaments).  

---

## Features
- Play a single interactive Tic-Tac-Toe game.  
- Run a tournament between multiple players.  
- Extendable player types (AI, human, random, etc.).  
- Different rendering strategies (console output, silent).  
- Modular factories for easy expansion.  

---

## Build
Compile with `javac`:
```bash
javac *.java
```

---

## Usage
Run with `java`:

```bash
java Main <renderer> <player1> <player2> [player3 ...]
```

- `<renderer>`: one of `console`, `void`.  
- `<playerX>`: player type (e.g., `human`, `random`).  

### Example
```bash
java Main console human random
```

Plays one game: human vs random, board shown in console.

```bash
java Main void random random random
```

Runs a silent tournament among three random players.

---

## Example Output
```
X | O | X
O | X |  
  |   | O
Player X wins!
```

---

## License
Educational use. Add a license if you plan to publish broadly.
