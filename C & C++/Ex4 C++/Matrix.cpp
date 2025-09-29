#include "Matrix.h"





Matrix::Matrix(int rows, int cols)
    :data_{rows, cols}{
         if (rows <= 0 || cols <= 0) {
             throw std::invalid_argument("Matrix dimensions must be greater than zero");
         }

         matrix_ = new float[rows * cols];

         for (int i = 0; i < rows * cols; ++i) {
             matrix_[i] = 0;
         }
     }
void Matrix::swapRows(int row1, int row2) {
    for (int col = 0; col < data_.cols; ++col) {
        std::swap(matrix_[row1 * data_.cols + col], matrix_[row2 * data_.cols + col]);
    }
}

Matrix::Matrix()
: data_{1, 1}
{
    matrix_ = new float[1];
    matrix_[0] = 0;
}
//privet method for swaping rows
Matrix::Matrix(const Matrix& other):data_{other.data_.rows,other.data_.cols} ,
                        matrix_ {new float[other.data_.rows*other.data_.cols]}
{
    for (int i = 0; i < data_.rows * data_.cols; i++) {
        matrix_[i] = other.matrix_[i];
    }
}

Matrix::~Matrix() {
    delete[] matrix_;
}


int Matrix::get_rows() const {
    return data_.rows;
}


int Matrix::get_cols() const{
    return data_.cols;
}


Matrix& Matrix::transpose() {
    // Store the original number of rows
    int rows = data_.rows;
    // Allocate memory for the transposed matrix
    float* transpose_ = new float[data_.rows * data_.cols];

    // Fill the transposed matrix with swapped indices
    for (int i = 0; i < data_.rows ; i++) {
        for (int j = 0; j < data_.cols; j++)
        {

            transpose_[(j)*data_.rows + i ] = matrix_[(data_.cols)*i + j];
        }
    }
    // Free the memory of the original matrix
    delete[] matrix_;
    // Swap rows and columns
    data_.rows = data_.cols;
    data_.cols = rows;
    // Update the matrix pointer
    matrix_ = transpose_;
    return *this;

    }


Matrix& Matrix::vectorize() {
    data_.rows *= data_.cols; // Set the number of rows to the total number of elements
    data_.cols = 1;// Set the number of columns to 1

    return *this;
}



void Matrix::plain_print() {
    for (int i = 0; i < data_.rows; i++) {
        for (int j = 0; j < data_.cols; j++) {
            // Print each element followed by a space
            std::cout << matrix_[i * data_.cols + j] << " ";

        }
        std::cout << std::endl;
    }
}


Matrix Matrix::dot(const Matrix& other) const {
     if(data_.cols != other.data_.cols || data_.rows != other.data_.rows) {
         throw std::invalid_argument("Matrix does not have same size");
     }
    // Create a new matrix for the result
    Matrix mult_matrix = Matrix(data_.rows, other.data_.cols);
    // Perform element-wise multiplication
    for (int i = 0; i < data_.rows*data_.cols; i++) {
        mult_matrix.matrix_[i] = matrix_[i] * other.matrix_[i];

    }
    return mult_matrix;
}


float Matrix::norm() const {
    float sum = 0;
    for (int i = 0; i < data_.rows*data_.cols; i++)
        {
         sum += (matrix_[i] * matrix_[i]);

        }
    sum = sqrt(sum);
    return sum;
}

Matrix Matrix::rref() const {
    // Copy the matrix to preserve the original
     Matrix copy_mat = Matrix(*this);
     int rows = copy_mat.data_.rows;
     int cols = copy_mat.data_.cols;
    // Leading column index
     int lead = 0;
     for (int r = 0; r < rows; ++r) {
         if (lead >= cols) {
             // Return if reached the last column
             return copy_mat;
         }
         int i = r;

         // Find a non-zero leading element
         while (std::abs(copy_mat(i, lead)) < LIMIT) {
             ++i;
             if (i == rows) {
                 i = r;
                 ++lead;
                 if (lead == cols) {
                     // Return if no leading element found
                     return copy_mat;
                 }
             }
         }

         // Swap rows to bring the leading element to the top
         copy_mat.swapRows(i, r);

         // Normalize the row by dividing by the leading value
         float lv = copy_mat(r, lead);
         for (int j = 0; j < cols; ++j) {
             copy_mat(r, j) /= lv;
         }

         // Subtract the normalized row from other rows to zero out the leading column
         for (int k = 0; k < rows; ++k) {
             if (k != r) {
                 float factor = copy_mat(k, lead);
                 for (int j = 0; j < cols; ++j) {
                     copy_mat(k, j) -= factor * copy_mat(r, j);
                 }
             }
         }
         ++lead;    // Move to the next column
     }

     return copy_mat;
 }

