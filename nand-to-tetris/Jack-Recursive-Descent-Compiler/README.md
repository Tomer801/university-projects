# Jack Language Compiler — Recursive Descent

A Python compiler for the Jack object-oriented language that performs lexical analysis, recursive-descent parsing, two-scope symbol resolution, and on-the-fly VM code emission — translating `.jack` source files directly into Hack VM bytecode (`.vm`).

---

## Problem Solved

Implement a complete single-pass compiler pipeline: tokenise Jack source text, parse it according to the Jack grammar using recursive descent, resolve identifiers through a two-scope symbol table (class-level + subroutine-level), and emit equivalent stack-based VM code without constructing an intermediate AST.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Lexical analysis | `JackTokenizer` strips comments/whitespace and classifies each token as keyword, symbol, identifier, integer constant, or string constant |
| Recursive descent parsing | `CompilationEngine` has one method per grammar production (`compileClass`, `compileSubroutine`, `compileStatements`, etc.); each consumes tokens and emits code directly |
| Two-scope symbol table | `SymbolTable` maintains class-level (`static`, `field`) and subroutine-level (`arg`, `var`) scopes independently; `startSubroutine()` resets the inner scope without touching the class scope |
| Constructor/method/function dispatch | Constructors allocate heap via `Memory.alloc(nFields)`; methods push `this` as argument 0 via `pointer 0`; functions have no implicit object |
| No AST required | `VMWriter` emits VM commands as the parser traverses the source — code generation and parsing are interleaved in a single pass |

---

## Compilation Pipeline

```
.jack source
  → JackTokenizer        — lexical analysis, token classification
  → CompilationEngine    — recursive descent, drives all components
       ├─ SymbolTable    — class-scope + subroutine-scope symbol resolution
       └─ VMWriter       — push/pop, arithmetic, labels, calls, function, return
  → .vm output
```

---

## Example

```jack
class Main {
    function void main() {
        do Output.printString("Hello, World");
        return;
    }
}
```

Compiles to VM code that calls `String.new`, populates the string with individual character codes via `String.appendChar`, calls `Output.printString`, then returns.

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Build:** `make` or `python3 JackCompiler.py <File.jack | directory>`
- **Key concepts:** Lexical analysis, recursive descent parsing, two-scope symbol tables, single-pass code generation, OOP compilation

---

## Run

```bash
python3 JackCompiler.py MyClass.jack       # produces MyClass.vm
python3 JackCompiler.py ProjectDirectory/  # compiles all .jack files in directory
```
