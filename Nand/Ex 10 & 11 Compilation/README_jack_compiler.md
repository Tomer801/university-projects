# Jack Compiler – Nand2Tetris Project

A Python implementation of the **Jack Compiler**, part of the [Nand2Tetris](https://www.nand2tetris.org) course.  
This compiler translates Jack high-level programs (`.jack`) into VM code (`.vm`) for the Hack platform.

---

## Files
- **JackCompiler.py / JackCompiler** – Entry point for compilation. Accepts a single `.jack` file or a directory and compiles it into `.vm` files.  
- **JackTokenizer.py** – Tokenizes Jack source code into keywords, symbols, identifiers, integer constants, and string constants. Removes whitespace/comments.【285†source】  
- **CompilationEngine.py** – Recursive descent parser. Compiles classes, variables, subroutines, statements, expressions, and terms, outputting VM commands.【287†source】  
- **SymbolTable.py** – Maintains symbol tables for class-level and subroutine-level scopes, mapping identifiers to type, kind, and index.  
- **VMWriter.py** – Encapsulates VM command writing (push/pop, arithmetic, label, goto, if-goto, call, function, return).【286†source】  
- **Makefile** – Build automation for running the compiler on test programs.  
- **AUTHORS** – Project authorship info.

---

## Features
- **Tokenizer**: strips comments, splits code into valid Jack tokens.【285†source】  
- **Compilation Engine**: compiles according to Jack grammar rules (class, varDec, subroutineDec, statements, expressions). Emits VM code with correct function calls, branching, and memory access.【287†source】  
- **Symbol Table**: supports defining static, field, argument, and local variables with unique indices.  
- **VM Writer**: produces valid VM commands for arithmetic, memory segments, program flow, function calls, and return.【286†source】  
- **Two-level scope management**: class scope (static/field) and subroutine scope (arg/var).  

---

## Build & Run
Run with Python 3.8+:

```bash
python3 JackCompiler.py <input.jack | directory>
```

If a directory is provided, all `.jack` files inside are compiled into `.vm` files with the same basename.  

---

## Example

Input (`Main.jack`):
```jack
class Main {
    function void main() {
        do Output.printString("Hello, World");
        return;
    }
}
```

Output (`Main.vm`):
```
function Main.main 0
push constant 12
call String.new 1
push constant 72
call String.appendChar 2
push constant 101
call String.appendChar 2
...
call Output.printString 1
pop temp 0
push constant 0
return
```

---

## License
This project follows the [CC BY-NC-SA 3.0 License](https://creativecommons.org/licenses/by-nc-sa/3.0/) as required by the Nand2Tetris course materials.
