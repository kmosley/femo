FeMo
=============
An api for Java machine learning enthusiasts to experiment with different learning algorithms. The purpose of this project is to separate the logic of extracting features from a data object from running a learning algorithm over examples of those features.
This way, if you want to compare two different random forest implementations you just need to write a Model for each implementation which knows how to handle the feature values and you can pass the same set of Examples to each implementation.