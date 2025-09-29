// Activation.h
#ifndef ACTIVATION_H
#define ACTIVATION_H
#include <cmath>

#include "Matrix.h"



namespace activation {
    // Applies ReLU (Rectified Linear Unit)
    // activation function to a matrix
    Matrix relu(const Matrix& mat);
    // Applies softmax activation function to a matrix
    Matrix softmax(const Matrix& mat);

    // Function pointer type for activation functions
    typedef Matrix (*activation_func)(const Matrix&);
} // namespace activation








#endif