# Machine Learning – Regression Projects

This repository contains implementations and experiments with **linear regression** and **polynomial regression**, applied to real-world datasets of house prices and city temperatures.

---

## Files

- **linear_regression.py** – Implementation of a Linear Regression model using ordinary least squares (OLS). Supports optional intercept and evaluation with MSE loss【372†source】.
- **polynomial_fitting.py** – Extension of linear regression to polynomial features. Transforms input into a Vandermonde matrix up to degree k and fits with least squares【373†source】.
- **house_price_prediction.py** – End-to-end pipeline for housing price prediction: preprocessing, feature engineering, feature evaluation, model training, and evaluation【370†source】.
- **city_temperature_prediction.py** – Data exploration and polynomial fitting of average daily temperatures for Israel and other countries. Includes training/test split, degree selection, and cross-country evaluation【369†source】.
- **imltheo_finalAns_merged.pdf** – Final written answers (theory part) of the course, containing explanations for preprocessing choices, model evaluation, and interpretation of results【371†source】.
- **requirements.txt** – Python dependencies for running the code【374†source】.

---

## Features

### House Price Prediction
- **Preprocessing**: Drop irrelevant/unreliable features (e.g., `id`, `date`), remove invalid rows, handle missing values, and engineer new features (e.g., `avg_bedroom_size`, `avg_bathroom_size`)【370†source】.
- **Feature Evaluation**: Computes Pearson correlations between features and house prices, with scatter plots saved for visualization.
- **Training & Evaluation**: Trains models with different training set sizes, measures test loss, and plots mean ± 2*std of MSE vs. dataset size.

### City Temperature Prediction
- **Exploratory Analysis**: Scatter plots of yearly temperature trends for Israel, monthly temperature std analysis, and cross-country comparisons with error bars.
- **Polynomial Fitting**: Tests polynomial models with degrees 1–10 for Israel, evaluates generalization error, and selects the optimal degree for cross-country prediction【369†source】.
- **Cross-Country Generalization**: Trains on Israel data and evaluates prediction loss on other countries.

---

## Installation

Install dependencies:
```bash
pip install -r requirements.txt
```

---

## Usage

### House Prices
```bash
python house_price_prediction.py
```

### City Temperatures
```bash
python city_temperature_prediction.py
```

Both scripts will generate plots and print evaluation metrics.

---

## Example Results

- **House Prices**: Strong correlation found between living area (`sqft_living`) and price, weak correlation with date. Larger training sets reduce variance of the model【371†source】.
- **City Temperatures**: Optimal polynomial degree found at k=5 (lowest test MSE). Israel’s temperature model generalizes well to Jordan, less so to countries like South Africa【371†source】.

---

## License
Educational use only.
