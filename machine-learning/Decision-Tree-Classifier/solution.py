import os
import numpy as np
import matplotlib.pyplot as plt
import seaborn as sns
from sklearn.svm import SVC
from sklearn.tree import DecisionTreeClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.datasets import make_moons, make_circles
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
np.random.seed(0)

### HELPER FUNCTIONS ###
# Add here any helper functions which you think will be useful
  
def generate_data(m:int): 
    """
        Helper for practical_1_runer generate data from a  normal distribution.

        Args:
            m (int): Number of samples to generate.

        Returns:
            tuple: A tuple (x, y) where:
                - x (np.ndarray): Generated data points of shape (m, 2).
                - y (np.ndarray): Labels for the data points of shape (m,).
        """
    mean = [0, 0]
    cov = [[1, 0.5],
           [0.5, 1]]  # 2x2 covariance matrix
    x = np.random.multivariate_normal(mean, cov, m)
    w = np.array([-0.6, 0.4])
    y = np.sign(x @ w)  # shape (m,)
    return (x, y)

def generate_tow_gaus(n:int,cov:np.ndarray , mu:np.ndarray):
    """
    Helper to practical_2_runer generate two Gaussian distributions with specified covariance and means.
    Args:
        n (int): Total number of samples to generate.
        cov (np.ndarray): Covariance matrix of shape (2, 2).
        mu (np.ndarray): Mean vectors of shape (2, 2).

    Returns:
        tuple: A tuple (gaus, y) where:
            - gaus (np.ndarray): Combined Gaussian data points of shape (n, 2).
            - y (np.ndarray): Labels for the data points (0 or 1) of shape (n,).
        """
    gaus_1 = np.random.multivariate_normal(mu[0], cov, int(n/2))
    gaus_2 = np.random.multivariate_normal(mu[1], cov, int(n/2))
    gaus = np.vstack([gaus_1, gaus_2])
    y = np.array([0] * int(n / 2) + [1] * int(n / 2))
    return gaus,y

def plot_lines(model, X_train, y_train, X_test, y_test, title=None,save_path=None):
   #Set the plot   
   h = 0.02  
   x_min, x_max = X_train[:, 0].min() - 1, X_train[:, 0].max() + 1
   y_min, y_max = X_train[:, 1].min() - 1, X_train[:, 1].max() + 1
   # Create coordinates for the grid
   x_, y_ = np.meshgrid(np.arange(x_min, x_max, h),
                        np.arange(y_min, y_max, h))
   grid = np.c_[x_.ravel(), y_.ravel()]

   # predict the values for all points on grid
   Z = model.predict(grid)
   Z = Z.reshape(x_.shape)

   # draw background color
   plt.figure(figsize=(6, 4))
   plt.contourf(x_, y_, Z, cmap='coolwarm', alpha=0.3)


   # plot training point as dots  test points as x
   plt.scatter(X_train[:, 0], X_train[:, 1], c=y_train, cmap='coolwarm', edgecolor='k', label='Train')
   plt.scatter(X_test[:, 0], X_test[:, 1], c=y_test, cmap='coolwarm', marker='x', label='Test')
   # the accuracy of the model
   acc = accuracy_score(y_test, model.predict(X_test))
   # title and labels
   plt.title(title + f" (Accuracy: {acc})")
   plt.xlabel("x1")
   plt.ylabel("x2")
   plt.legend()
   plt.tight_layout()
  # save the figure if needed
   if save_path is  None:
      plt.show()
   else:
       file_name = f"{save_path}/practical2"
       os.makedirs(file_name, exist_ok=True)
       file_name = file_name + f"/{title}.png"
       plt.savefig(file_name)
### Exercise Solution ###

def pratical_1_runner(save_path=None):
   true_w = np.array([-0.6, 0.4])
   for m in [5, 10, 20, 100]:
       x, y = generate_data(m)
       for C in [0.1, 1, 5, 10, 100]:
           # fit SVM model with current m,c
           model = SVC(kernel='linear', C=C)
           model.fit(x, y)

           # Get model weights and intercept
           w = model.coef_[0]
           b = model.intercept_[0]

           # x range for plotting the line
           x_range = np.linspace(x[:, 0].min() - 1, x[:, 0].max() + 1, 150)
           # Model decision boundary using the caculated w and b to draw the line
           model_line = -(w[0] * x_range + b) / w[1]

           # True line using the true_w
           true_line = -(true_w[0] / true_w[1]) * x_range
           plt.figure(figsize=(6, 4))
           plt.scatter(x[:, 0], x[:, 1], c=y, cmap='coolwarm', edgecolor='k', label="Samples")

           # true line
           plt.plot(x_range, true_line, 'k--', label='True Boundary')

           # predicted line
           plt.plot(x_range, model_line, 'g-', label='SVM Boundary')
           # title and labels
           plt.title(f"SVM with m={m}, C={C}")
           plt.xlabel("x1")
           plt.ylabel("x2")
           plt.legend()
           plt.tight_layout()
           if save_path is None:
               plt.show()
           else:
               file_name = f"{save_path}/practical1"
               os.makedirs(file_name, exist_ok=True)
               file_name = file_name + f"/m_{m}_C_{C}.png"
               plt.savefig(file_name)



def practical_2_runner(save_path=None):
   # mens and cov for the two gausian distributions
   mu = np.array([[-1, -1], [1, 1]])
   cov = np.array([[0.5, 0.2], [0.2, 0.5]])
    # models to be used
   models = {"tree":DecisionTreeClassifier(max_depth=7),
              "KNN":KNeighborsClassifier(n_neighbors=5),
               "SVM":SVC(C=1/5, kernel='linear')}
    # data to be use
   data_base = {"moon":make_moons(n_samples=200, noise=0.2),
                "circle":make_circles(n_samples=200, noise=0.1),
                "gausian":generate_tow_gaus(200, cov, mu)}
    # loop over the data and the models fit and plot the results
   for data_name,data in data_base.items():
       for model_name, model in models.items():
           X_train, X_test, y_train, y_test = train_test_split(data[0],data[1], test_size=0.2)
           model.fit(X_train, y_train)
           title = f"{model_name} on {data_name}"
           plot_lines(model, X_train, y_train, X_test, y_test, title,save_path)



if __name__ == "__main__":
    path = None
    pratical_1_runner(save_path=path)
    practical_2_runner(save_path=path)