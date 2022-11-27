import numpy as np
from sklearn.linear_model import LinearRegression
from sklearn.model_selection import cross_val_score
from sklearn.metrics import mean_squared_error

"""
Part 1 Implement the forward selection algorithm
"""
# read file
data = np.loadtxt("GPUbenchmark.csv", delimiter=",")
used_col = []  # the already used col numbers
best_models = []  # a list of best models (each presented by a list of col numbers)


# it takes already used feature column numbers
# returns the best model(columns)
def get_best(used):
    candidates = []  # new models
    mse_list = []  # corresponding mse for new models
    mdl = LinearRegression()
    for c in range(6):
        if used.count(c) == 0:  # if the col hasn't been used
            candidates.append(used + [c])  # create new model
            mdl.fit(data[:, used + [c]], data[:, 6])  # fit data
            y_pred = mdl.predict(data[:, used + [c]])  # predict y
            mse_list.append(mean_squared_error(data[:, 6], y_pred))  # compute mse and append to the list

    best_models.append(candidates[np.argmin(mse_list)])    # append the best model from candidates by using min mse
    return candidates[np.argmin(mse_list)]


while len(used_col) < 6:
    used_col = get_best(used_col)
print("Part 1")
print("The models (the numbers represent the feature column numbers)")
for m in best_models:
    print(m)


"""
Part 2 Apply your forward selection on the GPUbenchmark.csv. Use 3-fold cross-validation to find the best model
"""
print("\nPart 2")
print("the model with columns [4, 5, 2, 0] is the best one")
print("because it has comparatively low average absolute score (2.2463)")
print("feature at column 4 is the most important one because it was selected first\n")
for m in best_models:
    model = LinearRegression()
    model.fit(data[:, m], data[:, 6])  # fit the model with data
    score = cross_val_score(model, data[:, m], data[:, 6], cv=3)
    print("Model with columns ", m)
    print('absolute score:', abs(score))
    print('average absolute score:', (abs(score).mean()), "\n")




