import numpy as np
from numpy . random import default_rng
import matplotlib.pyplot as plt
from sklearn import tree
from sklearn.ensemble import RandomForestClassifier
from statistics import mean

# get the label for given data point (x1, x2) by checking dataset
def get_label(point, dataset):
    for r in dataset:
        if r[0] == point[0] and r[1] == point[1]:
            return r[2]


# make a mesh grid and return its x-, y-axis values
def mesh_grid(xx):
    x_min, x_max = np.min(xx[:, 0]) - 0.1, np.max(xx[:, 0]) + 0.1
    y_min, y_max = np.min(xx[:, 1]) - 0.1, np.max(xx[:, 1]) + 0.1
    return np.meshgrid(np.arange(x_min, x_max, 0.01), np.arange(y_min, y_max, 0.01))  # Mesh Grid


# read file and divide data into training and test datasets of size 5000
data = np.loadtxt("bm.csv", delimiter=",")
train_set = data[:5000, :]
X, Y = train_set[:, :2], train_set[:, 2]
test_set = data[5000:, :]
x_test, y_test = test_set[:, :2], test_set[:, 2]

# create 100 bootstrapped training data matrices for each decision tree
rng = np.random.default_rng()
n = 5000
r = np.zeros([n, 100], dtype=int)
XX = np.zeros([n, 2, 100])

for i in range(100):
    r[:, i] = rng.choice(n, size=n, replace=True)
    XX[:, :, i] = X[r[:, i], :]

# create y for each training data matrix
yy = []
for x in range(100):
    y = []
    for row in XX[:, :, x]:
        y.append(get_label(row, train_set))
    yy.append(np.array(y))

# a) The estimate of the generalization error using the test set of the ensemble of 100 decision trees.
ensemble = RandomForestClassifier()
ensemble.fit(X, Y)
y_predict = ensemble.predict(x_test)
errors = np.sum(y_predict != y_test)
print("Part a")
print("generalization errors: ", errors, "\n")  # 108

# b) The average estimated generalization error of the individual decision trees.
# create 100 decision trees
trees = []
for i in range(100):
    t = tree.DecisionTreeClassifier().fit(XX[:, :, i], yy[i])
    trees.append(t)

# all errors of 100 trees
error_100 = []
for t in trees:
    single_predict = t.predict(x_test)  # prediction by using a tree
    error_100.append(np.sum(single_predict != y_test))
print("Part b")
print("average generalization errors: ", round(mean(error_100)))  # 181

# c) A plot of the decision boundaries of all the models , and including the ensemble model
xx, yy = mesh_grid(data)
plt.figure(1)  # decision boundary for 100 individual models
plt.suptitle("Decision boundary of 100 trees")
for t in trees:
    Z = t.predict(np.c_[xx.ravel(), yy.ravel()])
    Z = Z.reshape(xx.shape)
    ax = plt.subplot(10, 10, (trees.index(t) + 1))
    ax.set_xticks([])
    ax.set_yticks([])
    plt.contourf(xx, yy, Z, cmap=plt.cm.coolwarm, alpha=0.2)
plt.xlim(xx.min(), xx.max())
plt.ylim(yy.min(), yy.max())
plt.tight_layout()
plt.show()

plt.figure(2)  # decision boundary for ensemble model
Z = ensemble.predict(np.c_[xx.ravel(), yy.ravel()])
Z = Z.reshape(xx.shape)
plt.title("Decision boundary of Ensemble Model")
plt.contourf(xx, yy, Z, cmap=plt.cm.coolwarm, alpha=0.2)
plt.show()

# d)
"""
The results have been similar to the predictions, there was no surprises in the results themselves, 
However the biggest surprise was also the downside of the approach, the time consumed to conduct 
such an approach as abnormally long, this will not be feasible on most computers with larger datasets, 
it takes an abnormal amount of time.
"""


