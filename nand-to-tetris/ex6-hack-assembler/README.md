# Hack Assembler – Nand2Tetris Project

A Python implementation of the **Hack Assembler**, part of the [Nand2Tetris](https://www.nand2tetris.org) course.  
This assembler translates Hack assembly (`.asm`) programs into Hack binary (`.hack`) machine code.

---

## Files
- **Code.py** – Translates Hack assembly mnemonics (`dest`, `comp`, `jump`) into binary codes.【239†source】
- **Parser.py** – Parses assembly instructions, strips whitespace/comments, and provides access to command types and fields.【241†source】
- **SymbolTable.py** – Maintains a symbol table mapping symbols/labels to addresses. Initialized with predefined Hack symbols (`R0`–`R15`, `SCREEN`, `KBD`, etc.).【242†source】
- **Main.py** – The assembler driver. Performs a **two-pass translation**: first adds labels to the symbol table, then generates binary instructions. Handles A-commands, C-commands, and ignores labels during code generation.【240†source】
- **Assembler** – Script/entry point for running the assembler from the command line (wrapper around `Main.py`).【240†source】
- **Makefile** – Build automation for running the assembler easily.

---

## Features
- **Two-pass assembler**:
  1. **First pass**: adds labels `(LOOP)` etc. to the symbol table with ROM addresses.【240†source】
  2. **Second pass**: translates A-commands and C-commands into 16-bit binary instructions.【240†source】
- **Symbol resolution**:
  - Supports predefined symbols (SP, LCL, ARG, THIS, THAT, R0–R15, SCREEN, KBD).【242†source】
  - Supports user-defined variables (allocated starting at RAM address 16).【240†source】
- **C-instructions**: Translates `dest=comp;jump` into `111<comp><dest><jump>` format.【239†source】
- **A-instructions**: `@value` translated to `0vvvvvvvvvvvvvvv` (15-bit value).【240†source】

---

## Build & Run
Run directly with Python 3.8+:

```bash
python3 Main.py <input.asm>
```

or using the `Assembler` script:

```bash
python3 Assembler <input.asm>
```

The assembler produces an output file with the same name but `.hack` extension.  
For example:

```bash
python3 Main.py Add.asm
# produces Add.hack
```

---

## Example

Input (`Add.asm`):
```asm
@2
D=A
@3
D=D+A
@0
M=D
```

Output (`Add.hack`):
```
0000000000000010
1110110000010000
0000000000000011
1110000010010000
0000000000000000
1110001100001000
```

---

## License
This project follows the [CC BY-NC-SA 3.0 License](https://creativecommons.org/licenses/by-nc-sa/3.0/) as required by the Nand2Tetris course materials.
