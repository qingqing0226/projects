import numpy as np
import matplotlib.pyplot as plt
from sklearn import svm

"""
Part 1
Create a dataset which consists of a random sample of 5,000 data points in bm.csv
"""
# read file
data = np.loadtxt("bm.csv", delimiter=",")
X = data[:, :2]
Y = data[:, 2]
np. random.seed(7)
r = np.random.permutation(len(Y))
X, Y = X[r, :], Y[r]
X_s, y_s = X[:5000, :], Y[:5000]


"""
Part 2
Use sklearn to create and train a support vector machine using a Gaussian kernel and compute its
training error ( gamma = .5 and C = 20 should yield a training error of .0102
"""
# create a svm classifier
clf = svm.SVC(C=20, kernel="rbf", gamma=0.5)
# train the model using the training set
clf.fit(X_s, y_s)
# predict and compute training error rate
y_pre = clf.predict(X_s)
print(" training error rate: ", np.sum(y_pre != y_s)/len(y_s))


"""
Part 3
"""
s = clf.support_vectors_
ones = []
zeros = []
for x, y in zip(X_s, y_s):
    if y == 1:
        ones.append(x)
    elif y == 0:
        zeros.append(x)
ones = np.array(ones)
zeros = np.array(zeros)
# create mesh grid for decision boundary
h = .01  # step size in the mesh
x_min, x_max = np.min(X_s[:, 0]) - 0.1, np.max(X_s[:, 0]) + 0.1
y_min, y_max = np.min(X_s[:, 1]) - 0.1, np.max(X_s[:, 1]) + 0.1
xx, yy = np.meshgrid(np.arange(x_min, x_max, h), np.arange(y_min, y_max, h))  # Mesh Grid

Z = clf.predict(np.c_[xx.ravel(), yy.ravel()])
Z = Z.reshape(xx.shape)
plt.subplot(1, 2, 1)
plt.contourf(xx, yy, Z, cmap=plt.cm.coolwarm, alpha=0.2)
plt.scatter(s[:, 0], s[:, 1], c='b', marker='.', s=1, cmap=plt.cm.coolwarm)

plt.subplot(1, 2, 2)
plt.contourf(xx, yy, Z, cmap=plt.cm.coolwarm, alpha=0.2)
plt.scatter(ones[:, 0], ones[:, 1], c='black', marker='.', s=1)
plt.scatter(zeros[:, 0], zeros[:, 1], c='yellow', marker='.', s=1)

plt.show()

