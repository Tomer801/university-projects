import numpy as np
import pandas as pd
from typing import Tuple, List, Callable, Type

from base_module import BaseModule
from base_learning_rate import  BaseLR
from cross_validate import cross_validate
from gradient_descent import GradientDescent
from learning_rate import FixedLR
from sklearn.metrics import roc_curve, auc
import loss_functions

# from IMLearn.desent_methods import GradientDescent, FixedLR, ExponentialLR
from modules import L1, L2
from logistic_regression import LogisticRegression
from utils import split_train_test

import plotly.graph_objects as go


def plot_descent_path(module: Type[BaseModule],
                      descent_path: np.ndarray,
                      title: str = "",
                      xrange=(-1.5, 1.5),
                      yrange=(-1.5, 1.5)) -> go.Figure:
    """
    Plot the descent path of the gradient descent algorithm

    Parameters:
    -----------
    module: Type[BaseModule]
        Module type for which descent path is plotted

    descent_path: np.ndarray of shape (n_iterations, 2)
        Set of locations if 2D parameter space being the regularization path

    title: str, default=""
        Setting details to add to plot title

    xrange: Tuple[float, float], default=(-1.5, 1.5)
        Plot's x-axis range

    yrange: Tuple[float, float], default=(-1.5, 1.5)
        Plot's x-axis range

    Return:
    -------
    fig: go.Figure
        Plotly figure showing module's value in a grid of [xrange]x[yrange] over which regularization path is shown

    Example:
    --------
    fig = plot_descent_path(IMLearn.desent_methods.modules.L1, np.ndarray([[1,1],[0,0]]))
    fig.show()
    """
    def predict_(w):
        return np.array([module(weights=wi).compute_output() for wi in w])

    from utils import decision_surface
    return go.Figure([decision_surface(predict_, xrange=xrange, yrange=yrange, density=70, showscale=False),
                      go.Scatter(x=descent_path[:, 0], y=descent_path[:, 1], mode="markers+lines", marker_color="black")],
                     layout=go.Layout(xaxis=dict(range=xrange),
                                      yaxis=dict(range=yrange),
                                      title=f"GD Descent Path {title}"))


def get_gd_state_recorder_callback() -> Tuple[Callable[[], None], List[np.ndarray], List[np.ndarray]]:
    """
    Callback generator for the GradientDescent class, recording the objective's value and parameters at each iteration

    Return:
    -------
    callback: Callable[[], None]
        Callback function to be passed to the GradientDescent class, recoding the objective's value and parameters
        at each iteration of the algorithm

    values: List[np.ndarray]
        Recorded objective values

    weights: List[np.ndarray]
        Recorded parameters
    """
    v = []
    w = []

    def callback(**kwargs):
        v.append(kwargs["val"])
        w.append(kwargs["weights"].copy())
    return callback, v, w





def compare_fixed_learning_rates(init: np.ndarray = np.array([np.sqrt(2), np.e / 3]),
                                 etas: Tuple[float] = (1, .1, .01, .001)):
    for module in [L1, L2]:
        fig = go.Figure()
        for eta in etas:
            init_weights = init.copy()
            l = module(weights=init_weights.copy())
            callback, values, weights = get_gd_state_recorder_callback()
            gd = GradientDescent( learning_rate = FixedLR(eta), callback=callback)
            gd.fit(l, X=None, y=None)
            print(eta ,values[-1])
            descent_path = np.array(weights)
            descent_fig= plot_descent_path(module, descent_path, title=f"Fixed LR {eta} for {module.__name__}",
                                    xrange=(-1.5, 1.5), yrange=(-1.5, 1.5))
            descent_fig.show()

            norms = [np.linalg.norm(w) for w in weights]
            fig.add_trace(go.Scatter(y=norms, mode="lines", name=f"eta={eta}"))
        fig.update_layout(title=f"Convergence Rate for {module.__name__}",
                                      xaxis_title="Iteration",
                                      yaxis_title="Weight Norm")
        fig.show()



