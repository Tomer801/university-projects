import numpy as np
from typing import Tuple

from matplotlib import pyplot as plt

from utils import *
import plotly.graph_objects as go
from plotly.subplots import make_subplots

from adaboost import AdaBoost
from decision_stump import DecisionStump

def generate_data(n: int, noise_ratio: float) -> Tuple[np.ndarray, np.ndarray]:
    """
    Generate a dataset in R^2 of specified size

    Parameters
    ----------
    n: int
        Number of samples to generate

    noise_ratio: float
        Ratio of labels to invert

    Returns
    -------
    X: np.ndarray of shape (n_samples,2)
        Design matrix of samples

    y: np.ndarray of shape (n_samples,)
        Labels of samples
    """
    '''
    generate samples X with shape: (num_samples, 2) and labels y with shape (num_samples).
    num_samples: the number of samples to generate
    noise_ratio: invert the label for this ratio of the samples
    '''
    X, y = np.random.rand(n, 2) * 2 - 1, np.ones(n)
    y[np.sum(X ** 2, axis=1) < 0.5 ** 2] = -1
    y[np.random.choice(n, int(noise_ratio * n))] *= -1
    return X, y


def fit_and_evaluate_adaboost(noise, n_learners=250, train_size=5000, test_size=500):
    (train_X, train_y), (test_X, test_y) = generate_data(train_size, noise), generate_data(test_size, noise)

    # Question 1: Train- and test errors of AdaBoost in noiseless case

    adaboost = AdaBoost(DecisionStump, n_learners)
    adaboost.fit(train_X, train_y)
    train_error = []
    test_error = []
    for i in range(1, n_learners+1):
        train_error.append(adaboost.partial_loss(train_X, train_y, i))
        test_error.append(adaboost.partial_loss(test_X, test_y, i))

    plt.figure(figsize=(10, 5))
    plt.plot(range(1, n_learners+ 1), train_error, label="Train Error")
    plt.plot(range(1, n_learners + 1), test_error, label="Test Error")
    plt.title("Train/Test Error vs Number of Learners")
    plt.xlabel("Number of Learners")
    plt.ylabel("Misclassification Error")
    plt.legend()
    plt.grid(True)
    plt.tight_layout()
    plt.show()
    # Question 2: Plotting decision surfaces
    T = [5, 50, 100, 250]
    lims = np.array([np.r_[train_X, test_X].min(axis=0), np.r_[train_X, test_X].max(axis=0)]).T + np.array([-.1, .1])

    fig = make_subplots(rows=1, cols=4, subplot_titles=[rf"$\text{{{t} Classifiers}}$" for t in T])
    for i, t in enumerate(T):
        fig.add_traces(
            [decision_surface(lambda X: adaboost.partial_predict(X, t), lims[0], lims[1], density=60, showscale=False),
             go.Scatter(x=test_X[:, 0], y=test_X[:, 1], mode="markers", showlegend=False,
                        marker=dict(color=test_y, symbol=np.where(test_y == 1, "circle", "x")))],
            rows=1, cols=i + 1)
    fig.update_layout(height=500, width=2000).update_xaxes(visible=False).update_yaxes(visible=False)
    fig.show()

    # for T_val in T:
    #
    #     def predict(X):
    #         return adaboost.partial_predict(X, T_val)
    #
    #     # יצירת המשטח
    #     surface = decision_surface(predict, xrange=lims[0], yrange=lims[1], density=200)
    #
    #     # ציור גרף חדש
    #
    #     plt.figure(figsize=(6, 5))
    #     plt.imshow(surface.z,  extent=(lims[0, 0], lims[0, 1], lims[1, 0], lims[1, 1]),
    #
    #                origin='lower', cmap='RdBu', alpha=0.5, aspect='auto')
    #
    #     # נקודות טסט צבועות לפי התוויות
    #     plt.scatter(test_X[:, 0], test_X[:, 1], c=test_y, cmap='bwr', edgecolor='k', s=15)
    #     plt.title(f"Decision Boundary with T = {T_val}")
    #     plt.xlim(lims[0,0])
    #     plt.ylim(lims[1, 0])
    #     plt.grid(False)
    #     plt.tight_layout()
    #     plt.show()


    # Question 3: Decision surface of best performing ensemble
    lowest_error = np.inf
    best_T = None
    for T_val in range(1,n_learners+1):
        if test_error[T_val-1] < lowest_error:
            lowest_error = test_error[T_val - 1]
            best_T = T_val
    acc = 1 - lowest_error
    def predict_best(X):
        return adaboost.partial_predict(X, best_T)
    surface = decision_surface(predict_best,
                               xrange=lims[0], yrange=lims[1], density=200)
    plt.figure(figsize=(6, 5))
    plt.imshow(
        surface.z,
        extent=(lims[0, 0], lims[0, 1], lims[1, 0], lims[1, 1]),
        origin='lower',
        cmap='RdBu',
        alpha=0.5,
        aspect='auto'
    )
    plt.scatter(test_X[:, 0], test_X[:, 1], c=test_y, cmap='bwr', edgecolor='k', s=15)
    plt.title(f"Best Ensemble (T={best_T}) | Accuracy = {acc:.3f}")
    plt.xlim(lims[0])
    plt.ylim(lims[1])
    plt.grid(False)
    plt.tight_layout()
    plt.show()

    # Question 4: Decision surface with weighted samples
    D_final = adaboost.D_[-1]
    D = D_final / np.max(D_final) * 5

    def predict_n(X):
        return adaboost.predict(X)
    surface = decision_surface(predict_n, xrange=lims[0], yrange=lims[1], density=200)
    plt.figure(figsize=(6, 5))
    plt.imshow(
        surface.z,
        extent=(lims[0, 0], lims[0, 1], lims[1, 0], lims[1, 1]),
        origin='lower',
        cmap='RdBu',
        alpha=0.5,
        aspect='auto'
    )

    # ציור train עם משקלים
    plt.scatter(train_X[:, 0], train_X[:, 1], c=train_y, cmap='bwr', edgecolor='k', s=D * 20)
    plt.title("Final Decision Boundary with Weighted Training Samples")
    plt.xlim(lims[0, 0], lims[0, 1])
    plt.ylim(lims[1, 0], lims[1, 1])
    plt.grid(False)
    plt.tight_layout()
    plt.show()



if __name__ == '__main__':
    np.random.seed(0)
    fit_and_evaluate_adaboost(noise=0)
    fit_and_evaluate_adaboost(noise=0.4)

