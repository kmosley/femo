FeMo
=============
FeMo (FeatureModeling) is an API for Java machine learning hobbyists to experiment with different learning algorithms.
The purpose of this project is to separate extracting features from a data object from running a learning algorithm.
This way, if you want to compare two different random forest implementations you just need to write a Model builder for each
implementation which knows how to handle different feature values and you can pass the same set of Examples to each implementation.

##Notable Classes

###Example
A holder for the predictor features of a single data object, sometimes called instances or independent variables.

###Model
Abstract class to hold a trained model for a specific learning method whose generic input type is tied to a specific data object. This object can be serialized and used later for prediction.

###Prediction
Abstract class to hold a Model prediction. Can simply be a wrapper for a single value or hold meta-data about the prediction based on the learning method.

###Feature
An abstract class to extract a piece of data from a data object.

###FeatureSet
A collection of Features to be extracted from a data object. This gets serialized with a Model so you can just pass data objects in and know the features you trained on are those being used at prediction.