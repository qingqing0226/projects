import numpy as np
import matplotlib.pyplot as plt

# read file
data = np.loadtxt("housing_price_index.csv", delimiter=",")

"""
Functions start
"""


# compute beta based on x_e and y_
def get_beta(x_e, y_):
    return np.linalg.inv(x_e.T.dot(x_e)).dot(x_e.T).dot(y_)


# predict housing price index x_beta based on degree of the model
def predict(degree, x_col, beta):
    x_beta = []
    for cell in x_col:
        if degree == 1:
            x_beta.append(np.c_[1, cell].dot(beta))
        if degree == 2:
            x_beta.append(np.c_[1, cell, cell ** 2].dot(beta))
        if degree == 3:
            x_beta.append(np.c_[1, cell, cell ** 2, cell ** 3].dot(beta))
        if degree == 4:
            x_beta.append(np.c_[1, cell, cell ** 2, cell ** 3, cell ** 4].dot(beta))
    return np.array(x_beta)


# plot predicted housing price index based on degrees
def draw_subplot(no, x_axis, point_y, curve_y):
    plt.subplot(2, 2, no)
    plt.title("Degree " + str(no))
    plt.xlabel("X")
    plt.ylabel("Y")
    plt.plot(x_axis, point_y, ".")
    plt.plot(x_axis, curve_y, "-")


# compute mse by using cost function
def mse(xe, b, y_):
    j = np.dot(xe, b) - y_
    c = (j.T.dot(j)) / len(xe)
    return round(c, 2)


"""
Functions end
"""

"""
Part 1 Plot the data in the matrix housing_price_index.
"""
x_axis = np.array(list(range(1975, 2018)))
y = data[:, 1]
plt.plot(x_axis, y, ".")
plt.xlabel("year")
plt.ylabel("housing price index")
plt.title("housing price index for houses in Småland from the year 1975 to 2017")
plt.show()

"""
Part 2
Degree 4 model gives the best fit because it has the lowest MSE value 443.76
"""
# create extended x matrix for degree 1-4
x = np.array(list(range(43)))
x_e1 = np.c_[np.ones((len(data), 1)), x]
x_e2 = np.c_[x_e1, x ** 2]
x_e3 = np.c_[x_e2, x ** 3]
x_e4 = np.c_[x_e3, x ** 4]

x_e_list = [x_e1, x_e2, x_e3, x_e4]  # a list of extended x matrix for models of degree 1-4
beta_list = []  # a list of beta values for models of degree 1-4
y_list = []  # a list of response vectors for models of degree 1-4
mse_list = []   # a list of mse values (cost function) for models of degree 1-4

for degree in range(4):
    beta_list.append(get_beta(x_e_list[degree], y))
    y_list.append(predict(degree + 1, x, beta_list[degree]))
    mse_list.append(mse(x_e_list[degree], beta_list[degree], y))


print("\nPart 2")
print("Degree 4 model gives the best fit because it has the lowest MSE value 443.76")
print("degree 1 mse = ", mse_list[0], "\ndegree 2 mse = ", mse_list[1], "\ndegree 3 mse = ", mse_list[2], "\ndegree 4 mse = ", mse_list[3])

# plot predicted housing price index for models of degree 1-4 plus original data
fig = plt.figure()
plt.suptitle("Polynomial Models with Degree 1-4")
for p in range(4):
    draw_subplot(p+1, x, y, y_list[p])
fig.tight_layout()
plt.show()

"""
Part 3
Jonas Nordqvist can expect to sell the house with the price around 3.2 million in 2022
"""
# use degree 4 model to compute housing price index for year 2015, 2022
hpi2015 = predict(4, np.array([40]), beta_list[3])[0]
hpi2022 = predict(4, np.array([47]), beta_list[3])[0]
print("\nPart 3")
print("2022 predicted house price: %.1f million" % (2.3 * hpi2022/hpi2015))  # 3.2 million
