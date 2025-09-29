#include "MlpNetwork.h"



MlpNetwork::MlpNetwork(const Matrix (&weights_list)[MLP_SIZE],
            const Matrix (&bias_list)[MLP_SIZE])
: layers{ Dense(weights_list[0], bias_list[0], activation::relu),
         Dense(weights_list[1], bias_list[1], activation::relu),
         Dense(weights_list[2], bias_list[2], activation::relu),
         Dense(weights_list[3], bias_list[3], activation::softmax) }
{
    for (int i = 0; i < MLP_SIZE; i++) {
        weights_list_[i] = weights_list[i];
        bias_list_[i] = bias_list[i];

    }
}

digit MlpNetwork::operator()(Matrix& input) {
    // Apply each layer
    for (int i = 0; i < MLP_SIZE; ++i) {
        input = layers[i](input);
    }
    // Create the digit struct with the value and probability
    unsigned int val = input.argmax();
    digit q =  {val,input[input.argmax()]};

    return q;
}

digit MlpNetwork::operator()(const Matrix& input) const{
    //copy
    Matrix copy = input;
    // Apply each layer
    for (int i = 0; i < MLP_SIZE; ++i) {
        copy = layers[i](copy);  // Reuse existing layers
    }
    // Create the digit struct with the value and probability
    unsigned int val = input.argmax();
    digit q =  {val,input[input.argmax()]};


    return q;
}