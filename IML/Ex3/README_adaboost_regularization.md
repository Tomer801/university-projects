# Ensemble Learning – AdaBoost, Regularization, and Model Selection

This repository contains implementations of **AdaBoost with Decision Stumps**, **regularized regression (Ridge, Lasso)**, and utility functions for model evaluation, selection, and visualization.

---

## Files

- **base_estimator.py** – Abstract base class for supervised estimators, defining `fit`, `predict`, `loss`, and helper methods【399†source】.
- **decision_stump.py** – Implementation of a simple Decision Stump classifier (a one-level decision tree). Supports threshold selection and misclassification error【400†source】.
- **adaboost_scenario.py** – Experiments with AdaBoost using Decision Stumps on synthetic 2D datasets, with visualization of decision boundaries and weighted samples【398†source】.
- **loss_functions.py** – Implements `misclassification_error` for classification tasks【402†source】.
- **cross_validate.py** – Generic cross-validation utility to evaluate estimators with training and validation folds【397†source】.
- **estimators.py** – Regression models: `LinearRegression`, `Lasso` (wrapper over sklearn), and manual `RidgeRegression` implementation with closed-form solution【401†source】.
- **perform_model_selection.py** – Uses cross-validation on the Diabetes dataset to compare Ridge, Lasso, and Least Squares regression. Selects optimal λ values and plots validation error curves【403†source】.
- **utils.py** – Helper functions for visualization (decision surfaces) and data splitting【404†source】.

---

## Features

### AdaBoost
- Implements boosting with Decision Stumps as weak learners.
- Visualizes training/test error vs. number of learners.
- Plots decision boundaries at different boosting stages and highlights weighted samples in the final ensemble【398†source】.

### Regularized Regression
- Ridge Regression (manual closed-form solution).
- Lasso Regression (sklearn implementation).
- Linear Regression baseline.
- Cross-validation to select best λ (regularization parameter)【403†source】.

### Utilities
- Misclassification loss calculation【402†source】.
- Decision surface visualization with Plotly【404†source】.
- Cross-validation helper for any estimator following the `BaseEstimator` API【397†source】.

---

## Installation

Install dependencies:
```bash
pip install -r requirements.txt
```

---

## Usage

### Run AdaBoost experiment:
```bash
python adaboost_scenario.py
```

### Run model selection with Ridge/Lasso:
```bash
python perform_model_selection.py
```

Both scripts generate plots (decision surfaces or validation curves).

---

## Example Results

- **AdaBoost**: With noiseless data, test error drops rapidly as learners increase. With noise, error stabilizes and weighted samples highlight mislabeled points.  
- **Ridge/Lasso**: Cross-validation identifies λ values minimizing validation error. Best Ridge and Lasso models outperform vanilla Linear Regression on test set【403†source】.

---

## License
Educational use only.