def load_data(path: str = "SAheart.data", train_portion: float = .8) -> \
        Tuple[pd.DataFrame, pd.Series, pd.DataFrame, pd.Series]:
    """
    Load South-Africa Heart Disease dataset and randomly split into a train- and test portion

    Parameters:
    -----------
    path: str, default= "../datasets/SAheart.data"
        Path to dataset

    train_portion: float, default=0.8
        Portion of dataset to use as a training set

    Return:
    -------
    train_X : DataFrame of shape (ceil(train_proportion * n_samples), n_features)
        Design matrix of train set

    train_y : Series of shape (ceil(train_proportion * n_samples), )
        Responses of training samples

    test_X : DataFrame of shape (floor((1-train_proportion) * n_samples), n_features)
        Design matrix of test set

    test_y : Series of shape (floor((1-train_proportion) * n_samples), )
        Responses of test samples
    """
    df = pd.read_csv(path)
    df.famhist = (df.famhist == 'Present').astype(int)
    return split_train_test(df.drop(['chd', 'row.names'], axis=1), df.chd, train_portion)


def fit_logistic_regression():
    # Load and split SA Heard Disease dataset
    X_train, y_train, X_test, y_test = load_data()
    callback, losses, weights = get_gd_state_recorder_callback()

    gd = GradientDescent(callback=callback)

    # Plotting convergence rate of logistic regression over SA heart disease data
    model = LogisticRegression(solver=gd)
    model.fit(X_train.to_numpy(), y_train.to_numpy())
    y_proba = model.predict_proba(X_test.to_numpy())
    fpr, tpr, thresholds = roc_curve(y_test, y_proba)
    best_threshold_idx = np.argmax(tpr - fpr)
    model.alpha_ = thresholds[best_threshold_idx]
    test_error = model.loss(X_test.to_numpy(), y_test.to_numpy())


    print(f"Best threshold: {model.alpha_:.3f}, Test error: {test_error:.3f}")

    go.Figure(
        data=[go.Scatter(x=[0, 1], y=[0, 1], mode="lines", line=dict(color="black", dash='dash'),
                         name="Random Class Assignment"),
              go.Scatter(
                  x=fpr, y=tpr, mode='markers+lines',
                  text=thresholds, name="", showlegend=False,
                  marker=dict(size=5, color="blue"),
                  hovertemplate="<b>Threshold:</b>%{text:.3f}<br>FPR: %{x:.3f}<br>TPR: %{y:.3f}"
              )],
        layout=go.Layout(title=rf"$\text{{ROC Curve Of Fitted Model - AUC}}={auc(fpr, tpr):.6f}$",
                         xaxis=dict(title=r"$\text{False Positive Rate (FPR)}$"),
                         yaxis=dict(title=r"$\text{True Positive Rate (TPR)}$"))).show()
    candidate_lambdas = [0.001, 0.002, 0.005, 0.01, 0.02, 0.05, 0.1]
    best_lambda = None
    best_val_score = float('inf')

    for lam in candidate_lambdas:
        model = LogisticRegression(
            penalty="l1",
            lam=lam,
            alpha=0.5,
            include_intercept=True,
            solver=GradientDescent(max_iter=20000, learning_rate=FixedLR(1e-4))
        )

        # Average validation error
        _, val_score = cross_validate(model, X_train.to_numpy(), y_train.to_numpy(),
                                                scoring=loss_functions.misclassification_error,
                                                cv=5)
        if val_score < best_val_score:
            best_val_score = val_score
            best_lambda = lam


    final_model = LogisticRegression(
        penalty="l1",
        lam=best_lambda,
        alpha=0.5,
        include_intercept=True,
        solver=GradientDescent(max_iter=20000, learning_rate=FixedLR(1e-4))
    )
    final_model.fit(X_train.to_numpy(), y_train.to_numpy())

    test_error = final_model.loss(X_test.to_numpy(), y_test.to_numpy())
    print(f"Best λ: {best_lambda}, Test Error: {test_error:.3f}")


if __name__ == '__main__':
    np.random.seed(0)
   # compare_fixed_learning_rates()

    fit_logistic_regression()




