# ASCII Art Generator – Java Project

Java implementation of an **ASCII Art Generator**, which converts images into ASCII text representations.  
The project supports multiple algorithms, commands, and a shell interface.

---

## Files
- **AsciiArtAlgorithm.java** – Interface/abstract base for ASCII art algorithms (defines how images are converted).  
- **AsciiArtConstant.java** – Constants used for ASCII generation (characters, scaling factors, etc.).  
- **Image.java** – Represents an input image (pixel grid, brightness extraction).  
- **ImagePrepration.java** – Prepares images for ASCII conversion (resizing, grayscale normalization).  
- **Command.java** – Represents commands available in the interactive shell.  
- **Shell.java** – Command-line shell for running ASCII art operations interactively.  
- **IncorrectFormatException.java / UserInputException.java** – Custom exceptions for invalid user input or file format errors.  
- **AbsRound.java** – Utility/helper for rounding operations in image processing.  

---

## Features
- Load an image and convert it into ASCII art.  
- Multiple conversion algorithms supported (different character sets, brightness scaling).  
- Interactive shell with commands for choosing algorithm, loading/saving images, adjusting parameters.  
- Error handling with descriptive exceptions for invalid inputs.  
- Modular design allows adding new algorithms by implementing `AsciiArtAlgorithm`.  

---

## Usage

### Build
Compile with `javac`:
```bash
javac *.java
```

### Run
Run the shell:
```bash
java Shell
```

Inside the shell, commands may include:
```
load image.png
set algorithm simple
render
save output.txt
```

---

## Example Output
Input (image of a cat):

```
 /\_/\ 
( o.o )
 > ^ <
```

ASCII output rendered with chosen character set.

---

## Strategies
The system uses the **strategy pattern** for ASCII conversion algorithms:  
- Each algorithm implements `AsciiArtAlgorithm`.  
- User can switch algorithms dynamically in the shell.  
- Example: simple brightness mapping vs. advanced dithering.  
- Makes it easy to extend the system with new ASCII art strategies.  

---

## License
Educational use. Add a license if you plan to publish broadly.
