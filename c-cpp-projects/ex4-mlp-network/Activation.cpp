#include <stdio.h>
#include "Activation.h"

Matrix activation::relu(const Matrix& mat) {
    Matrix result = mat;
    for(int i = 0; i < result.get_cols()*result.get_rows(); i++) {
        if(result[i]<0) {
            result[i] = 0;
        }

    }
    return result;
}

Matrix activation::softmax(const Matrix& mat) {
    Matrix result = mat;
    float sum = 0;
    // Compute the sum of exp(x) for each element
    for(int i = 0; i < result.get_cols()*result.get_rows(); i++) {
        sum += std::exp(result[i]);

    }
    // Divide each element by the sum of exponentials to normalize
    for(int i = 0; i < result.get_cols()*result.get_rows(); i++) {
        result[i] = std::exp(result[i])/sum;

    }
   return result;
}

