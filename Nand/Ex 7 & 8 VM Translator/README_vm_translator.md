# VM Translator – Nand2Tetris Project

A Python implementation of the **VM Translator**, part of the [Nand2Tetris](https://www.nand2tetris.org) course.  
This translator converts **Virtual Machine (VM)** language programs (`.vm`) into Hack assembly (`.asm`).

---

## Files
- **Parser.py** – Parses `.vm` files, strips whitespace/comments, and exposes command type + arguments (arithmetic, push/pop, branching, functions).【256†source】
- **CodeWriter.py** – Translates VM commands into Hack assembly. Implements arithmetic, memory access, branching, function calls, bootstrap initialization.【257†source】
- **Main.py** – Entry point for translation. Handles input file/directory, iterates `.vm` files, applies parser + code writer, and produces `.asm` output.【255†source】
- **VMtranslator** – Script wrapper for running translator easily.【255†source】
- **Makefile** – Build automation for running translator on test files.
- **AUTHORS** – Project authorship info.

---

## Features
- **Arithmetic commands**: add, sub, neg, eq, gt, lt, and, or, not, shiftleft, shiftright.【257†source】
- **Memory access**: push/pop to segments (constant, local, argument, this, that, temp, pointer, static).【257†source】
- **Branching**: label, goto, if-goto (function-scoped labels).【257†source】
- **Functions & calls**: function, call, return with full frame handling.【257†source】
- **Bootstrap code**: initializes stack pointer to 256 and calls `Sys.init`.【257†source】
- **Two-phase file handling**: can process one `.vm` file or an entire directory of `.vm` files into a single `.asm` output.【255†source】

---

## Build & Run
Run with Python 3.8+:

```bash
python3 Main.py <input.vm | directory>
```

or using the `VMtranslator` script:

```bash
python3 VMtranslator <input.vm | directory>
```

If a directory is given, all `.vm` files inside are translated into one `<dirname>.asm` file.【255†source】

---

## Example

Input (`SimpleAdd.vm`):
```
push constant 7
push constant 8
add
```

Output (`SimpleAdd.asm`):
```
@7
D=A
@SP
A=M
M=D
@SP
M=M+1
@8
D=A
@SP
A=M
M=D
@SP
M=M+1
@SP
AM=M-1
D=M
@SP
A=M-1
M=D+M
```

---

## License
This project follows the [CC BY-NC-SA 3.0 License](https://creativecommons.org/licenses/by-nc-sa/3.0/) as required by the Nand2Tetris course materials.
