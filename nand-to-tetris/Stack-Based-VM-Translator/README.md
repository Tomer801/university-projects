# Stack-Based VM Translator

A Python VM translator that compiles Hack Virtual Machine (VM) stack-based bytecode (`.vm`) into Hack assembly (`.asm`), implementing arithmetic, memory access, branching, and function-call semantics.

---

## Problem Solved

Implement the full VM-to-assembly compilation layer for the Hack platform: translate all five VM command categories while correctly managing the runtime stack, memory segments, and the function-call/return protocol.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Stack arithmetic | Each command pops operand(s), applies the operation in registers, pushes result; comparison commands (`eq`, `gt`, `lt`) use conditional jumps with unique auto-generated labels |
| Memory segment addressing | `local/argument/this/that` use indirect addressing via base-pointer registers; `temp/pointer` use fixed RAM offsets (R5–R12, R3–R4); `static` uses file-scoped named variables; `constant` pushes an immediate |
| Function call protocol | `call` saves five caller-frame fields on the stack and repositions ARG/LCL; `return` restores the frame from the saved data and jumps to the return address |
| Multi-file translation | Accepts a directory of `.vm` files and produces one merged `.asm` output; each file's static variables are namespaced by filename |
| Bootstrap | Emits `SP=256` and `call Sys.init` before the first translated instruction |

---

## VM Command Reference

| Category | Commands |
|---|---|
| Arithmetic/Logic | `add`, `sub`, `neg`, `eq`, `gt`, `lt`, `and`, `or`, `not`, `shiftleft`, `shiftright` |
| Memory Access | `push`/`pop` — `constant`, `local`, `argument`, `this`, `that`, `temp`, `pointer`, `static` |
| Branching | `label`, `goto`, `if-goto` |
| Functions | `function`, `call`, `return` |

---

## Example

```
push constant 7
push constant 8
add
```
compiles to push 7 and 8 onto the Hack stack, then pop both into registers, add them, and push the result.

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Build:** `make` or `python3 Main.py <file.vm | directory>`
- **Key concepts:** Stack machine, memory segmentation, function-call protocol, code generation, assembly

---

## Run

```bash
python3 Main.py Program.vm          # single file → Program.asm
python3 Main.py ProgramDirectory/   # directory  → ProgramDirectory.asm
```
