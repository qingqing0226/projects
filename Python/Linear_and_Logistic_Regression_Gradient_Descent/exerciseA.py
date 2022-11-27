import numpy as np
import matplotlib.pyplot as plt
data = np.loadtxt("girls_height.csv", usecols=(0, 1, 2))
# Part 1 plot dataset
fig = plt.figure()
plt.suptitle("Girls Height Dataset Plot")

p1 = plt.subplot(121)
p1.set_title("Girl vs Mom")
p1.set_xlabel("Mom Height (Inches)")
p1.set_ylabel("Girl Height (Inches)")
plt.scatter(data[:, 1], data[:, 0], marker="o")

p2 = plt.subplot(122)
p2.set_title("Girl vs Dad")
p2.set_xlabel("Dad Height (Inches)")
p2.set_ylabel("Girl Height (Inches)")
plt.scatter(data[:, 2], data[:, 0], marker="o")
fig.tight_layout()
plt.show()
# Part 2 extend matrix Xe
x_e = np.c_[np.ones((data.shape[0], 1)), data[:, 1], data[:, 2]]

# Part 3 Normal equation and prediction  of girl height
beta = np.linalg.inv(x_e.T.dot(x_e)).dot(x_e.T).dot(data[:, 0])
# A girl with parent heights (65,70)
print("Part 3")
print("A girl with parent heights (65,70) has a predicted height: ", np.c_[1, 65, 70].dot(beta))
print("------------------------------------------------------")

# Part 4 Feature Normalization
print("Part 4")
u_1 = np.mean(data[:, 1])  # mom mean
u_2 = np.mean(data[:, 2])  # dad mean
sigma_1 = np.std(data[:, 1])  # mom std
sigma_2 = np.std(data[:, 2])  # dad std
# normalized mom dad heights
normalized_x1 = []
normalized_x2 = []
for x in data[:, 1]:
    normalized_x1.append((x - u_1)/sigma_1)
for x in data[:, 2]:
    normalized_x2.append((x - u_2)/sigma_2)
normalized_x1 = np.array(normalized_x1)
normalized_x2 = np.array(normalized_x2)
normalized = np.c_[normalized_x1, normalized_x2]
# new X matrix and beta
x_e = np.c_[np.ones((data.shape[0], 1)), normalized[:, 0], normalized[:, 1]]
beta = np.linalg.inv(x_e.T.dot(x_e)).dot(x_e.T).dot(data[:, 0])
girl_heights = []
for row in normalized:
    girl_heights.append(np.c_[1, row[0], row[1]].dot(beta))
girl_heights = np.array(girl_heights)
print("new mom std: ", np.std(normalized_x1))
print("new dad std: ", np.std(normalized_x2))
# new plot
fig = plt.figure()
plt.suptitle("Girls Height After Normalization")

p1 = plt.subplot(121)
p1.set_title("Girl vs Mom")
p1.set_xlabel("Mom Height (Inches)")
p1.set_ylabel("Girl Height (Inches)")
plt.scatter(normalized_x1, girl_heights, marker="o")

p2 = plt.subplot(122)
p2.set_title("Girl vs Dad")
p2.set_xlabel("Dad Height (Inches)")
p2.set_ylabel("Girl Height (Inches)")
plt.scatter(normalized_x2, girl_heights, marker="o")
fig.tight_layout()
plt.show()
# Part 5 new prediction
print("------------------------------------------------------")
print("Part 5")
print("After normalization:")
print("A girl with parent heights (65,70) has a new predicted height: ", np.c_[1, (65 - u_1)/sigma_1, (70 - u_2)/sigma_2].dot(beta))
# beta = np.linalg.inv(x_e.T.dot(x_e)).dot(x_e.T).dot(np.array(girl_heights))
print("normal equation beta = ", beta)
# Part 6 cost function
# J = (j.T.dot(j))/n where j = np.dot(Xe,beta)-y

j = np.dot(x_e, beta) - data[:, 0]
cost = (j.T.dot(j))/data.shape[0]
print("------------------------------------------------------")
print("Part 6")
print("cost = ", cost)
print("------------------------------------------------------")

# Part 7 Gradient Descent
print("Part 7")
y = data[:, 0].reshape(214, 1)
n = 4000
alpha = 0.00001
beta = np.array([0.0, 0.0, 0.0]).reshape(3, 1)
costs = []

for iter in range(n):
    j = np.dot(x_e, beta) - y   # 214x1
    cost = (j.T.dot(j))/data.shape[0]
    costs.append(cost[0])
    beta -= alpha * np.dot(x_e.T, np.dot(x_e, beta) - y)

print("minimum cost: ", costs[n-1])
print("verify height for a girl with parents (65, 70): ", np.c_[1, (65 - u_1)/sigma_1, (70 - u_2)/sigma_2].dot(beta))
plt.plot(np.array(list(range(n))), np.array(costs), "-")
plt.xlabel("iterations")
plt.ylabel("cost function")
plt.show()




