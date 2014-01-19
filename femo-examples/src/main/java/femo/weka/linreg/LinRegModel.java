package femo.weka.linreg;

import femo.feature.FeatureSet;
import femo.modeling.Example;
import femo.modeling.ExampleDensity;
import femo.modeling.Model;
import femo.weka.common.FemoWekaUtils;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

/**
 * Model implementation for a WEKA Linear Regression
 *
 * @param <DataType> the class of data object which the features can extract values from
 */
public class LinRegModel<DataType> extends Model<DataType, Double, LinRegPrediction> {
    protected LinearRegression classifier;
    protected Instances instances;

    public LinRegModel(FeatureSet<DataType> featureSet, LinearRegression classifier, ArrayList<Attribute> attributes,
                       Attribute classAttribute){
        super(featureSet);
        this.classifier = classifier;
        // Weka is generally used to classify batches at a time so we need a new Instances if we are running one at a time
        // the Instance doesn't even need to get added to Instances, seems like a stupid design
        instances = new Instances("ExampleInstances", attributes, 0);
        instances.setClass(classAttribute);
    }

    @Override
    public LinRegPrediction getPrediction(DataType dataObject) throws Exception {
        Example example = featureSet.getExample(dataObject, ExampleDensity.Sparse);
        Instance instance = FemoWekaUtils.createInstance(instances, example);

        double predictedValue = classifier.classifyInstance(instance);

        return new LinRegPrediction(predictedValue);
    }
}
