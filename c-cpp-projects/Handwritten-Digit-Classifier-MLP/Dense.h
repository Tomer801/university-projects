// Dense.h
#ifndef DENSE_H
#define DENSE_H

#include "Activation.h"


class Dense {
private:
      // Matrix of weights
      Matrix weights_;
      // Matrix of biases
      Matrix bias_;
      // Activation function (ReLU or softmax)
      activation::activation_func a_func_;


public:
      // Constructor: initializes the weights, bias, and activation function
      Dense(const Matrix& weights, const Matrix& bias,
            activation::activation_func a_func);
      // Getter for weights
      const Matrix& get_weights() const;
      // Getter for bias
      const Matrix& get_bias() const;
      // Getter for activation function
      activation::activation_func get_activation() const;
      // Applies the dense layer transformation
      // to the input matrix (non-const version)
      Matrix& operator()(Matrix& input);
      // Applies the dense layer transformation
      // to the input matrix (const version)
      Matrix operator()(const Matrix& input) const;


};




#endif //DENSE_H