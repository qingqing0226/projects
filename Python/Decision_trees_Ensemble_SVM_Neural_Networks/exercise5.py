import numpy as np
import matplotlib.pyplot as plt
from sklearn.model_selection import GridSearchCV
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import plot_confusion_matrix

# read file and create training, test datasets
train = np.loadtxt("fashion-mnist_train.csv", delimiter=",", skiprows=1)
X_train = train[:, 1:]
Y_train = train[:, 0]
test = np.loadtxt("fashion-mnist_test.csv", delimiter=",", skiprows=1)
x_test = test[:, 1:]
y_test = test[:, 0]


# Part 1 Plot 16 random samples from the training set with the corresponding labels
indexes = np.random.choice(60000, 16)  # pick 16 samples to plot
samples = X_train[indexes]
labels = Y_train[indexes]

for i in range(16):
    ax = plt.subplot(4, 4, (i+1))
    ax.set_xticks([])
    ax.set_yticks([])
    image = samples[i].reshape(28, 28)
    plt.imshow(image, cmap='gray_r')
    ax.set_title(str(int(labels[i])))
plt.tight_layout()
plt.show()

# Part 2 Train a multilayer perceptron (remove double quotation marks if you want to run this part)

parameters = {'hidden_layer_sizes': [(10,), (10, 10), (10, 10, 10)],
              'activation': ['identity', 'logistic', 'tanh', 'relu'], 'solver': ['sgd', 'adam'], 
              'alpha': [0.001, 0.0001, 0.00001]}
clf = GridSearchCV(MLPClassifier(), parameters, scoring="accuracy")
clf.fit(X_train[:5000, :], Y_train[:5000])   # you can change the size of dataset here
print(clf.best_params_) # {'activation': 'identity', 'alpha': 0.001, 'hidden_layer_sizes': (10,), 'solver': 'adam'}


# Part 3 Plot the confusion matrix
clf = MLPClassifier(activation='identity', alpha=0.001, hidden_layer_sizes=(10,), solver='adam')
clf.fit(X_train, Y_train)
plot_confusion_matrix(clf, x_test, y_test)
plt.show()

"""
Categories 1 and 9 are the easy ones to classify, with correct number of predictions 944, 972 respectively.
Category 6 is the hard one to classify, with correct number of predictions 418.
Category 2 is often mixed with categories 4, 6.
Category 4 is often mixed with categories 6.
Category 6 is often mixed with categories 0, 2, 3, 4.
"""