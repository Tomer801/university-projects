#include <cstdio>
#include "Dense.h"


// Constructor: initializes the Dense layer with the given weights,
// biases, and activation function
Dense::Dense(const Matrix& weights,
    const Matrix &bias,activation::activation_func a_func)
:weights_{weights} , bias_{bias} , a_func_{a_func} {

}
// Return reference to the weights matrix
const Matrix& Dense::get_weights() const {
    return weights_;
}
// Return reference to the bias matrix
const Matrix& Dense::get_bias() const {
    return bias_;
}
// Return the activation function
activation::activation_func Dense::get_activation() const {
    return a_func_;
}

Matrix& Dense::operator()(Matrix& input) {

    input = weights_*input + bias_;
    input = a_func_(input);
    return input;
}
Matrix Dense::operator()(const Matrix& input) const {
    Matrix copy = input; // Make a copy of the input matrix
    copy = weights_*copy + bias_;
    copy = a_func_(copy);
    return copy;
}
