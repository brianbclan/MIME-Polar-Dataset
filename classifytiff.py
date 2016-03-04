__author__ = 'yeh'
# from sklearn.feature_selection import SelectKBest
# from sklearn.feature_selection import chi2
# from sklearn.feature_selection import f_regression
# from sklearn.ensemble import BaggingRegressor
# from sklearn.tree import DecisionTreeRegressor
# from sklearn.ensemble import AdaBoostRegressor
# from sklearn.feature_selection import SelectKBest, chi2
# from sklearn import neighbors
# from sklearn.ensemble import RandomForestRegressor
# from sklearn.ensemble import GradientBoostingRegressor
from sklearn.linear_model import LogisticRegression
from sklearn import tree
from sklearn.svm import SVC
from sklearn.neighbors import KNeighborsClassifier
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis
from sklearn.naive_bayes import BernoulliNB
from sklearn.naive_bayes import GaussianNB
import numpy as np
import pandas as pd
import sys
from sklearn import linear_model
from sklearn import cross_validation
from sklearn import metrics

from sklearn import linear_model
from sklearn.externals import joblib

input_file = '/Users/yeh/2016_Spring/599_DC/hw1/contentml/clean_data.csv'
input_file_test = '/Users/yeh/2016_Spring/599_DC/hw1/contentml/test_clean_data.csv'
output_file = '/Users/yeh/2016_Spring/599_DC/hw1/contentml/model.pkl'
out = open( output_file, 'w')



df = pd.read_csv(input_file, header = 0)
X_train = df.drop('label',axis=1)
y_train = df['label']

df_test = pd.read_csv(input_file_test, header = 0)
X_test = df_test.drop('label',axis=1)
y_test = df_test['label']

classifer = tree.DecisionTreeClassifier( )
#classifer = LogisticRegression(penalty='l1')
classifer.fit(X_train,y_train)
joblib.dump(classifer, output_file)
result = classifer.predict( X_test )
# #alpha =[ .1 , 4 ];
# regr = linear_model.RidgeCV(alphas=alpha, fit_intercept=True, normalize=True, scoring='mean_absolute_error', cv=5, gcv_mode=None, store_cv_values=False)
# regr.fit(X_train, y_train);
# print(regr.alpha_)
# final_alpha = regr.alpha_
# regr = linear_model.Ridge( alpha=regr.alpha_, copy_X=True, fit_intercept=True, max_iter=None, normalize=False, solver='auto', tol=0.001  );
# regr.fit(X_train, y_train);
# result = regr.predict(X_test)
print sum(result)
print len(result)
print sum(y_test)
print len(y_test)
postive_index = y_test == 1
negative_index = y_test == 0
TruePostive = 0
FalsePostive= 0
TrueNegative = 0
FalseNegative = 0
for i in range(len(result)):
    if y_test.iloc[i] == 1:
        if result[i] == 1:
            TruePostive = TruePostive + 1
        else:
            FalseNegative = FalseNegative +1
    else:
        if result[i] == 0:
            TrueNegative = TrueNegative + 1
        else:
            FalsePostive = FalsePostive +1


print np.sum(TruePostive) *1.0 / len(result)
print np.sum(FalseNegative) *1.0/len(result)
print np.sum(FalsePostive) *1.0 /len(result)
print np.sum(TrueNegative) *1.0 / len(result)
print len(result)
print len(y_test)
print("Mean Absolute Error: %.4f"
       % ( np.mean( abs( result - y_test) ) ) )
print("Mean Square Error: %.4f"
       % ( np.mean( ( result - y_test) * ( result - y_test) ) ) )