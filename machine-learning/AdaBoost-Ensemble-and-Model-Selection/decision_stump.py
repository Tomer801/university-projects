from __future__ import annotations
from typing import Tuple, NoReturn
from base_estimator import BaseEstimator
import numpy as np
from itertools import product
from loss_functions import misclassification_error


class DecisionStump(BaseEstimator):
    """
    A decision stump classifier for {-1,1} labels according to the CART algorithm

    Attributes
    ----------
    self.threshold_ : float
        The threshold by which the data is split

    self.j_ : int
        The index of the feature by which to split the data

    self.sign_: int
        The label to predict for samples where the value of the j'th feature is about the threshold
    """

    def __init__(self) -> DecisionStump:
        """
        Instantiate a Decision stump classifier
        """
        super().__init__()
        self.threshold_, self.j_, self.sign_ = None, None, None


    def _fit(self, X: np.ndarray, y: np.ndarray, sample_weight: np.ndarray = None) -> NoReturn:
        """
        Fit a decision stump to the given data. That is, finds the best feature and threshold by which to split

        Parameters
        ----------
        X : ndarray of shape (n_samples, n_features)
            Input data to fit an estimator for

        y : ndarray of shape (n_samples, )
            Responses of input data to fit to
        """

        n_samples, n_features = X.shape
        best_err = np.inf

        for j in range(n_features):
            for sign in [+1, -1]:
                thr, err = self._find_threshold(X[:, j], y, sign)
                if err < best_err:
                    best_err = err
                    self.threshold_ = thr
                    self.j_ = j
                    self.sign_ = sign



    def _predict(self, X: np.ndarray) -> np.ndarray:
        """
        Predict sign responses for given samples using fitted estimator

        Parameters
        ----------
        X : ndarray of shape (n_samples, n_features)
            Input data to predict responses for

        y : ndarray of shape (n_samples, )
            Responses of input data to fit to

        Returns
        -------
        responses : ndarray of shape (n_samples, )
            Predicted responses of given samples

        Notes
        -----
        Feature values strictly below threshold are predicted as `-sign` whereas values which equal
        to or above the threshold are predicted as `sign`
        """
        if not self.fitted_:
            raise ValueError("Estimator must be fitted before predicting")


        preds = np.where(X[:, self.j_] >= self.threshold_, self.sign_, -self.sign_)
        return preds


    def _find_threshold(self, values: np.ndarray, labels: np.ndarray, sign: int) -> Tuple[float, float]:
        """
        Given a feature vector and labels, find a threshold by which to perform a split
        The threshold is found according to the value minimizing the misclassification
        error along this feature

        Parameters
        ----------
        values: ndarray of shape (n_samples,)
            A feature vector to find a splitting threshold for

        labels: ndarray of shape (n_samples,)
            The labels to compare against

        sign: int
            Predicted label assigned to values equal to or above threshold

        Returns
        -------
        thr: float
            Threshold by which to perform split

        thr_err: float between 0 and 1
            Misclassificaiton error of returned threshold

        Notes
        -----
        For every tested threshold, values strictly below threshold are predicted as `-sign` whereas values
        which equal to or above the threshold are predicted as `sign`
        """
        # Sort values and labels based on the sorted order of values
        sorted_indices = np.argsort(values)
        sorted_values = values[sorted_indices]
        sorted_labels = labels[sorted_indices]

        # Compute initial loss for classifying all as `sign`
        initial_loss = np.sum(np.abs(sorted_labels)[np.sign(sorted_labels) == sign])

        # Compute cumulative loss for each threshold
        cumulative_loss = initial_loss - np.cumsum(sorted_labels * sign)
        all_losses = np.concatenate(([initial_loss], cumulative_loss))

        # Find the threshold with the minimum loss
        min_loss_index = np.argmin(all_losses)
        thresholds = np.concatenate(([-np.inf], sorted_values[1:], [np.inf]))
        return thresholds[min_loss_index], all_losses[min_loss_index]



    def _loss(self, X: np.ndarray, y: np.ndarray) -> float:
        """
        Evaluate performance under misclassification loss function

        Parameters
        ----------
        X : ndarray of shape (n_samples, n_features)
            Test samples

        y : ndarray of shape (n_samples, )
            True labels of test samples

        Returns
        -------
        loss : float
            Performance under missclassification loss function
        """
        prediction = self.predict(X)
        return misclassification_error(y_true=y, y_pred=prediction, normalize=True)