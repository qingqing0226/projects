import matplotlib.pyplot as plt
import numpy as np
import math

"""
Reading Data from "polynomial200.csv"
"""
rawData = np.loadtxt("polynomial200.csv", delimiter=",", usecols=(0, 1))

"""
<<<<<< Functions Section Start >>>>>>
"""


# predict y by using knn
def k_nn(knn, x, arr):
    distance = []
    for pair in arr:
        distance.append(math.sqrt((x - pair[0]) ** 2))  # Euclidean method
    newArray = np.append(arr, np.array(distance).reshape(arr.shape[0], 1), axis=1)
    newArray = newArray[newArray[:, 2].argsort()]
    return np.mean(newArray[:knn, 1])


# draw subplot showing regression result and dataset error
# x_predicted_y: a 100x2 array, col0=x in [1-25, step 0.24], col1=predicted y
# dataset: a 100x2 array from rawData
def draw_subplot(x_predicted_y, dataset):
    plt.plot(x_predicted_y[:, 0], x_predicted_y[:, 1], "c-")  # line
    plt.scatter(dataset[:, 0], dataset[:, 1], c="b", marker=".")  # points


# return x, y coordinates of regression line
def regression(k):
    x_col = []
    y_col = []
    for x in np.arange(1.0, 25, 0.24):
        x_col.append(x)
        y_col.append(k_nn(k, x, training_set))
    return np.array(list(zip(x_col, y_col)))


def mse(y, predict_y):
    return np.mean((y - predict_y) ** 2)


"""
<<<<<< Functions Section End >>>>>> 
"""


"""
<<<<<< Exercise 2 Start >>>>>>
"""

"""
PART 1
Divide the dataset into a training set of size 100, and test set of size 100.
"""
training_set = rawData[:100, :]
test_set = rawData[100:, :]

"""
PART 2
Plot the training and test set side-by-side in a 1 x 2 pattern.
"""

plt.figure(figsize=(15, 10))

p1 = plt.subplot(121)
p1.set_title("Training Set")
plt.scatter(training_set[:, 0], training_set[:, 1], c="g", marker="o")

p2 = plt.subplot(122)
p2.set_title("Test Set")
plt.scatter(test_set[:, 0], test_set[:, 1], c="r", marker="o")
plt.show()

"""
PART 3
Display a 2 x 3 plot showing the k-NN regression result and the MSE training error for
k = 1, 3, 5, 7, 9, 11.
"""

# a 2x3 plot showing knn regression result and MSE training errors
fig = plt.figure()
plt.suptitle("Polynomial200 Regression Training Set")
k_list = [1, 3, 5, 7, 9, 11]
for k in k_list:
    train_predict = []
    for row in training_set:
        train_predict.append(k_nn(k, row[0], training_set))
    mse_value = 0.0 if k == 1 else round(mse(training_set[:, 1], np.array(train_predict)), 2)
    sub_p = plt.subplot(2, 3, k_list.index(k) + 1)
    sub_p.set_title("polynomial_train, k=" + str(k) + " MSE=" + str(mse_value), size=10)
    draw_subplot(regression(k), training_set)

fig.tight_layout()
plt.show()

"""
PART 4 
Compute and present the MSE test error for k = 1, 3, 5, 7, 9, 11
"""
# a 2x3 plot showing knn regression result and MSE test errors
fig = plt.figure()
plt.suptitle("Polynomial200 Regression Test Set")

for k in k_list:
    test_predict = []
    for row in test_set:
        test_predict.append(k_nn(k, row[0], training_set))
    mse_value = 0.0 if k == 1 else round(mse(test_set[:, 1], np.array(test_predict)), 2)
    sub_p = plt.subplot(2, 3, k_list.index(k) + 1)
    sub_p.set_title("polynomial_test, k=" + str(k) + " MSE=" + str(mse_value), size=10)
    draw_subplot(regression(k), test_set)

fig.tight_layout()
plt.show()

"""
Part 5
The k value that gives the best regression
"""
# k = 5 gives the best regression.
# When k = 5, its Mean Square Error(MSE) in training set is the second smallest (22.25)
# and its MSE is the second smallest in test set as well (28.48).
# The smaller MSE is, the better quality regression is.
# Therefore, combining two results on both training and test sets, k=5 is the best choice.

"""
<<<<<< Exercise 2 End >>>>>>
"""