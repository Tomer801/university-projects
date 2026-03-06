# Caesar Cipher CLI

A command-line encode/decode tool implementing the classical Caesar cipher in C, with file I/O and a comprehensive unit-test harness.

---

## Problem Solved

Implement a portable, testable Caesar cipher that correctly handles positive and negative shift values, alphabet wrap-around (modulo 26), and preservation of all non-alphabetic characters — while exposing a clean, reusable library API.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Correct wrap-around for negative shifts | `((shift % 26) + 26) % 26` always produces a non-negative remainder |
| Preserving punctuation, digits, and spaces | `isalpha()` guard applied before any transformation |
| Upper vs. lower case | Offset arithmetic relative to `'A'` or `'a'` based on `isupper()` |
| Separation of concerns | `cipher.h/c` is a pure library; `main.c` owns CLI parsing; `tests.c` is standalone |

---

## API

```c
// Encrypt string s in-place with shift k (any integer)
void cipher(char s[], int k);

// Decrypt — equivalent to cipher(s, -k)
void decipher(char s[], int k);
```

---

## Tech Stack & Concepts

- **Language:** C (C11)
- **Build:** `gcc -std=c11 -Wall -Wextra -Werror -O2`
- **Key concepts:** Modular arithmetic, in-place string mutation, CLI argument parsing, unit testing without a framework

---

## Build & Run

```bash
gcc -std=c11 -Wall -Wextra -Werror -O2 main.c cipher.c tests.c -o caesar

# Encode with shift +3
./caesar cipher 3 input.txt output.txt

# Decode
./caesar decipher 3 output.txt restored.txt

# Run test suite
./caesar test
```

**Example:**
```
Input:   Hello, World!  xyz XYZ
Output:  Khoor, Zruog!  abc ABC
```