int Matrix::argmax() const
   {
    float max = matrix_[0];
    int idx = 0;

    for (int i = 0; i < data_.rows*data_.cols; i++)
        {
        if (matrix_[i] > max)
                {
                max = matrix_[i];
                idx = i;
                }
        }
        return idx;
   }
float Matrix::sum() const {
        float sum = 0;
        for (int i = 0; i < data_.rows*data_.cols; i++)
        {
            sum += matrix_[i] ;

        }
        return sum;

}

//return reference for chaning
Matrix& Matrix::operator+=(const Matrix& other) {
        if (data_.rows != other.data_.rows || data_.cols != other.data_.cols) {
          throw std::invalid_argument("Matrix dimensions do not match for addition.");
         }
        for (int i = 0; i < data_.rows * data_.cols; ++i) {
            matrix_[i] += other.matrix_[i];
        }
        return *this;
    }

Matrix Matrix::operator+(const Matrix& other) const {
        if (data_.rows != other.data_.rows || data_.cols != other.data_.cols) {
            throw std::invalid_argument("Matrix dimensions do not match for addition.");
        }
        Matrix copy_matrix = Matrix(other);
        for (int i = 0; i < data_.rows * data_.cols; ++i) {
            copy_matrix.matrix_[i] = matrix_[i] + other.matrix_[i];
        }
        return copy_matrix;
    }

Matrix& Matrix::operator=(const Matrix& other) {
    // Handle self-assignment
    if (this == &other) {
        return *this;
    }
    float* temp = new float[other.data_.rows * other.data_.cols];// Allocate new memory
    delete[] matrix_;  // Free existing memory

    data_ = other.data_;  // Copy dimensions

    for (int i = 0; i < data_.rows * data_.cols; ++i) {
        temp[i] = other.matrix_[i];  // Copy data
    }
    matrix_ = temp;
    return *this;
}
Matrix Matrix::operator*(const Matrix& other) const {
     if (data_.cols != other.data_.rows) {
         throw std::invalid_argument("Matrix dimensions do not match for multiplication.");
     }
     int k = 0;
     //sum of result(i,j)
     float sum = 0;
     Matrix mat = Matrix(data_.rows, other.data_.cols);
     int rows = data_.rows;
     int cols = other.data_.cols;
     for (int i = 0; i < rows; ++i) {
         for (int j = 0; j < cols; ++j) {

             while (k < data_.cols) {
                 sum += matrix_[i * data_.cols + k] * other.matrix_[j+other.data_.cols*k];
                 k++;
             }

             mat.matrix_[i*cols + j] = sum ;
             k = 0;
             sum = 0;

         }
     }
     return mat;
 }

//scelar
Matrix Matrix::operator*(float c) const {
    Matrix copy_matrix = Matrix(*this);

    for (int i = 0; i < data_.rows * data_.cols; ++i) {
        copy_matrix.matrix_[i] *= c;
    }
    return copy_matrix;
}

// calls operator * with c to mult from left (none member)
Matrix operator*(float c ,const Matrix& matrix) {
        return matrix * c;
    }

float& Matrix::operator()(int row, int col) {
        if (row < 0 || row >= data_.rows || col < 0 || col >= data_.cols) {
            throw std::out_of_range("Matrix index out of bounds.");
        }
        return matrix_[row * data_.cols + col];
    }

const float& Matrix::operator()(int row, int col) const {
    if (row < 0 || row >= data_.rows || col < 0 || col >= data_.cols) {
        throw std::out_of_range("Matrix index out of bounds.");
    }
    return matrix_[row * data_.cols + col];
}


float& Matrix::operator[](int location) {
    if (location < 0 || location >= data_.rows * data_.cols) {
        throw std::out_of_range("Index out of bounds.");
    }
    return matrix_[location];
}


const float& Matrix::operator[](int location) const {
    if (location < 0 || location >= data_.rows * data_.cols) {
        throw std::out_of_range("Index out of bounds.");
    }
    return matrix_[location];
}

std::ostream& operator<<(std::ostream& os, const Matrix& matrix) {
    for (int i = 0; i < matrix.data_.rows; ++i) {
        for (int j = 0; j < matrix.data_.cols; ++j) {
            if (matrix(i,j)>0.1){
                os << "**";
            }
            else {
                os<<"  ";
            }
        }
        os<<"\n";
    }
    return os;


    }
std::istream& operator>>(std::istream& is, Matrix& matrix) {
    if (!(is.good())) {
        throw std::runtime_error("Error: File could not be opened.");
    }

    if (!(is.read(reinterpret_cast<char*>(matrix.matrix_),
        matrix.data_.rows * matrix.data_.cols*sizeof(float)))) {
        throw std::invalid_argument("Error reading matrix.");
    }
    return is;
}



