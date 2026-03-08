# Jack OS Standard Library

A complete operating-system standard library written in the Jack programming language, providing math, memory management, string manipulation, I/O, and graphics services to all Jack programs running on the Hack platform.

---

## Problem Solved

Implement the OS layer that bridges high-level Jack programs and the raw Hack hardware: provide arithmetic operations the CPU lacks (multiply, divide, sqrt), a heap memory allocator, character/string/integer I/O, bitmap screen drawing, and system bootstrap/halt.

---

## Library Classes

| Class | Responsibility | Key Implementation Notes |
|---|---|---|
| `Math.jack` | `abs`, `multiply`, `divide`, `sqrt`, `max`, `min` | Multiply via shift-and-add (O(log n)); sqrt via binary search |
| `String.jack` | Construction, `length`, `charAt`, `appendChar`, `eraseLastChar`, `intValue`, `setInt` | Internal char array with length tracking; integer parsing via digit iteration |
| `Array.jack` | `new(size)`, `dispose()` | Thin wrapper over `Memory.alloc` / `Memory.deAlloc` |
| `Memory.jack` | `peek(addr)`, `poke(addr, val)`, `alloc(size)`, `deAlloc(object)` | Free-list heap allocator using a linked block structure in RAM |
| `Screen.jack` | `clearScreen`, `drawPixel`, `drawLine`, `drawRectangle`, `drawCircle` | Bitwise word-level access to the screen memory map (0x4000–0x5FFF); Bresenham-style line algorithm |
| `Output.jack` | `moveCursor`, `printChar`, `printString`, `printInt`, `println` | Fixed-width bitmap font rendered by writing bit patterns to screen memory |
| `Keyboard.jack` | `keyPressed`, `readChar`, `readLine`, `readInt` | Polls the keyboard memory-mapped register (0x6000) for key codes |
| `Sys.jack` | `init`, `halt`, `error`, `wait` | Bootstrap: calls `Math.init`, `Memory.init`, `Screen.init`, `Output.init`, `Keyboard.init`, then `Main.main` |

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Multiplication without a MUL instruction | `Math.multiply(x, y)` uses repeated shift-and-add: check each bit of y, accumulate `x << bit` — O(16) iterations for 16-bit integers |
| Square root without hardware support | Binary search over the range [0, 2^(n/2)]; uses only multiply and compare |
| Heap allocator | `Memory.alloc` maintains a free list of variable-size blocks; `deAlloc` coalesces adjacent free blocks to reduce fragmentation |
| Screen drawing | `Screen.drawLine` uses Bresenham-derived incremental algorithm; `drawCircle` uses `sqrt` and horizontal line segments |

---

## Tech Stack & Concepts

- **Language:** Jack (high-level language for the Hack platform)
- **Key concepts:** OS primitives, memory management (free-list allocator), bitmap graphics, hardware memory-mapped I/O, arithmetic algorithms

---

## Compile & Use

```bash
# Compile all .jack files using the Jack compiler from ex10-11
python3 ../ex10-11-jack-compiler/JackCompiler.py .
```

The resulting `.vm` files can then be loaded into the Hack VM emulator.
