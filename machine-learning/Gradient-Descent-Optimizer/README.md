# Gradient Descent Optimizer & Logistic Regression

A modular gradient descent engine with pluggable learning-rate schedules and L1/L2 regularisation, applied to logistic regression on a real-world cardiovascular dataset with ROC/AUC evaluation.

---

## Problem Solved

Build a configurable gradient descent optimiser that supports any differentiable objective via a module interface, then use it to train regularised logistic regression classifiers and evaluate them with ROC analysis and threshold tuning.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Pluggable objectives | `GradientDescent` accepts any `BaseModule` — exposes `compute_output()` and `compute_jacobian()` — making the optimiser objective-agnostic |
| Learning rate schedules | `FixedLR(η)` and `ExponentialLR(η₀, decay)` implement a common `lr_schedule` interface; rate is queried each iteration |
| Regularisation | `RegularizedModule` sums fidelity loss with an `L1` or `L2` penalty term; gradient is computed by chain rule |
| Logistic regression | `LogisticModule` computes `log(1 + exp(−y·wᵀx))` loss and its gradient; probability prediction via sigmoid |
| ROC & AUC | Sweeps classification threshold α ∈ [0,1]; plots TPR vs. FPR curve and reports area under curve |
| Hyperparameter tuning | Cross-validation over λ ∈ {0.001, 0.01, 0.1, 1, 10}; selects λ minimising mean validation misclassification error |

---

## Module Overview

| File | Responsibility |
|---|---|
| `gradient_descent.py` | Core optimiser: step loop, stopping criterion, per-iteration callbacks |
| `learning_rate.py` | `FixedLR`, `ExponentialLR` rate schedule implementations |
| `modules.py` | `L1`, `L2`, `LogisticModule`, `RegularizedModule` — all implement `compute_output` + `compute_jacobian` |
| `logistic_regression.py` | Estimator wrapping gradient descent for classification; supports `none`/`l1`/`l2` penalty |
| `gradient_descent_investigation.py` | Experiments: LR comparison, SA Heart Disease dataset, ROC curves |

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Libraries:** NumPy, Matplotlib, Pandas
- **Key concepts:** Gradient descent, learning rate schedules, logistic regression, L1/L2 regularisation, ROC curves, AUC, cross-validation

---

## Run

```bash
python gradient_descent_investigation.py
```

Generates convergence plots (loss vs. iteration), a ROC curve, and cross-validation error curves.

**Key empirical findings:**
- `η = 0.1` converges fastest; `η = 1.0` causes divergence
- Optimal L1 penalty strength λ ≈ 0.01 on the SAheart dataset
- Logistic regression with tuned λ achieves significantly higher AUC than random baseline
