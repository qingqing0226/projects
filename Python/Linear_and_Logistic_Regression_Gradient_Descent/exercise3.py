import numpy as np
import matplotlib.pyplot as plt

"""
Functions start
"""


# normalize a feature column
def normalize(x):
    mu = np.mean(x)
    sigma = np.std(x)
    for ele in x:
        ele = (ele - mu)/sigma
    return x


# sigmoid function
def sigmoid(x_beta):
    return 1/(1 + np.e ** (-x_beta))


# compute cost function by using model x_beta, response y_
# return the cost
def cost(x_beta, y_):
    s = sigmoid(x_beta)
    return -1/(len(x_beta)) * (y_.T.dot(np.log(s)) + (1 - y_).T.dot(np.log(1 - s)))


"""
Functions end
"""

"""
Part 1
"""
# read file and shuffle data
data = np.loadtxt("breast_cancer.csv", delimiter=",")
np.random.shuffle(data)

"""
Part 2
Replace the responses 2 and 4 with 0 and 1 and divide the dataset into a training set and a test set.
"""
print("\nPart 2")
print("Training set has 80% data. Test set has 20% data. \nI chose this division because the training set has to be "
      "large enough to be useful for creating a reliable model.")
for row in data:
    if row[9] == 2:
        row[9] = 0
    if row[9] == 4:
        row[9] = 1
train_size = round(len(data) * 0.8)  # training set size: 80% data
training_set = data[:train_size, :]
test_set = data[train_size:, :]

"""
Part 3
Normalize the training data and train a linear logistic regression model using gradient
descent. Print the hyperparameters alpha and Niter and plot the cost function J(beta) as a
function over iterations
n = 30000
alpha = 0.01
"""
x_e = np.array(np.ones((len(training_set), 1)))   # extended x matrix
for feature in range(9):
    x_e = np.c_[x_e, normalize(training_set[:, feature])]
y = training_set[:, 9]
# gradient descent
n = 3000
alpha = 0.1
costs = []
beta = np.r_[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]

for i in range(n):
    beta -= alpha / len(training_set) * (x_e.T).dot(sigmoid(x_e.dot(beta)) - y)
    costs.append(cost(x_e.dot(beta), y))


plt.plot(np.array(list(range(n))), np.array(costs), "-")
plt.xlabel("iterations")
plt.ylabel("cost function")
plt.show()
print("\nPart 3")
print("N = ", n, "\talpha = ", alpha, "\tcost = ", round(costs[n-1], 6))

"""
Part 4
What is the training error (number of non-correct classifications in the training data) and
the training accuracy (percentage of correct classifications) for your model?
"""
# compute training errors and accuracy
z = np.dot(x_e, beta).reshape(-1, 1)  # Compute X*beta
p = sigmoid(z)  # Probabilities in range [0,1]
pp = np.round(p)  # prediction
yy = y.reshape(-1, 1)  # actual
errors = np.sum(yy != pp)
accuracy = (np.sum(yy) - errors)/np.sum(yy)*100
print("\nPart 4")
print("Training errors: ", errors)
print("training accuracy: ", round(accuracy, 2), "%")

"""
Part 5
What is the number of test error and the test accuracy for your model?
"""
# create matrix based on test set
x_e = np.array(np.ones((len(test_set), 1)))   # extended x matrix
for feature in range(9):
    x_e = np.c_[x_e, normalize(test_set[:, feature])]
y = test_set[:, 9]
# compute test errors and accuracy
z = np.dot(x_e, beta).reshape(-1, 1)  # Compute X*beta
p = sigmoid(z)  # Probabilities in range [0,1]
pp = np.round(p)  # prediction
yy = y.reshape(-1, 1)  # actual
errors = np.sum(yy != pp)
accuracy = (np.sum(yy) - errors)/np.sum(yy)*100
print("\nPart 5")
print("Test errors: ", errors)
print("Test accuracy: ", round(accuracy, 2), "%")

"""
Part 6
Repeated runs will (due to the shuffling) give different results. Are they qualitatively
the same? Do they depend on how many observations you put aside for testing? Is the
difference between training and testing expected?
"""
print("\nPart 6")
print("Yes, they are the same. The qualitative results seem to follow a logical pattern. Those results are subject to "
      "change of based on the size of the training set. However, if the training set reaches a certain size, "
      "the effect of the increase of its size becomes minimum. Overall, the difference between two sets seemed "
      "normal. ")

