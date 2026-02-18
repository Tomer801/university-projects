# Jack Standard Library – Nand2Tetris Project

Implementation of the **Jack OS standard library** in the Jack programming language, part of the [Nand2Tetris](https://www.nand2tetris.org) course.  
These files provide built-in classes that support high-level Jack programs, mapping directly to VM/Hack platform functionality.

---

## Files
- **Math.jack** – Mathematical functions: `abs`, `multiply`, `divide`, `sqrt`, random number generation.  
- **String.jack** – String abstraction: constructors, length, char access, append, erase, int-to-string conversion.  
- **Array.jack** – Array abstraction: heap allocation & deallocation of arrays.  
- **Memory.jack** – Memory access primitives: `peek`, `poke`, allocation and deallocation.  
- **Screen.jack** – Graphics primitives: draw pixels, rectangles, lines, clear screen.  
- **Output.jack** – Text output functions: print characters, strings, integers; cursor management.  
- **Keyboard.jack** – Input functions: key press codes, read character, read line, read integer.  
- **Sys.jack** – System bootstrap: program entry (`init`), halt, wait/delay.  
- **AUTHORS** – Project authorship information.

---

## Features
- Provides a **standard runtime library** for Jack programs.  
- Each class abstracts VM instructions into high-level APIs.  
- Enables Jack programs to use I/O, math, strings, memory, graphics, and system services.

---

## Build & Run
The `.jack` files are compiled using the Jack compiler into `.vm` files:  

```bash
JackCompiler Math.jack
```

The generated `.vm` files are then translated to Hack assembly using the VM Translator, and finally executed on the Hack platform (CPU emulator).

---

## Example (String.jack)
```jack
let s = String.new(10);
do s.appendChar('H');
do s.appendChar('i');
do Output.printString(s);
```

Output:
```
Hi
```

---

## License
This project follows the [CC BY-NC-SA 3.0 License](https://creativecommons.org/licenses/by-nc-sa/3.0/) as required by the Nand2Tetris course materials.
