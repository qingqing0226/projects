import numpy as np
from sklearn import svm
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import GridSearchCV

# Be aware that this code runs quite slowly due to the size of data (you can just check the comments for results)
# I reduced the training set size from 60000 to 10000 and # of features from 784 to 667 (still very slow)
"""
<<<<<<< Data processing start >>>>>>>
"""
print("The datasets are large. It will take some time to load.")
# you can adjust dataset sizes by giving different values for 'skiprows'
train_set = np.loadtxt("mnist_train.csv", delimiter=",", skiprows=50001)  # 600001 rows = 1 label + 60000 samples
X = train_set[:, 1:]  # 10000 x 784
y = train_set[:, 0]  # 10000 x 1
test_set = np.loadtxt("mnist_test.csv", delimiter=",", skiprows=1)  # 10001 rows = 1 label + 10000 samples
x_test = test_set[:, 1:]  # 10000 x 784
y_test = test_set[:, 0]  # 10000 x 1
# remove non-changing features (std=0)
delete_cols = []
for col in range(X.shape[1]):
    if np.std(X[:, col]) == 0:
        delete_cols.append(col)
X = np.delete(X, delete_cols, 1)  # 10000 x 667
x_test = np.delete(x_test, delete_cols, 1)  # 10000 x 667
print("After removing un-changed columns, current number of  feature columns:", X.shape[1])  # reduced to 667
"""
<<<<<<< Data processing end >>>>>>>
"""

"""
<<<<<<< Part 1 >>>>>>>
I used GridSearchCV
best parameters: {'C': 10, 'gamma': 'scale', 'kernel': 'rbf'}
svm Accuracy:  96.87 %
"""
# find best parameters by using GridSearchCV (NOTE! time-consuming. takes a few minutes to run)
parameters = {'kernel': ['rbf'], 'C': [1, 10, 100, 1000], 'gamma': ['scale']}
clf = GridSearchCV(svm.SVC(), parameters, scoring="accuracy")
clf.fit(X, y)
print("Part 1")
print("best parameters: ", clf.best_params_)  # {'C': 10, 'gamma': 'scale', 'kernel': 'rbf'}
mean = clf.cv_results_["mean_test_score"]
print("mean_test_score for best params: ", round(mean.max(), 3), "\n")  # 0.966

# compute accuracy by using test set (takes a few minutes to run)
clf = svm.SVC(kernel='rbf', C=10)
clf.fit(X, y)
y_pred = clf.predict(x_test)
accuracy = (np.sum(y_pred == y_test)/len(y_test))*100
print("Accuracy: ", round(accuracy, 2), "%\n")  # 96.87 %


"""
<<<<<<< Part 2 >>>>>>>
svm one-vs-one accuracy:  96.87 %
my one-vs-all accuracy: 85.81 %
In terms of mis-classification, the svm one-vs-one has higher accuracy than my one-vs-one method. 
Thus, svm one-vs-one performs better.
"""
# the following is my implementation of one-vs-one method
trained_clf = []  # train a classifier for each class
for i in range(10):  # loop classes 0-9
    temp_y = []  # a y list of 1s and 0s
    for j in y:
        if j == i:
            temp_y.append(1)  # replace the selected class with 1
        else:
            temp_y.append(0)  # replace the rest with 0
    clf = LogisticRegression(solver='liblinear', tol=1e-6, C=10,  max_iter=5000).fit(X, np.array(temp_y))
    trained_clf.append(clf)
print("Part 2")
print("10 classifiers are created")

# compute probabilities ndarrays for each classifier and add them to 'pp' list
pp = []
for c in trained_clf:
    p = c.predict_proba(x_test)
    pp.append(p)

# predict y by taking the class with the highest probability
y_predic = []
for x in range(x_test.shape[0]):
    p_list = [pro[x, 1] for pro in pp]  # a list of predictions on the same x value with different classifiers
    # get the index of the highest probability. Note: the index = class(0-9)
    y_predic.append(p_list.index(max(p_list)))

# compute accuracy for one-vs-all method
accuracy = (np.sum(np.array(y_predic) == y_test)/len(y_test))*100
print("My one-vs-all method accuracy: ", round(accuracy, 2), "%")  # 85.81 %
