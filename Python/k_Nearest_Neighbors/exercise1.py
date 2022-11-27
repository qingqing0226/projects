import matplotlib.pyplot as plt
import numpy as np
import math

"""
Reading Data from "microchips.csv"
"""
rawData = np.loadtxt("microchips.csv", delimiter=",", usecols=(0, 1, 2))
ok = rawData[:59, :]
fail = rawData[59:, :]

"""
<<<<<< Functions Section Start >>>>>>
"""


# calculate distance between point and each row in the training set
# add it as fourth column, sort the new array in ascending order
def sortArray(point, arr):
    newCol = []  # distances
    for row in arr:
        newCol.append([math.sqrt((row[0] - point[0]) ** 2 + (row[1] - point[1]) ** 2)])
    newArray = np.append(arr, np.array(newCol).reshape(arr.shape[0], 1), axis=1)
    return newArray[newArray[:, 3].argsort()]

# predict the y value(Ok/Fail) of microchip by using sort_arr
# and print the result
def print_prediction(chip, chip_no, sort_arr, k):
    okCount, failCount = 0, 0
    for row in sort_arr[:k, :]:
        if row[2] == 1.0:
            okCount += 1
        if row[2] == 0.0:
            failCount += 1
    print("Chip", chip_no, ": ", chip, " ==> ", "OK" if okCount > failCount else "Fail")


# predict the y value(1.0/0.0) of microchip by using sorted_arr with respect to k
# return y
def ok_or_fail(k, sorted_arr):
    okCount, failCount = 0, 0
    for row in sorted_arr[:k, :]:
        if row[2] == 1.0:
            okCount += 1
        if row[2] == 0.0:
            failCount += 1
    return 1.0 if okCount > failCount else 0.0


# make a mesh with coordinates [i, j, predict y],
# i, j in interval [-1.0, 1.2) step 0.02
# predict y: based on prediction by using k value
def mesh(k):
    mesh_list = []
    i, j = -1.0, -1.0
    while i < 1.2:
        while j < 1.2:
            mesh_list.append([i, j, ok_or_fail(k, sortArray([i, j], rawData))])
            j += 0.02
        i += 0.02
        j = -1.0
    ok_mesh = np.array([row for row in mesh_list if row[2] == 1.0])
    fail_mesh = np.array([row for row in mesh_list if row[2] == 0.0])
    return ok_mesh, fail_mesh


# draw subplot of decision boundary
def draw_subplot(k):
    ok_mesh, fail_mesh = mesh(k)
    plt.scatter(ok_mesh[:, 0], ok_mesh[:, 1], c="c", marker="s")
    plt.scatter(fail_mesh[:, 0], fail_mesh[:, 1], c="m", marker="s")
    plt.scatter(np.array(ok)[:, 0], np.array(ok)[:, 1], c="g", marker=".")
    plt.scatter(np.array(fail)[:, 0], np.array(fail)[:, 1], c="r", marker=".")


def train_error(k):
    predict_y = []
    for r in rawData:
        predict_y.append(ok_or_fail(k, sortArray([r[0], r[1]], rawData)))
    return np.sum(np.array(predict_y) != np.array(rawData[:, 2]))


"""
<<<<<< Functions Section End >>>>>> 
"""

"""
<<<<<< Exercise 1 Start >>>>>>
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
use k-NN to predict whether three unknown microchips are likely to be OK or Fail.
"""
sorted1 = sortArray([-0.3, 1.0], rawData)
sorted2 = sortArray([-0.5, -0.1], rawData)
sorted3 = sortArray([0.6, 0.0], rawData)
print("k = 1")
print_prediction([-0.3, 1.0], 1, sorted1, 1)
print_prediction([-0.5, -0.1], 2, sorted2, 1)
print_prediction([0.6, 0.0], 3, sorted3, 1)
print("k = 3")
print_prediction([-0.3, 1.0], 1, sorted1, 3)
print_prediction([-0.5, -0.1], 2, sorted2, 3)
print_prediction([0.6, 0.0], 3, sorted3, 3)
print("k = 5")
print_prediction([-0.3, 1.0], 1, sorted1, 5)
print_prediction([-0.5, -0.1], 2, sorted2, 5)
print_prediction([0.6, 0.0], 3, sorted3, 5)
print("k = 7")
print_prediction([-0.3, 1.0], 1, sorted1, 7)
print_prediction([-0.5, -0.1], 2, sorted2, 7)
print_prediction([0.6, 0.0], 3, sorted3, 7)


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
p1.set_title("k = 1, training errors = " + str(train_error(1)))
draw_subplot(1)

p2 = plt.subplot(222)
p2.set_title("k = 3, training errors = " + str(train_error(3)))
draw_subplot(3)

p3 = plt.subplot(223)
p3.set_title("k = 5, training errors = " + str(train_error(5)))
draw_subplot(5)

p4 = plt.subplot(224)
p4.set_title("k = 7, training errors = " + str(train_error(7)))
draw_subplot(7)
fig.tight_layout()
plt.show()
"""
<<<<<< Exercise 1 End >>>>>>
"""