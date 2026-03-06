# Stochastic Text Generator — Markov Chain Engine

A generic Markov chain library in C, driving two applications: a probabilistic tweet generator and a Snakes & Ladders board-game simulator.

---

## Problem Solved

Build a **reusable, type-agnostic Markov chain engine** capable of modelling any sequential stochastic process. Demonstrate its generality by powering two completely different applications — natural-language generation and board-game simulation — without modifying the core library.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Type-generic data structure in C (no templates) | Node payload is `void*`; behaviour injected via function-pointer struct (`copy`, `free`, `compare`, `print`, `is_last`) |
| Frequency-weighted random walks | Each node maintains a frequency list; next state sampled proportionally to transition counts |
| Sentence boundary detection | Tokens ending with `.` are marked as terminal — random walk never continues past them |
| Memory safety | Dedicated `free_database()` traverses the linked list and releases every node and its frequency list |

---

## Architecture

```
markov_chain.h / markov_chain.c   — Generic engine (LinkedList of MarkovNodes, frequency lists, random walk)
tweets_generator.c                — Application 1: corpus → word n-gram chain → random tweets
snakes_and_ladders.c              — Application 2: 100-cell board → Markov transitions → game simulation
```

**Core data structures:**
```c
typedef struct MarkovNode {
    void            *data;
    MarkovNodeData  *frequency_list; // weighted edges to successor states
    int              freq_list_size;
} MarkovNode;

typedef struct MarkovChain {
    LinkedList *database;
    // -- function pointers for generic behaviour --
    void (*print_func)(void *);
    int  (*comp_func)(void *, void *);
    void (*free_data)(void *);
    void *(*copy_func)(void *);
    bool (*is_last)(void *);
} MarkovChain;
```

---

## Applications

### Tweets Generator
Reads a text corpus, builds a word-level Markov chain with transition probabilities, and generates random tweets (≤ 20 words, ending at `.`).

```bash
gcc -std=c11 -Wall -Wextra -O2 tweets_generator.c markov_chain.c -o tweets
./tweets 42 5 corpus.txt 1000
# Tweet 1: the cat sat on the mat .
```

### Snakes & Ladders Simulator
Models a 100-cell board where dice rolls are uniform random transitions and snakes/ladders are deterministic jumps. Generates and prints random full-game walks.

```bash
gcc -std=c11 -Wall -Wextra -O2 snakes_and_ladders.c markov_chain.c -o snakes
./snakes 42 3
# Random Walk 1: [1] -> [6] -> [ladder-to-30] -> ... -> [100]
```

Or build both with `make`.

---

## Tech Stack & Concepts

- **Language:** C (C11)
- **Build:** Make + gcc
- **Key concepts:** Generic programming via function pointers, stochastic modelling, linked list, frequency-weighted sampling, memory management
