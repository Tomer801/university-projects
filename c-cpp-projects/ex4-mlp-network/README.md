# Handwritten Digit Classifier — MLP from Scratch

A 4-layer feedforward neural network for MNIST digit recognition implemented entirely in C++, with no machine-learning libraries. All linear algebra is hand-rolled using a custom `Matrix` class.

---

## Problem Solved

Implement inference for a pre-trained Multi-Layer Perceptron that classifies 28×28 grayscale handwritten digits (0–9), building every component — matrix operations, activation functions, dense layers, and the sequential forward pass — from first principles in C++.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Matrix algebra without a library | `Matrix` class implements multiplication (`operator*`), addition (`operator+`), transpose, vectorize, argmax, and scalar operations with full operator overloading |
| ReLU activation | Element-wise `max(0, x)` via `operator()` on a `Matrix`; preserves dimensions |
| Softmax activation | Numerically stable: shift by max value before exponentiation, then normalize to sum = 1 |
| Layer abstraction | `Dense` encapsulates weight matrix, bias vector, and activation; `operator()` performs the full `activation(W*x + b)` forward pass |
| Digit prediction | `MlpNetwork::operator()` chains all 4 layers and returns a `digit` struct with predicted value and confidence probability |

---

## Network Architecture

```
Input (28×28 image) → vectorize → [784]
  Dense(784 → 128, ReLU)
  Dense(128 →  64, ReLU)
  Dense( 64 →  20, ReLU)
  Dense( 20 →  10, Softmax)
Output: digit ∈ {0..9}, probability ∈ [0,1]
```

---

## Class Hierarchy

| Class | Responsibility |
|---|---|
| `Matrix` | Linear algebra primitives; operator overloading for natural math notation |
| `Activation` | Stateless functors: `relu(Matrix)`, `softmax(Matrix)` |
| `Dense` | Fully connected layer: holds `W`, `b`, activation function; applies forward pass |
| `MlpNetwork` | Sequential container of 4 `Dense` layers; entry point is `operator()(Matrix img)` |

---

## Tech Stack & Concepts

- **Language:** C++ (C++11)
- **Build:** `g++ -std=c++11 -Wall -Wextra -Werror -O2`
- **Key concepts:** Operator overloading, const-correctness, matrix algebra, ReLU/Softmax activations, feedforward inference

---

## Build & Run

```bash
g++ -std=c++11 -Wall -Wextra -Werror -O2 \
    Matrix.cpp Dense.cpp Activation.cpp MlpNetwork.cpp -o mlp

# Feed a 28x28 image (784 float values) via stdin
./mlp < image.bin
# Output: Digit: 7  Probability: 0.9823
```
