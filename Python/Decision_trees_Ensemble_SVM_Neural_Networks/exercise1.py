import numpy as np
import matplotlib.pyplot as plt
from sklearn import svm
from sklearn.model_selection import GridSearchCV, cross_val_predict

"""
Functions START
"""


# create mesh grid points with step h=0.01
def get_meshgrid(x, y, h=0.01):
    x_min, x_max = np.min(x) - 0.1, np.max(x) + 0.1
    y_min, y_max = np.min(y) - 0.1, np.max(y) + 0.1
    xx, yy = np.meshgrid(np.arange(x_min, x_max, h), np.arange(y_min, y_max, h))
    return xx, yy


# Plot the decision boundaries for a classifier
def plot_decision_boundary(clf, xx, yy, **params):
    Z = clf.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)
    plt.contourf(xx, yy, Z, **params)


"""
Functions END
"""

"""
Part 1
Tune the necessary hyperparameters by for instance grid search.
The original dataset is divided into training set(80%) and validation set(20%) 
best parameters: {'C': 1000, 'gamma': 0.01, 'kernel': 'rbf'}
"""
# read file
data = np.loadtxt("mnistsub.csv", delimiter=",")
# divide data set into two parts
vali_no = round(len(data) * 0.2)  # validation set: 20%  training set: 80%
X = data[vali_no:, :2]  # training set
y = data[vali_no:, 2]
X_val = data[:vali_no, :2]  # validation set
y_val = data[:vali_no, 2]

parameters = [{'kernel': ['linear'], 'C': [1, 10, 100, 1000]},
              {'kernel': ['rbf'], 'C': [1, 10, 100, 1000], 'gamma': [0.01, 0.001, 0.0001]},
              {'kernel': ['poly'], 'C': [1, 10, 100, 1000], 'degree': [3, 5, 7]}]
metrics = ["recall_macro", "accuracy"]

for metric in metrics:
    clf = GridSearchCV(svm.SVC(), parameters, scoring=metric)
    clf.fit(X, y)
    print("<<", metric, ">>")
    print("best parameters: ", clf.best_params_)
    mean = clf.cv_results_["mean_test_score"]
    print("mean_test_score: ", round(mean.max(), 3), "\n")

# compute accuracy on validation set by using the classifier based on best parameters
clf = svm.SVC(kernel="rbf", gamma=0.01, C=1000)
clf.fit(X, y)
y_pred = cross_val_predict(clf, X_val, y_val)
accuracy = round(1 - np.sum(y_pred != y_val) / len(y_val), 3)  # compare y_val with y_pred and compute accuracy
print("The accuracy for best model on validation set is: ", accuracy)

"""
Part 2
Produce a plot of the decision boundary for the best models together with the data
"""
xx, yy = get_meshgrid(data[:, 0], data[:, 1])  # create a mesh grid based on data features
one, three, five, nine = [], [], [], []  # divide original data into classes
for row in data:
    if row[2] == 1:
        one.append(row)
    elif row[2] == 3:
        three.append(row)
    elif row[2] == 5:
        five.append(row)
    elif row[2] == 9:
        nine.append(row)
one, three, five, nine = np.array(one), np.array(three), np.array(five), np.array(nine)

plot_decision_boundary(clf, xx, yy, cmap=plt.cm.coolwarm, alpha=0.8)
plt.scatter(one[:, 0], one[:, 1], c='dodgerblue', cmap=plt.cm.coolwarm, s=15, edgecolors="k", label="class 1")
plt.scatter(three[:, 0], three[:, 1], c='cyan', cmap=plt.cm.coolwarm, s=15, edgecolors="k", label="class 3")
plt.scatter(five[:, 0], five[:, 1], c='white', cmap=plt.cm.coolwarm, s=15, edgecolors="k", label="class 5")
plt.scatter(nine[:, 0], nine[:, 1], c='r', cmap=plt.cm.coolwarm, s=15, edgecolors="k", label="class 9")
plt.xlim(xx.min(), xx.max())
plt.ylim(yy.min(), yy.max())
plt.xticks(())
plt.yticks(())
plt.legend()
plt.title("kernel=rbf, gamma=0.01, C=1000")
plt.show()
