# Bricker Game – Java Project

Java implementation of a **Brick Breaker** style arcade game.  
The game includes balls, paddles, bricks, power-ups, and different player strategies.

---

## Files
- **Ball.java / Puck.java** – Ball-like objects that bounce around the screen and break bricks.  
- **Paddle.java / ExtraPaddle.java** – Player-controlled paddles for deflecting the ball. Extra paddle may appear as a power-up.  
- **Brick.java / Heart.java / Lives.java** – Bricks that must be broken, life/heart tracking for the player.  
- **BrickerGameManager.java** – Main game manager: initializes entities, updates game state, detects collisions, applies power-ups, tracks win/loss.  
- **TurboStrategy.java** – Example strategy that modifies ball/paddle behavior (e.g., speed boost).  
- *(Other strategy classes may be included but not detailed here.)*

---

## Features
- Classic brick-breaker gameplay with multiple lives.  
- Ball bounces against walls, bricks, and paddles.  
- Bricks are destroyed when hit, some may spawn bonuses.  
- Power-ups such as extra paddle, extra life, or turbo mode.  
- Flexible strategy design – new strategies can be plugged in without changing the game engine.  

---

## Strategies
The game supports **strategy pattern** for power-ups:  
- When a brick is destroyed, it may trigger a **strategy** that changes game behavior.  
- Example: `TurboStrategy` speeds up the ball and increases difficulty.  
- Other strategies (e.g., wider paddle, multi-ball, slow ball) can be added easily.  
- The game manager applies the chosen strategy dynamically at runtime.

This design allows experimenting with different gameplay mechanics without rewriting core logic.

---

## Build
Compile with `javac`:
```bash
javac *.java
```

---

## Run
Run the game with:
```bash
java BrickerGameManager
```

The game window opens, and the player controls the paddle to keep the ball in play and break all bricks.

---

## Example Gameplay
- Start with 3 lives.  
- Ball moves upward, bounces off walls, and hits bricks.  
- Some bricks spawn power-ups.  
- Lose a life if the ball falls below the paddle.  
- Win by clearing all bricks.  

---

## License
Educational use. Add a license if you plan to publish broadly.
