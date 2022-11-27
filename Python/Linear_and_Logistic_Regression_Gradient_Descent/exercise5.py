import numpy as np
import matplotlib.pyplot as plt
from matplotlib.colors import ListedColormap
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import cross_val_predict

"""
Functions start
"""


# takes 2 features x_1, x_2, and degree d
# creates an extended polynomial matrix
def mapFeature(x_1, x_2, d):  # d = 3
    Xe = np.c_[x_1, x_2]  # Start with [1,X1,X2]
    for i in range(2, d + 1):  # 2, 3
        for j in range(0, i + 1):  # 0, 2
            x_new = x_1 ** (i - j) * x_2 ** j  # type (N)  x_1**2, x_1 * x_2, x_2**2, x_1**3, x_1**2 * x_2, x_1 * x_2**2
            x_new = x_new.reshape(-1, 1)  # type (N,1) required by append
            Xe = np.append(Xe, x_new, 1)  # axis = 1 ==> append column
    return Xe


# it takes a regularization parameter 'reg_para' and uses logistic regression to create models
# draws subplots of decision boundary with degree 1-9
def decision_boundary(reg_para):
    log_reg = LogisticRegression(solver='liblinear', C=reg_para, tol=1e-6, max_iter=100000)
    for i in range(1, 10):
        log_reg.fit(mapFeature(x1, x2, i), y)  # fit the model with data at degree i
        y_pred = log_reg.predict(mapFeature(x1, x2, i))  # predict
        errors = np.sum(y_pred != y) # compare y with y_pred
        z = log_reg.predict(mapFeature(x_axis, y_axis, i))
        clz_mesh = z.reshape(xx.shape)  # return to mesh format
        plt.subplot(3, 3, i)
        plt.title("Degree = " + str(i) + " Errors = " + str(errors))
        plt.pcolormesh(xx, yy, clz_mesh, cmap=cmap_light, shading='auto')
        plt.scatter(x1, x2, c=y, marker='.', cmap=cmap_bold)
    plt.tight_layout()
    plt.show()


"""
Functions end
"""


"""
Part 1
Use Logistic regression and mapFeatures from the previous exercise to construct nine
different classifiers, one for each of the degrees d [1; 9], and produce a figure containing a
3 x 3 pattern of subplots showing the corresponding decision boundaries. 
"""
# read file
data = np.loadtxt("microchips.csv", delimiter=",")
x1 = data[:, 0]
x2 = data[:, 1]
y = data[:, 2]
# colors
cmap_light = ListedColormap(['#FFAAAA', '#AAFFAA', '#AAAAFF'])  # mesh plot
cmap_bold = ListedColormap(['#FF0000', '#00FF00', '#0000FF'])  # colors

# create the intervals on mesh
h = .01  # step size in the mesh
x_min, x_max = np.min(x1) - 0.1, np.max(x1) + 0.1
y_min, y_max = np.min(x2) - 0.1, np.max(x2) + 0.1
xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                     np.arange(y_min, y_max, h))  # Mesh Grid
x_axis, y_axis = xx.ravel(), yy.ravel()  # Turn to two Nx1 arrays

print("Part 1")
print("C = 10000**2")
plt.figure(1)
decision_boundary(10000**2)

"""
Part 2
Redo 1) use the regularization parameter C = 1. What is different than from the step in 1)?
"""
print("\nPart 2")
print("C = 1")
print("The difference is that this logistic regression uses stronger regularization. c=1 is the default value. ")
print("It controls the importance of the regularization term. Smaller C increases regularization. ")
plt.figure(2)
decision_boundary(1)

"""
Part 3
use cross-validation (in sklearn) to see which of the regularized and unregularized models performs best. 
"""
print("\nPart 3")
print("As shown in the graph, at degree 1-2, regularized model has the same number of errors as non-regularized model.")
print("At degree 3-9, regularized model produces fewer errors.")
print("So regularized model performs best")
mdl1 = LogisticRegression(penalty='none', solver='newton-cg', max_iter=10000)  # non-regularized model
mdl2 = LogisticRegression(solver='liblinear', tol=1e-6, C=1000,  max_iter=10000)  # regularized model
err1 = []  # non-regularized errors
err2 = []  # regularized errors

for d in range(1, 10):
    mdl1.fit(mapFeature(x1, x2, d), y)  # fit the model with data
    y_pred1 = cross_val_predict(mdl1, mapFeature(x1, x2, d), y)
    err1.append(np.sum(y_pred1 != y))  # compare y with y_pred1

    mdl2.fit(mapFeature(x1, x2, d), y)  # fit the model with data
    y_pred2 = cross_val_predict(mdl2, mapFeature(x1, x2, d), y)
    err2.append(np.sum(y_pred2 != y))  # compare y with y_pred2

plt.plot(np.array(list(range(1, 10))), np.array(err1), "r.", label="non-regularized")  # non-regularized
plt.plot(np.array(list(range(1, 10))), np.array(err2), "b.", label="regularized")  # regularized
plt.title("red = non-regularized blue = regularized k = 5")
plt.xlabel("Degree")
plt.ylabel("Errors")
plt.show()



