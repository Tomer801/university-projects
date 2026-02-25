# Machine Learning – Visualization & Requirements

This repository specifies dependencies for visualization and machine learning experiments.

---

## Requirements

The project uses the following Python libraries (see `requirements.txt`):

- **numpy 2.2.5** – Numerical computing and array operations【382†source】.
- **matplotlib 3.10.1** – Plotting library for data visualization【382†source】.
- **seaborn 0.13.2** – Statistical data visualization, built on top of matplotlib【382†source】.
- **scikit-learn 1.6.1** – Machine learning algorithms, model training, evaluation, preprocessing【382†source】.

---

## Installation

Install dependencies with:
```bash
pip install -r requirements.txt
```

---

## Usage

These libraries support machine learning workflows such as regression, classification, visualization, and exploratory data analysis.  

Example:
```python
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.linear_model import LinearRegression

# Data
X = np.array([[1], [2], [3], [4]])
y = np.array([2, 4, 6, 8])

# Model
model = LinearRegression().fit(X, y)
pred = model.predict(X)

# Visualization
sns.scatterplot(x=X.flatten(), y=y, label="Data")
plt.plot(X, pred, color="red", label="Prediction")
plt.legend()
plt.show()
```

---

## License
Educational use only.
