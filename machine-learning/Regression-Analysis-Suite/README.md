# Regression Analysis Suite

OLS linear regression and polynomial regression implementations applied to real-world house price and climate datasets, with end-to-end preprocessing, feature engineering, and model evaluation pipelines.

---

## Problem Solved

Build regression models from scratch using the closed-form OLS solution and evaluate them on two heterogeneous datasets — predicting house sale prices and modelling temperature time-series across multiple countries.

---

## Technical Highlights

| Challenge | How It Was Addressed |
|---|---|
| Closed-form OLS | `LinearRegression.fit()` computes `β = (XᵀX)⁻¹Xᵀy` via `numpy.linalg.lstsq` for numerical stability |
| Polynomial features | `PolynomialFitting` builds a Vandermonde matrix of degree k and delegates to the linear solver |
| Feature engineering | Derived `avg_bedroom_size` and `avg_bathroom_size` from raw counts; dropped low-correlation features identified by Pearson analysis |
| Degree selection | Cross-validated polynomial degree 1–10 on Israel temperature data; degree 5 minimises test MSE |
| Cross-country generalisation | Model trained on Israel evaluated on Jordan, South Africa, and others — visualised via bar chart with error bars |

---

## Modules

| File | Responsibility |
|---|---|
| `linear_regression.py` | OLS estimator with optional intercept; `fit()`, `predict()`, `loss()` (MSE) |
| `polynomial_fitting.py` | Vandermonde feature transform + linear OLS for degree-k polynomial fitting |
| `house_price_prediction.py` | Full pipeline: load → preprocess → feature selection → train/test split → learning curves |
| `city_temperature_prediction.py` | Exploratory analysis → polynomial degree sweep → cross-country evaluation |

---

## Tech Stack & Concepts

- **Language:** Python 3
- **Libraries:** NumPy, Matplotlib, Pandas
- **Key concepts:** OLS, Vandermonde matrices, MSE loss, Pearson correlation, feature engineering, learning curves, model selection

---

## Run

```bash
pip install -r requirements.txt
python house_price_prediction.py
python city_temperature_prediction.py
```
