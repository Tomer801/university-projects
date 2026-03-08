# Infinite World Simulation — PEPSE

A Java side-scrolling simulation featuring a procedurally generated infinite world, a physics-driven avatar with an energy system, and modular visual indicators — all wired together through clean OOP abstractions.

---

## Problem Solved

Build an extensible infinite world simulation where the world generates terrain on demand as the avatar moves, an energy system governs avatar actions, and visual feedback components update reactively — without tight coupling between simulation, physics, and UI layers.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Procedural infinite terrain | World chunks are generated on the fly as the camera moves; already-visited chunks are cached to avoid regeneration |
| Energy-constrained avatar | Avatar maintains an energy value (0–100); jumping costs energy, standing still regenerates it; actions are gated on sufficient energy |
| Reactive energy indicator | `EnergyIndicator` observes the avatar's energy and updates a graphical bar each frame — UI and simulation state are decoupled |
| Dynamic colour rendering | `ColorSupplier` is a pluggable strategy injected into terrain/flora renderers, allowing visual variety without changing rendering logic |
| Constants management | All layer IDs, tags, and configuration values live in `NameConstant` — a single source of truth that prevents magic-string bugs |

---

## Architecture

```
pepse/
├── PepseGameManager     — world initialisation, camera setup, game loop
├── world/
│   ├── Terrain          — procedural ground generation (noise-based height)
│   ├── Sky / Night      — background layers
│   ├── trees/           — tree + leaf procedural placement
│   └── Avatar           — player entity, physics, energy management
└── util/
    ├── EnergyIndicator  — reactive energy HUD element
    └── ColorSupplier    — pluggable colour strategy
```

---

## Design Patterns

| Pattern | Where Applied |
|---|---|
| Strategy | `ColorSupplier` — different colour policies injected into renderers |
| Observer-like | `EnergyIndicator` polled/notified on energy changes |
| Factory / procedural generation | Terrain and flora generation driven by deterministic noise functions |

---

## Tech Stack & Concepts

- **Language:** Java (SE)
- **Framework:** DanoGameLab (course game engine)
- **Key concepts:** Procedural generation, energy/physics system, strategy pattern, observer pattern, modular game architecture

---

## Run

```bash
java pepse.PepseGameManager
```
