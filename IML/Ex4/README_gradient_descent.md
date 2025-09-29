# Gradient Descent & Logistic Regression

This repository contains implementations and experiments related to **Gradient Descent optimization**, **learning rate schedules**, and **Logistic Regression** with regularization.

---

## Files

- **gradient_descent.py** – Core implementation of the Gradient Descent algorithm. Supports fixed/decaying learning rates, stopping criteria, output type (last/best/average), and per-iteration callbacks【417†source】.
- **learning_rate.py** – Implements learning rate strategies: `FixedLR` and `ExponentialLR`【418†source】.
- **modules.py** – Modules for optimization:
  - `L1`, `L2` – Regularization terms (L1 = sum |w|, L2 = sum w²)【420†source】.
  - `LogisticModule` – Logistic regression loss and gradient.
  - `RegularizedModule` – Combines a fidelity term with a regularization term【420†source】.
- **logistic_regression.py** – Logistic Regression estimator. Supports penalties (`none`, `l1`, `l2`), threshold tuning, probability prediction, and misclassification loss【419†source】.
- **gradient_descent_investigation.py** – Experiments with gradient descent:
  - Compare fixed learning rates for L1/L2 modules.
  - Visualize convergence paths.
  - Train logistic regression on the South Africa Heart Disease dataset, tune λ via cross-validation, and plot ROC curves【416†source】.

---

## Features

### Gradient Descent
- Modular design: works with any `BaseModule` objective.
- Supports callback logging per iteration (weights, gradient, objective value, etc.).
- Flexible stopping criterion via tolerance.

### Logistic Regression
- Fits logistic regression models with/without L1/L2 regularization.
- Adjustable threshold α for classification decisions.
- Probability prediction via sigmoid.
- Performance evaluated using misclassification error【419†source】.

### Experiments
- **Fixed Learning Rate**: Demonstrates convergence for different step sizes.
- **ROC Curve & AUC**: Evaluates logistic regression classifier on heart disease dataset.
- **Regularization Selection**: Cross-validation for optimal λ in L1-penalized logistic regression【416†source】.

---

## Installation

Install dependencies:
```bash
pip install -r requirements.txt
```

---

## Usage

### Run gradient descent experiments:
```bash
python gradient_descent_investigation.py
```

This will:
- Compare fixed learning rates (L1/L2).
- Train logistic regression.
- Display convergence and ROC plots.

---

## Example Results

- Fixed LR η=0.1 converges fastest for both L1 and L2, while too large η diverges【416†source】.
- Logistic regression achieves optimal classification error with λ ≈ 0.01 for L1 penalty on the SAheart dataset.
- ROC AUC shows significant improvement over random guessing.

---

## License
Educational use only.
