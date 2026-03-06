# Hack Assembler — Two-Pass Assembler

A Python two-pass assembler that translates Hack assembly language (`.asm`) into 16-bit binary machine code (`.hack`) for the Hack computer platform.

---

## Problem Solved

Implement the complete assembly pipeline for the Hack ISA: resolve symbolic labels and variable references in Pass 1, then translate every A-instruction and C-instruction into its 16-bit binary encoding in Pass 2.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Two-pass label resolution | Pass 1 scans for `(LABEL)` pseudo-instructions and maps each label to its ROM address; Pass 2 uses the completed symbol table for translation |
| Variable allocation | First occurrence of an undeclared symbol allocates the next available RAM address starting at 16; subsequent references look up the same address |
| C-instruction encoding | Bit fields `dest` (3 bits), `comp` (7 bits), `jump` (3 bits) looked up in mnemonic→binary tables in `Code.py`; combined as `111ccccccdddjjj` |
| A-instruction encoding | Numeric literals → 15-bit binary; symbols resolved through symbol table → `0vvvvvvvvvvvvvvv` |

---

## Architecture

```
Parser.py       — tokenise lines; classify as A_COMMAND, C_COMMAND, or L_COMMAND
Code.py         — mnemonic → binary lookup tables (dest, comp, jump)
SymbolTable.py  — symbol → address map; predefined registers + user labels + variables
Main.py         — orchestrates two passes; writes .hack output file
```

**Predefined symbols:** `R0–R15`, `SP`, `LCL`, `ARG`, `THIS`, `THAT`, `SCREEN` (0x4000), `KBD` (0x6000)

---

## Example

```asm
@2         →  0000000000000010
D=A        →  1110110000010000
@3         →  0000000000000011
D=D+A      →  1110000010010000
@0         →  0000000000000000
M=D        →  1110001100001000
```

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Build:** `make` or `python3 Main.py <file.asm>`
- **Key concepts:** Two-pass assembly, symbol tables, instruction encoding, Hack ISA

---

## Run

```bash
python3 Main.py Program.asm    # produces Program.hack
# or
make && ./Assembler Program.asm
```
