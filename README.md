# Software Engineering Portfolio
### B.Sc. Computer Science — The Hebrew University of Jerusalem

A curated collection of systems, algorithms, and software engineering projects spanning five core disciplines. Every project is implemented from scratch, with emphasis on correctness, design clarity, and engineering depth.

---

## Table of Contents

- [Systems Programming & OS](#systems-programming--os)
- [Computer Architecture — Nand to Tetris](#computer-architecture--nand-to-tetris)
- [Object-Oriented Design — Java](#object-oriented-design--java)
- [Machine Learning & Data Science](#machine-learning--data-science)
- [Algorithms & Fundamentals](#algorithms--fundamentals)
- [Tech Stack](#tech-stack)
- [CV Strategy](#cv-strategy)

---

## Systems Programming & OS

> **Languages:** C / C++ &nbsp;|&nbsp; **Themes:** Memory management, concurrency, OS internals

| Project | Description | Key Concepts |
|---|---|---|
| [Multithreaded MapReduce Framework](operating-systems/map-reduce-framework/) | Generic parallel data-processing framework modelled after Google's MapReduce | `pthreads`, lock-free atomics, barrier sync, work-stealing |
| [User-Level Thread Scheduler](operating-systems/ex2-user-level-threads/) | Preemptive round-robin user-space threading library built without kernel threads | `sigsetjmp/siglongjmp`, `SIGVTALRM`, context switching, quantum accounting |
| [Handwritten Digit Classifier — MLP](c-cpp-projects/ex4-mlp-network/) | 4-layer neural network for MNIST digit recognition, built from raw linear algebra | Matrix ops, ReLU, Softmax, operator overloading, no ML libraries |
| [Collaborative Filtering Recommendation Engine](c-cpp-projects/ex5-movie-recommender/) | Content-based + collaborative filtering movie recommender with cosine similarity | KNN, cosine similarity, `shared_ptr`, file I/O |
| [Stochastic Text Generator](c-cpp-projects/ex3-markov-chain/) | Generic Markov chain engine powering both tweet generation and board-game simulation | Generic C via function pointers, stochastic modelling, linked lists |
| [Sorting Algorithms Benchmark](c-cpp-projects/ex2-bus-lines/) | Side-by-side bubble sort vs. quicksort with a full validation suite | Quicksort, partition, comparators, unit testing in C |
| [Caesar Cipher CLI](c-cpp-projects/ex1-cipher/) | Modular encode/decode tool with file I/O and test harness | Modular arithmetic, CLI argument parsing, unit testing |

---

## Computer Architecture — Nand to Tetris

> **Languages:** Python / Jack &nbsp;|&nbsp; **Theme:** Build a fully working computer from logic gates to a high-level language

This track implements every layer of the computing stack — from binary instruction encoding all the way up through a compiler for the Jack object-oriented language.

| Project | Layer | Description |
|---|---|---|
| [Jack OS Standard Library](nand-to-tetris/ex12-jack-stdlib/) | OS | Memory allocator, screen graphics, keyboard I/O, string manipulation, math library |
| [Jack Language Compiler](nand-to-tetris/ex10-11-jack-compiler/) | Compiler | Recursive-descent compiler: lexer → parser → two-scope symbol table → VM code emission |
| [Stack-Based VM Translator](nand-to-tetris/ex7-8-vm-translator/) | VM | Translates stack-based bytecode to Hack assembly; handles arithmetic, branching, and function calls |
| [Hack Assembler](nand-to-tetris/ex6-hack-assembler/) | Assembler | Two-pass assembler with symbol resolution, produces 16-bit binary machine code |

---

## Object-Oriented Design — Java

> **Language:** Java &nbsp;|&nbsp; **Themes:** Design patterns, game engines, extensibility

| Project | Description | Patterns |
|---|---|---|
| [Infinite World Simulation — PEPSE](oop-java/ex4-infinite-world-game/) | Procedurally generated infinite side-scrolling world with an energy/physics system | Observer, Strategy, Factory |
| [Brick Breaker with Power-ups](oop-java/ex2-bricker-game/) | Full game engine with 6 distinct collision-driven power-up strategies | Strategy, Factory, Composition |
| [ASCII Art Generator](oop-java/ex3-ascii-art-generator/) | Image-to-ASCII pipeline with adjustable resolution and console/HTML output | Strategy, Template Method, Factory |
| [TicTacToe Tournament Framework](oop-java/ex1-tictactoe-tournament/) | Extensible game framework supporting human vs. AI tournament scheduling | Factory, Strategy, Abstract Class |

---

## Machine Learning & Data Science

> **Language:** Python 3 &nbsp;|&nbsp; **Libraries:** NumPy, scikit-learn, Matplotlib, Plotly

| Project | Description | Techniques |
|---|---|---|
| [Gradient Descent Optimizer](machine-learning/ex4-gradient-descent/) | Configurable optimizer with fixed/exponential learning-rate schedules, L1/L2 penalties, and ROC analysis | Logistic regression, LR schedules, AUC/ROC |
| [AdaBoost & Model Selection](machine-learning/ex3-adaboost-model-selection/) | Boosting ensemble + k-fold cross-validation pipeline comparing Ridge vs. Lasso | AdaBoost, decision stumps, k-fold CV, regularization |
| [Decision Tree Classifier](machine-learning/ex2-decision-trees/) | Entropy-based decision tree with pruning capability | Information gain, entropy, Gini impurity |
| [Regression Analysis Suite](machine-learning/ex1-regression/) | OLS and polynomial regression on real-world housing and climate datasets | OLS, Vandermonde matrices, feature engineering, MSE |

---

## Algorithms & Fundamentals

> **Language:** Python 3 &nbsp;|&nbsp; **Theme:** Algorithmic problem-solving and program design

| Project | Description | Techniques |
|---|---|---|
| [Constraint Satisfaction Puzzle Solver](intro-to-cs/ex8-puzzle-solver/) | Backtracking solver for a visibility-constraint logic puzzle | Backtracking, constraint propagation, solution counting |
| [Rush Hour Sliding Puzzle](intro-to-cs/ex9-rush-hour-game/) | Full OOP implementation of the Rush Hour board game with JSON config | OOP design, state machines, move validation |
| [Image Processing Pipeline](intro-to-cs/ex6-image-editor/) | Grayscale, blur, resize, edge detection, and quantization built from first principles | Convolution, bilinear interpolation, adaptive thresholding |
| [Multi-Directional Word Search](intro-to-cs/ex5-word-search/) | 8-direction word search engine over character matrices | Matrix traversal, diagonal iteration |
| [Recursive Algorithms Library](intro-to-cs/ex7-recursion-exercises/) | Tower of Hanoi, logarithmic multiplication, digit analysis, 2D list comparison | Divide-and-conquer, tail recursion, structural recursion |
| [Battleship Game Engine](intro-to-cs/ex4-battleship/) | Terminal battleship with board-state machine and legal-move generation | 2D arrays, state machines, coordinate parsing |

---

## Tech Stack

| Domain | Languages | Tools & Libraries |
|---|---|---|
| Systems | C, C++ (C11 / C++17) | gcc, g++, pthreads, POSIX signals, Valgrind, Make |
| Scripting / Data | Python 3 | NumPy, scikit-learn, Matplotlib, Plotly, Pandas |
| Application / OOP | Java | Java SE (zero external dependencies) |
| Computer Architecture | Python, Jack | Nand2Tetris toolchain |

---

## CV Strategy

Three projects with the highest recruiting signal for systems and software engineering roles:

### 1. Multithreaded MapReduce Framework
Lock-free 64-bit atomic stage encoding, barrier synchronisation between phases, generic template-based client API, and load balancing through work-stealing atomics. Directly comparable to production concurrent-systems work.

### 2. User-Level Thread Scheduler
Extremely rare at the undergraduate level — implements OS scheduling primitives (preemption via `SIGVTALRM`, context save/restore via `sigsetjmp/siglongjmp`) entirely in user space, without kernel threads. Shows deep POSIX and CPU-state knowledge.

### 3. Jack Language Compiler
End-to-end pipeline from source text to VM bytecode: lexical analysis, recursive-descent parsing, a two-scope symbol table (class + subroutine), and on-the-fly code emission. Compilers are a top signal in systems and infrastructure interviews.

---

*Developed at The Hebrew University of Jerusalem, School of Computer Science and Engineering.*
