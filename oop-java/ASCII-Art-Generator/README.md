# ASCII Art Generator

A Java image-to-ASCII pipeline that converts bitmap images into character art, with an interactive shell for adjusting charset, resolution, and output target (console or HTML).

---

## Problem Solved

Convert a raster image to ASCII art by dividing it into sub-images, computing a brightness value for each region, and mapping that brightness to the most visually similar character from a configurable set — with pluggable output renderers and rounding strategies.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Sub-image brightness computation | `ImagePrepration` pads the image to the nearest power-of-two dimensions, then divides it into equal-size sub-regions; `CharConverter` computes the average greyscale brightness of each region normalised to [0,1] |
| Character brightness normalisation | `SubImgCharMatcher` pre-computes the brightness of each character in the charset and normalises all values to [0,1]; brightness-to-char mapping is a simple nearest-neighbour lookup |
| Pluggable rounding strategies | `RoundingMethod` interface with `UpRound` and `DownRound` implementations lets callers choose how brightness ties are broken |
| Multiple output targets | `AsciiOutput` interface with `ConsoleAsciiOutput` (prints to stdout) and `HtmlAsciiOutput` (writes a styled HTML file) |
| Interactive shell | `Shell` parses user commands (`add`, `remove`, `res`, `image`, `output`, `asciiArt`) with validation and descriptive error messages via custom exceptions |

---

## Architecture

```
ascii_art/
├── Shell                  — interactive REPL
├── AsciiArtAlgorithm      — conversion orchestration
└── AsciiArtConstant       — shared constants

image/
├── Image                  — pixel grid and brightness extraction
└── ImagePrepration        — padding, sub-image division

image_char_matching/
├── SubImgCharMatcher      — charset brightness index + nearest-char lookup
├── CharConverter          — character → brightness value
└── RoundingMethod / UpRound / DownRound — pluggable rounding

ascii_output/
├── AsciiOutput            — output interface
├── ConsoleAsciiOutput     — stdout renderer
└── HtmlAsciiOutput        — HTML file renderer
```

---

## Design Patterns

| Pattern | Where Applied |
|---|---|
| Strategy | `RoundingMethod` interface — `UpRound`/`DownRound` injected at runtime |
| Template Method | `AsciiArtAlgorithm` defines the conversion flow; subclasses override specific steps |
| Factory | Output target selected by string name in `Shell` |

---

## Tech Stack & Concepts

- **Language:** Java (SE)
- **Key concepts:** Image sub-sampling, brightness normalisation, strategy pattern, template method, custom exceptions, interactive CLI design

---

## Run

```bash
java ascii_art.Shell
```

Shell commands:
```
image <path>        — load image
chars               — show current charset
add <char|range|all>
remove <char|range|all>
res <up|down>       — increase/decrease resolution
output <console|html>
asciiArt            — render
```
