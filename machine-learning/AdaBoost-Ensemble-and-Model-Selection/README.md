# AdaBoost Ensemble & Regularized Regression with Model Selection

A two-part project: (1) AdaBoost with decision-stump weak learners visualised on synthetic 2D data, and (2) k-fold cross-validation pipeline comparing Ridge, Lasso, and OLS regression on the Diabetes dataset.

---

## Problem Solved

**Part 1:** Show how boosting compounds weak learners into a strong classifier and visualise the evolution of decision boundaries and per-sample weights as the ensemble grows.

**Part 2:** Select the optimal regularisation strength (λ) for Ridge and Lasso via k-fold cross-validation and compare against unpenalised OLS on a real-world dataset.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| AdaBoost weight updates | `wᵢ ← wᵢ · exp(−αᵢ · yᵢ · h(xᵢ))` after each weak learner; normalised to sum = 1 each round |
| Decision stump training | Exhaustive threshold search over all feature values and both polarities; selects split minimising weighted misclassification error |
| Ridge regression (closed-form) | `β = (XᵀX + λI)⁻¹Xᵀy` — direct matrix inversion, no iterative solver |
| Lasso via sklearn | Wrapped in the `BaseEstimator` interface for uniform k-fold evaluation |
| Cross-validation pipeline | `cross_validate()` splits data into k folds, trains on k−1, evaluates on holdout; returns mean train/validation error |
| Visualisation | Decision surfaces via Plotly; sample weight magnitude encoded by marker size |

---

## Module Overview

| File | Responsibility |
|---|---|
| `base_estimator.py` | Abstract base class: `fit`, `predict`, `loss` |
| `decision_stump.py` | Weak learner: weighted threshold classifier |
| `adaboost_scenario.py` | Boosting experiments on synthetic noisy/noiseless 2D data |
| `estimators.py` | `LinearRegression`, `RidgeRegression` (closed-form), `Lasso` (sklearn wrapper) |
| `cross_validate.py` | Generic k-fold CV returning per-fold train + validation errors |
| `perform_model_selection.py` | Diabetes dataset: Ridge vs. Lasso λ sweep + learning curves |
| `loss_functions.py` | `misclassification_error` metric |

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Libraries:** NumPy, Plotly, scikit-learn (Lasso + Diabetes dataset)
- **Key concepts:** AdaBoost, decision stumps, k-fold cross-validation, Ridge regression, L1/L2 regularisation, bias-variance tradeoff

---

## Run

```bash
pip install -r requirements.txt
python adaboost_scenario.py        # boosting experiments
python perform_model_selection.py  # Ridge/Lasso model selection
```
