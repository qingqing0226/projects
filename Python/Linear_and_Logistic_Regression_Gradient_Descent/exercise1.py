import numpy as np
import matplotlib.pyplot as plt

# read file
data = np.loadtxt("GPUbenchmark.csv", delimiter=",")

"""
Functions - start
"""


# draw subplot with no = subplot number, x_axis, y_axis
def draw_subplot(no, x_axis, y_axis):
    plt.subplot(2, 3, no)
    plt.xlabel("X" + str(no))
    plt.ylabel("Y")
    plt.plot(x_axis, y_axis, ".")


# normalize a given value 'cell' based on vector 'x_col'
# return the normalized value
def normalize(cell, x_col):
    mu = np.mean(x_col)
    theta = np.std(x_col)
    return (cell - mu) / theta


# compute cost function by using extend matrix xe, beta b, response y_
# return the cost
def cost(xe, b, y_):
    j = np.dot(xe, b) - y_
    c = (j.T.dot(j)) / 18
    return c[0]


"""
Functions - end
"""

"""
Part 1
normalizing X using Xn = (X - μ )/σ
"""
normal_list = []
for col in range(6):
    normalized = []
    for x in data[:, col]:
        normalized.append(normalize(x, data[:, col]))
    normal_list.append(np.array(normalized))

"""
Part 2
plot Xi vs y for each one of the features
"""
y = data[:, 6].reshape(18, 1)
fig = plt.figure()
plt.suptitle("Xi vs y for each one of the features")
draw_subplot(1, normal_list[0], y)
draw_subplot(2, normal_list[1], y)
draw_subplot(3, normal_list[2], y)
draw_subplot(4, normal_list[3], y)
draw_subplot(5, normal_list[4], y)
draw_subplot(6, normal_list[5], y)
fig.tight_layout()
plt.show()

"""
Part 3
Compute beta using the normal equation
predict benchmark result for a graphic card with the feature values (2432, 1607, 1683, 8, 8, 256)
"""
x_e = np.c_[np.ones((data.shape[0], 1)), normal_list[0], normal_list[1], normal_list[2], normal_list[3], normal_list[4],
            normal_list[5]]
beta = np.linalg.inv(x_e.T.dot(x_e)).dot(x_e.T).dot(y)
sample = np.c_[1, normalize(2432, data[:, 0]), normalize(1607, data[:, 1]), normalize(1683, data[:, 2]),
               normalize(8, data[:, 3]), normalize(8, data[:, 4]), normalize(256, data[:, 5])]
print("Part 3")
print("Predicted benchmark result: ", sample.dot(beta)[0])  # 110.80403514

"""
Part 4
the cost J(beta) when using the beta computed by the normal equation above
"""
c = cost(x_e, beta, y)
print("Part 4")
print("cost: ", c)  # 12.39644436

"""
Part 5
Gradient descent
"""
n = 360
alpha = 0.026
beta = np.array([0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]).reshape(7, 1)
costs = []

for i in range(n):
    costs.append(cost(x_e, beta, y))
    # x_e = 18*7, x_e.T = 7*18, beta = 7*1     np.dot(x_e, beta) = 18*1
    beta -= alpha * np.dot(x_e.T, np.dot(x_e, beta) - y)
print("Difference between original cost and my cost: ", (abs(costs[n-1] - c)/c)*100, "%")
plt.plot(np.array(list(range(n))), np.array(costs), "-")
plt.xlabel("iterations")
plt.ylabel("cost function")
plt.show()

print("Part 5")
print("(a): ")
print("n = ", n,  "alpha = ", alpha)
print("(b): ")
print("Predicted benchmark result: ", sample.dot(beta)[0])  # 111.71170956
