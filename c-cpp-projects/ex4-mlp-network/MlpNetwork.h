#ifndef MLPNETWORK_H
#define MLPNETWORK_H

#include "Dense.h"

#define MLP_SIZE 4

/**
 * @struct digit
 * @brief Identified (by Mlp network) digit with
 *        the associated probability.
 * @var value - Identified digit value
 * @var probability - identification probability
 */
typedef struct digit {
	unsigned int value;
	float probability;
} digit;

const matrix_dims img_dims = {28, 28};
const matrix_dims weights_dims[] = {{128, 784},
									{64,  128},
									{20,  64},
									{10,  20}};
const matrix_dims bias_dims[] = {{128, 1},
								 {64,  1},
								 {20,  1},
								 {10,  1}};
class MlpNetwork {
private:
	// List of weight matrices for each layer
	Matrix weights_list_[MLP_SIZE];
	// List of bias matrices for each layer
	Matrix bias_list_[MLP_SIZE];
	// Array of Dense layers
	const Dense layers[MLP_SIZE];

public:
	// Constructor: initializes the network
	// with weights and biases for each layer
	MlpNetwork(const Matrix (&weights_list)[MLP_SIZE],
		const Matrix (&bias_list)[MLP_SIZE]);
	// Applies the network to an input matrix and
	// returns the predicted digit and probability
	digit operator()(Matrix& input);
	// Const version of the network operation
	digit operator()(const Matrix& input) const;




};

#endif