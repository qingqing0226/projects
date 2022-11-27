import matplotlib.pyplot as plt
import numpy as np
from sklearn.neighbors import KNeighborsClassifier

"""
Reading Data from "microchips.csv"
"""
rawData = np.loadtxt("microchips.csv", delimiter=",", usecols=(0, 1, 2))
ok = rawData[:59, :]
fail = rawData[59:, :]

"""
<<<<<< Functions Section Start >>>>>>
"""


# predict class for each microchip by using KNeighborsClassifier
def predict_class(k, clf, microchips):
    print("k = ", k)
    for i in range(len(microchips)):
        print("Chip", (i + 1), ": ", microchips[i], " ==> ",
              "OK" if clf.predict([microchips[i]])[0] == 1.0 else "Fail")


# make a mesh with coordinates [i, j, predict y],
# i, j in interval [-1.0, 1.2) step 0.02
# predict y: based on prediction by using k value
def mesh(clf):
    mesh_list = []
    i, j = -1.0, -1.0
    while i < 1.2:
        while j < 1.2:
            mesh_list.append([i, j, clf.predict([[i, j]])[0]])
            j += 0.02
        i += 0.02
        j = -1.0
    ok_mesh = np.array([row for row in mesh_list if row[2] == 1.0])
    fail_mesh = np.array([row for row in mesh_list if row[2] == 0.0])
    return ok_mesh, fail_mesh


# draw subplot of decision boundary by using KNeighborsClassifier
def draw_subplot(clf):
    ok_mesh, fail_mesh = mesh(clf)
    plt.scatter(ok_mesh[:, 0], ok_mesh[:, 1], c="c", marker="s")
    plt.scatter(fail_mesh[:, 0], fail_mesh[:, 1], c="m", marker="s")
    plt.scatter(np.array(ok)[:, 0], np.array(ok)[:, 1], c="g", marker=".")
    plt.scatter(np.array(fail)[:, 0], np.array(fail)[:, 1], c="r", marker=".")


"""
<<<<<< Functions Section End >>>>>> 
"""

"""
<<<<<< Exercise 4 Start >>>>>>
"""

"""
PART 1
Plot the original microchip data using different markers for the two classes OK and Fail.
"""
plt.scatter(ok[:, 0], ok[:, 1], c="g", marker="o", label="Ok")
plt.scatter(fail[:, 0], fail[:, 1], c="r", marker="s", label="Fail")
plt.legend()
plt.xlabel("property 1")
plt.ylabel("property 2")
plt.title("Microchips in ok and fail classes")
plt.show()


"""
PART 2
use KNeighborsClassifier to predict whether three unknown microchips are likely to be OK or Fail.
"""

# create features and label
features = list(zip(rawData[:, 0], rawData[:, 1]))
label = rawData[:, 2]

# create models with k = 1, 3, 5, 7 and train them
clf_1 = KNeighborsClassifier(n_neighbors=1)
clf_1.fit(features, label)

clf_3 = KNeighborsClassifier(n_neighbors=3)
clf_3.fit(features, label)

clf_5 = KNeighborsClassifier(n_neighbors=5)
clf_5.fit(features, label)

clf_7 = KNeighborsClassifier(n_neighbors=7)
clf_7.fit(features, label)

mc = [[-0.3, 1.0], [-0.5, -0.1], [0.6, 0.0]]
predict_class(1, clf_1, mc)
predict_class(3, clf_3, mc)
predict_class(5, clf_5, mc)
predict_class(7, clf_7, mc)


"""
PART 3
Display a 2 x 2 plot showing the decision boundary and the training error for k = 1, 3, 5, 7.
"""
# Note: the plots may show errors more than stated in the titles. However, if zooming in,
# some seemingly error points are actually located in correct areas.
print("It is working on plots ......")
print("Please wait ......")
fig = plt.figure()
plt.suptitle("Microchip Decision Boundary")

p1 = plt.subplot(221)
p1.set_title("k = 1, training errors = 1")
draw_subplot(clf_1)

p2 = plt.subplot(222)
p2.set_title("k = 3, training errors = 16")
draw_subplot(clf_3)

p3 = plt.subplot(223)
p3.set_title("k = 5, training errors = 13")
draw_subplot(clf_5)

p4 = plt.subplot(224)
p4.set_title("k = 7, training errors = 19")
draw_subplot(clf_7)
fig.tight_layout()
plt.show()
"""
<<<<<< Exercise 4 End >>>>>>
"""


