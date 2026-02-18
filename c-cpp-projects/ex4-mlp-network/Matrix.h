// Matrix.h
#ifndef MATRIX_H
#define MATRIX_H
#include <complex>
#include <iostream>
#include <math.h>
#include <bits/ranges_base.h>
#include <bits/range_access.h>
#define LIMIT 0.001


// You don't have to use the struct. Can help you with MlpNetwork.h
struct matrix_dims {

    int rows, cols;
};

class Matrix {
private:
    matrix_dims data_;  // Matrix dimensions
    float* matrix_;  // Array to store matrix values

    // Swaps two rows in the matrix
    void swapRows(int row1, int row2);

public:
    // Constructor: creates a matrix of specified dimensions,
    // initialized to zero
    Matrix(int rows, int cols);

    // Default constructor: creates a 1x1 matrix with value 1
    Matrix();

    // Copy constructor
    Matrix(const Matrix& other);

    // Destructor: frees allocated memory
    ~Matrix();

    // Transpose the matrix
    Matrix& transpose();

    // Vectorize the matrix (convert to a single column)
    Matrix& vectorize();

    // Print the matrix values
    void plain_print();

    // Get the number of rows
    int get_rows() const;

    // Get the number of columns
    int get_cols() const;

    // Element-wise multiplication
    Matrix dot(const Matrix& other) const;

    // Returns the Euclidean norm of the matrix
    float norm() const;

    // Reduces the matrix to its reduced row echelon form
    Matrix rref() const;

    // Returns the index of the maximum value in the matrix
    int argmax() const;

    // Returns the sum of all elements in the matrix
    float sum() const;

    // Adds another matrix to this matrix
    Matrix& operator+=(const Matrix& other);

    // Adds two matrices
    Matrix operator+(const Matrix& other) const;

    // Assignment operator
    Matrix& operator=(const Matrix& other);

    // Matrix multiplication
    Matrix operator*(const Matrix& other) const;

    // Scalar multiplication
    Matrix operator*(float c) const;

    // Access matrix element by row and column
    float& operator()(int row, int col);
    const float& operator()(int row, int col) const;

    // Access matrix element by index (linearized)
    float& operator[](int location);
    const float& operator[](int location) const;

    // Print matrix image to output stream
    friend std::ostream& operator<<(std::ostream& os, const Matrix& matrix);

    // Read matrix data from input stream
    friend std::istream& operator>>(std::istream& is, Matrix& matrix);
};

// Scalar multiplication (commutative)
Matrix operator*(float c, const Matrix& matrix);



#endif //MATRIX_H