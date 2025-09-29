import numpy as np
import plotly.graph_objects as go
from matplotlib import pyplot as plt
from sklearn import datasets
from sklearn.datasets import load_diabetes
from sklearn.model_selection import train_test_split

from cross_validate import cross_validate
from estimators import RidgeRegression, Lasso, LinearRegression


def select_regularization_parameter(n_samples: int = 50, n_evaluations: int = 500):
    # Question 1 - Load diabetes dataset and split into training and testing portions
    diabetes = datasets.load_diabetes()
    X, y = diabetes.data[:n_samples], diabetes.target[:n_samples]
    X_train, X_test, y_train, y_test = train_test_split(X, y, train_size=2 / 3, random_state=0)

    # Question 2 - Perform Cross Validation for different values of the regularization parameter for Ridge and
    # Lasso regressions
    lam_values = np.linspace(10**(-5), 5*10**(-5), n_evaluations)
    ridge_train_scores = []
    ridge_val_scores = []
    lasso_train_scores = []
    lasso_val_scores = []

    for lam in lam_values:
        ridge = RidgeRegression(lam)
        train_score, val_score = cross_validate(ridge, X_train, y_train)
        ridge_train_scores.append(train_score)
        ridge_val_scores.append(val_score)

        lasso = Lasso(alpha=lam)
        train_score, val_score = cross_validate(lasso, X_train, y_train)
        lasso_train_scores.append(train_score)
        lasso_val_scores.append(val_score)

    # Question 3 - Compare best Ridge model, best Lasso model and Least Squares model
    best_ridge_lam = lam_values[np.argmin(ridge_val_scores)]
    best_lasso_lam = lam_values[np.argmin(lasso_val_scores)]

    ridge_best = RidgeRegression(best_ridge_lam)
    ridge_best.fit(X_train, y_train)
    ridge_loss = ridge_best.loss(X_test, y_test)

    lasso_best = Lasso(alpha=best_lasso_lam)
    lasso_best.fit(X_train, y_train)
    lasso_loss = lasso_best.loss(X_test, y_test)

    linear_model = LinearRegression()
    linear_model.fit(X_train, y_train)
    linear_loss = linear_model.loss(X_test, y_test)

    print(f"Best Ridge lambda: {best_ridge_lam:.5f}, Test error: {ridge_loss:.2f}")
    print(f"Best Lasso lambda: {best_lasso_lam:.5f}, Test error: {lasso_loss:.2f}")
    print(f"Linear Regression Test error: {linear_loss:.2f}")

    # Plot results
    plt.figure(figsize=(10, 6))
    plt.plot(lam_values, ridge_val_scores, label='Ridge Validation Error')
    plt.plot(lam_values, lasso_val_scores, label='Lasso Validation Error')
    plt.xlabel('Lambda')
    plt.ylabel('Validation Error')
    plt.title('Validation Error vs Lambda')
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.show()  # save instead of show for static output
    plt.close()


if __name__ == '__main__':
    np.random.seed(0)
    select_regularization_parameter()
