import numpy as np
import matplotlib.pyplot as plt

# read file
data = np.loadtxt("admission.csv", delimiter=",")

"""
Functions
"""


# sigmoid function
def sigmoid(x_beta):
    return 1/(1 + np.e ** (-x_beta))


# compute cost function by using model x_beta, response y_
# return the cost
def cost(x_beta, y_):
    return -1/(len(x_beta)) * (y_.T.dot(np.log(sigmoid(x_beta))) + (1 - y_).T.dot(np.log(1 - sigmoid(x_beta))))


"""
Part 1 
"""
mu1 = np.mean(data[:, 0])
sigma1 = np.std(data[:, 0])
mu2 = np.mean(data[:, 1])
sigma2 = np.std(data[:, 1])
s1 = []  # normalized score 1
s2 = []  # normalized score 2
for x in data[:, 0]:
    s1.append((x - mu1)/sigma1)
for x in data[:, 1]:
    s2.append((x - mu2)/sigma2)
s1 = np.array(s1)
s2 = np.array(s2)
matrix = np.c_[s1, s2, data[:, 2]]
matrix = matrix[matrix[:, 2].argsort()]
zeros = 0
for row in matrix:
    if row[2] == 0:
        zeros += 1
print("Part 1")
print("After normalization")
print("std1 = ", np.std(s1), " std2 = ", np.std(s2))
plt.plot(matrix[:zeros, 0], matrix[:zeros, 1], "ro", label="Not admitted")
plt.plot(matrix[zeros:, 0], matrix[zeros:, 1], "g+", label="Admitted")
plt.legend()
plt.xlabel("score 1")
plt.ylabel("score 2")
plt.show()

"""
Part 2 test sigmoid function on [[0,1],[2,3]]
"""
S = np.array([[0, 1], [2, 3]])

print("\nPart 2")
print("prob for [[0,1],[2,3]]:\n", sigmoid(S))


"""
Part 3
"""
x_e = np.c_[np.ones((len(data), 1)), s1, s2]
y = data[:, 2]

"""
Part 4 logistic cost function
"""
print("\nPart 4")
beta = np.r_[0.0, 0.0, 0.0]
print("cost for beta[0, 0, 0] is ", round(cost(np.dot(x_e, beta), y), 4))

"""
Part 5 gradient descent
"""
n = 1
alpha = 0.5
beta1 = beta - alpha/len(x_e) * (x_e.T).dot(sigmoid(x_e.dot(beta)) - y)
print("\nPart 5")
print("After 1 iteration\nbeta 1 = ", beta1, "\nreduced cost J = ", cost(x_e.dot(beta1), y))


"""
Part 6 gradient descent + linear decision boundary
"""
n = 998
reduced_cost = 0
for i in range(n):
    beta -= alpha/len(x_e) * x_e.T.dot(sigmoid(x_e.dot(beta)) - y)
    reduced_cost = cost(x_e.dot(beta), y)
print("\nPart 6")
print("N = 998")
print("Reduced cost = %.4f" % reduced_cost)
print("beta = ", np.round_(beta, decimals=3))
# Plot x2 = −(β0 + β1x1)/β2 for x1 ∈ [min(X1), max(X1)] to display the boundary
x1 = np.arange(np.min(s1), np.max(s1), 0.01)
x2 = -(beta[0] + beta[1]*x1)/beta[2]
plt.plot(x1, x2, "-")
plt.plot(matrix[:zeros, 0], matrix[:zeros, 1], "ro", label="Not admitted")
plt.plot(matrix[zeros:, 0], matrix[zeros:, 1], "g+", label="Admitted")
plt.legend()
plt.xlabel("Exam 1 score (normalized)")
plt.ylabel("Exam 2 score (normalized)")
plt.show()

"""
Part 7
The admission probability for a student with scores 45, 85 is 0.77, 
and the number of training errors is 11.
"""
Sn = np.array([(45-mu1)/sigma1, (85-mu2)/sigma2])
Sne = np.c_[1, Sn[0], Sn[1]]      # Extended Sn
prob = sigmoid(np.dot(Sne, beta))   # g(Sne*beta)
print("\nPart 7")
print("Prob for [45, 85] is %.2f" % prob)
# compute training errors
z = np.dot(x_e, beta).reshape(-1, 1)  # Compute X*beta
p = sigmoid(z)  # Probabilities in range [0,1]
pp = np.round(p)  # prediction
yy = y.reshape(-1, 1)  # actual
print("Training errors: ", (np.sum(yy != pp)))

