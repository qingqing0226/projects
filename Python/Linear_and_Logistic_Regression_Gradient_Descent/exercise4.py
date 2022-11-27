import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap

"""
Functions start
"""


# sigmoid function
def sigmoid(x_beta):
    return 1/(1 + np.e ** (-x_beta))


# compute cost function by using model x_beta, response y_
# return the cost
def cost(x_beta, y_):
    return -1/(len(x_beta)) * (y_.T.dot(np.log(sigmoid(x_beta))) + (1 - y_).T.dot(np.log(1 - sigmoid(x_beta))))


# Use gradient descent to find beta (b)
# return beta and a list of costs
def gradient_descent(n, a, x_matrix, b, y_):
    cost_list = []
    for it in range(n):
        b -= a / len(x_matrix) * x_matrix.T.dot(sigmoid(x_matrix.dot(b)) - y)
        cost_list.append(cost(x_matrix.dot(b), y_))
    return b, cost_list


# takes 2 features x_1, x_2, and degree d
# create extended matrix
def mapFeature(x_1, x_2, d):
    one = np.ones((len(x_1), 1))
    Xe = np.c_[one, x_1, x_2]  # Start with [1,X1,X2]
    for i in range(d, d + 1):
        for j in range(0, i + 1):
            x_new = x_1 ** (i - j) * x_2 ** j  # type (N)
            x_new = x_new.reshape(-1, 1)  # type (N,1) required by append
            Xe = np.append(Xe, x_new, 1)  # axis = 1 ==> append column
    return Xe


# use sigmoid function to predict responses for x_beta and compare them with actual values y_
# return  training errors
def train_error(x_beta, y_):
    z = x_beta.reshape(-1, 1)  # Compute X*beta
    pp = np.round(sigmoid(z))  # prediction
    y_ = y_.reshape(-1, 1)  # actual
    return np.sum(y_ != pp)


# classify mesh and return a classified mesh grid
def mesh_grid(x_axis, y_axis, degree, beta, clz_shape):
    XXe = mapFeature(x_axis, y_axis, degree)  # Extend matrix for degree
    p = sigmoid(np.dot(XXe, beta))  # classify mesh ==> probabilities
    classes = p > 0.5  # round off probabilities
    return classes.reshape(clz_shape)  # return to mesh format


# plot iterations vs cost
def subplot_iter_vs_cost(iter, cos, a_):
    plt.subplot(1, 2, 1)
    plt.plot(np.array(list(range(iter))), np.array(cos), "-")
    plt.xlabel("iterations")
    plt.ylabel("cost function")
    plt.title("N = " + str(iter) + " Alpha = " + str(a_))


# plot decision boundary
# mesh: (x_m, y_m) = axis, c1 = color, clz_m = classes
# points(data): (x_p, y_p) = axis, c2 = color
def subplot_dcs_bdr(x_m, y_m, x_p, y_p, c1, c2, clz_m, e):
    plt.subplot(1, 2, 2)
    plt.pcolormesh(x_m, y_m, clz_m, cmap=c1)  # mesh
    plt.scatter(x_p, y_p, c=y, marker='.', cmap=c2)  # points
    plt.title("Training errors = " + str(e))
    plt.xlabel("X1")
    plt.ylabel("X2")


"""
Functions end
"""


"""
Part 1
Plot the data in X and y using different symbols or colors for the two different classes.
"""
print("There are three figures. It takes a while to plot. \nPlease wait......")
# read file
data = np.loadtxt("microchips.csv", delimiter=",")
plt.plot(data[:58, 0], data[:58, 1], "go", label="OK")
plt.plot(data[58:, 0], data[58:, 1], "rx", label="Failed")
plt.xlabel("X1")
plt.ylabel("X2")
plt.title("Microchips with flaws")
plt.legend()
plt.show()

"""
Part 2
Use gradient descent to find beta in the case of a quadratic model
Print the hyper parameters alpha and Niter, and produce a 1x2 plot with: 1) the cost function
J(beta) as a function over iterations, 2) the corresponding decision boundary (together with
the X, y scatter plot), and 3) the number of training errors presented as a part of the
decision boundary plot title.
"""
x1 = data[:, 0]
x2 = data[:, 1]
x_e = mapFeature(x1, x2, 2)  # extended x matrix
y = data[:, 2]
# gradient descent
iterations = 30000
alpha = 0.1
beta = np.r_[0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
beta, costs = gradient_descent(iterations, alpha, x_e, beta,  y)

# create mesh grid for decision boundary
h = .01  # step size in the mesh
x_min, x_max = np.min(x1) - 0.1, np.max(x1) + 0.1
y_min, y_max = np.min(x2) - 0.1, np.max(x2) + 0.1
xx, yy = np.meshgrid(np.arange(x_min, x_max, h), np.arange(y_min, y_max, h))  # Mesh Grid
x_axis, y_axis = xx.ravel(), yy.ravel()  # Turn to two Nx1 arrays


clz_mesh = mesh_grid(x_axis, y_axis, 2, beta, xx.shape)

cmap_light = ListedColormap(['#FFAAAA', '#AAFFAA', '#AAAAFF'])  # mesh plot
cmap_bold = ListedColormap(['#FF0000', '#00FF00', '#0000FF'])  # colors
errors = train_error(np.dot(x_e, beta), y)  # training errors

# subplot 1 cost function vs iterations
fig = plt.figure(2)
subplot_iter_vs_cost(iterations, costs, alpha)
# subplot 2 decision boundary
subplot_dcs_bdr(xx, yy, x1, x2, cmap_light, cmap_bold, clz_mesh, errors)
plt.tight_layout()
plt.show()
"""
Part 3
Implement a method called mapFeatures.
That is a function that takes two features X1, X2 and a degree d as input and outputs all
combinations of polynomial terms of degree less than or equal to d of the variables X1 and
X2.
"""
# implementation see line 31

"""
Part 4
Use mapFeatures to repeat 2) but with a polynomial of degree five (d = 5) model
"""
x_e = mapFeature(x1, x2, 5)  # extended matrix x with degree 5
# gradient descent
iterations = 70000
alpha = 0.5
beta = np.r_[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]
beta, costs = gradient_descent(iterations, alpha, x_e, beta, y)

# create mesh grid for decision boundary
clz_mesh = mesh_grid(x_axis, y_axis, 5, beta, xx.shape)
errors = train_error(np.dot(x_e, beta), y)  # training errors

# subplot 1  cost function vs iterations
fig = plt.figure(3)
subplot_iter_vs_cost(iterations, costs, alpha)
# subplot 2  decision boundary
subplot_dcs_bdr(xx, yy, x1, x2, cmap_light, cmap_bold, clz_mesh, errors)
plt.tight_layout()
plt.show()
