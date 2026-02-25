# MLP Neural Network (C++) – Intro2CS Assignment

Implementation of a simple feedforward **Multi-Layer Perceptron (MLP)** for digit classification.  
The network uses 4 fully connected (Dense) layers with ReLU and Softmax activations.

---

## Files
- **Matrix.h / Matrix.cpp** – Matrix class with linear algebra operations (construction, transpose, vectorize, multiplication, RREF, argmax, sum, etc.).【193†source】【194†source】
- **Activation.h / Activation.cpp** – Implements activation functions: ReLU and Softmax.【197†source】【198†source】
- **Dense.h / Dense.cpp** – Dense (fully connected) layer: applies weights, bias, and activation function to an input matrix.【192†source】【199†source】
- **MlpNetwork.h / MlpNetwork.cpp** – Defines the 4-layer MLP, applies layers in sequence, outputs predicted digit and probability.【195†source】【196†source】

---

## Architecture
- Input: 28×28 grayscale image (flattened to 784 values).【196†source】
- Layer 1: Dense(128, ReLU)
- Layer 2: Dense(64, ReLU)
- Layer 3: Dense(20, ReLU)
- Layer 4: Dense(10, Softmax) → prediction (digit 0–9)【196†source】

---

## Build
Compile with a C++11+ compiler. Example with `g++`:
```bash
g++ -std=c++11 -Wall -Wextra -Werror -O2 Matrix.cpp Dense.cpp Activation.cpp MlpNetwork.cpp -o mlp
```

---

## Usage
1. Load trained weights and biases into `Matrix` objects (from binary files).【194†source】
2. Construct the network:
```cpp
Matrix weights[MLP_SIZE], biases[MLP_SIZE];
// ... load data ...
MlpNetwork mlp(weights, biases);
```
3. Apply the network to an input image:
```cpp
Matrix img(28, 28);
std::cin >> img;      // read raw data into matrix
digit result = mlp(img.vectorize());
std::cout << "Digit: " << result.value << " Probability: " << result.probability << std::endl;
```

---

## Example Output
```
Digit: 7 Probability: 0.9823
```

---

## Notes
- `Matrix` operator overloads allow natural math expressions: `weights * input + bias`.【193†source】
- `relu` replaces negatives with 0; `softmax` exponentiates and normalizes to probabilities.【197†source】
- `MlpNetwork::operator()` applies all 4 layers and returns the best digit + probability.【195†source】

---

## License
Educational use. Add a license if you plan to publish broadly.
