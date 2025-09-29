# Infinite World Simulation – Java Project

Java implementation of an **Infinite World Simulation**, where entities interact with their environment.  
The project demonstrates modular design, indicators, and strategy-like components.

---

## Files
- **InfiniteWorld.java** – Main simulation manager. Initializes world state, runs updates, tracks entities.  
- **EnergyIndicator.java** – Tracks and displays entity energy/health levels.  
- **ColorSupplier.java** – Supplies colors dynamically (for rendering entities, UI indicators, etc.).  
- **NameConstant.java** – Defines string constants for naming entities, attributes, or parameters.  

---

## Features
- Infinite world grid where entities exist and interact.  
- Energy system – entities consume/restore energy.  
- Indicators show entity states visually (e.g., health bars, energy bars).  
- Color abstraction for customizing display.  
- Modular constants for easier configuration and maintainability.  

---

## Strategies & Design
The system is built with **strategy-like abstractions**:  
- `ColorSupplier` acts as a pluggable strategy to determine entity colors dynamically.  
- Indicators can be extended (e.g., health, stamina, mana).  
- Simulation logic can be extended by injecting new behaviors into `InfiniteWorld`.  

This design makes it easy to add new mechanics (e.g., AI-controlled movement, additional indicators) without changing core logic.

---

## Build
Compile with `javac`:
```bash
javac *.java
```

---

## Run
Run the simulation with:
```bash
java InfiniteWorld
```

---

## Example Behavior
- Entities move across the world grid.  
- Energy depletes as they move.  
- `EnergyIndicator` shows remaining energy in real time.  
- World continues infinitely in all directions (looping or procedural generation).  

---

## License
Educational use. Add a license if you plan to publish broadly.
