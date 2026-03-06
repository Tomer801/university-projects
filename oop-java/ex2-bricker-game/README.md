# Brick Breaker with Power-ups

A full Java brick-breaker game engine featuring physics-based ball bouncing, a lives system, and 6 distinct collision-driven power-up strategies implemented via the Strategy pattern.

---

## Problem Solved

Build an extensible arcade game where each brick can trigger a different power-up behaviour on destruction, without coupling the game engine to any specific strategy — new power-ups plug in by adding a class, not by changing `BrickerGameManager`.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Decoupled power-up dispatch | Each `Brick` holds a `CollisionStrategy` reference; on collision, the game manager calls `strategy.onCollision()` — zero knowledge of which strategy it is |
| Strategy composition | `DoubleStrategy` wraps two strategies and invokes both, allowing any two effects to be combined on a single brick without modifying existing strategy classes |
| Physics collisions | Ball velocity vector reflected across the surface normal on each collision; separate handling for wall, brick, and paddle surfaces |
| Extra-paddle lifecycle | `ExtraPaddleStrategy` spawns an additional paddle with an internal hit counter; the paddle removes itself after a fixed number of ball contacts |
| Lives/game-over tracking | `Lives` object owns a graphical heart indicator and a numeric count; losing the last life or clearing all bricks triggers win/loss transitions |

---

## Power-up Strategies

| Strategy | Effect |
|---|---|
| `BasicCollisionStrategy` | Remove brick, no side effect |
| `TurboStrategy` | Increase ball speed; reverts after fixed time |
| `HeartStrategy` | Drop a falling heart; catching it adds a life |
| `ExtraPaddleStrategy` | Spawn second paddle for N ball contacts |
| `AddPuckStrategy` | Spawn two extra puck balls |
| `DoubleStrategy` | Combine any two of the above |

---

## Design Patterns

| Pattern | Where Applied |
|---|---|
| Strategy | `CollisionStrategy` interface; 6 concrete implementations |
| Factory | `StrategyFactory` selects a random strategy (or double) for each brick at game initialisation |
| Composition | `DoubleStrategy` composes two `CollisionStrategy` objects |

---

## Tech Stack & Concepts

- **Language:** Java (SE)
- **Key concepts:** Strategy pattern, factory pattern, composition, collision detection, game loop, entity lifecycle management

---

## Run

```bash
java bricker.BrickerGameManager
```
