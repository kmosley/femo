package femo.weka.randomForests;

import femo.feature.FeatureSet;
import femo.modeling.Example;
import femo.modeling.ExampleDensity;
import femo.modeling.Model;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

/**
 * Model implementation for a WEKA Random Forest
 *
 * @param <DataType> the class of data object which the features can extract values from
 */
public class ForestModel<DataType> extends Model<DataType, ForestPrediction> {
    protected RandomForest forest;
    protected ArrayList<String> classNameLookup;
    protected Instances instances;

    public ForestModel(FeatureSet<DataType> featureSet, RandomForest forest, ArrayList<Attribute> attributes, Attribute classAttribute, ArrayList<String> classNameLookup){
        super(featureSet);
        this.forest = forest;
        this.classNameLookup = classNameLookup;
        // Weka is generally used to classify batches at a time so we need a new Instances if we are running one at a time
        // the Instance doesn't even need to get added to Instances, seems like a stupid design
        instances = new Instances("ExampleInstances", attributes, 0);
        instances.setClass(classAttribute);
    }

    @Override
    public ForestPrediction getPrediction(DataType dataObject) throws Exception {
        Example example = featureSet.getExample(dataObject, ExampleDensity.Sparse);
        Instance instance = ForestBuilder.createInstance(instances, example);

        int predictedClassValue = (int)Math.round(forest.classifyInstance(instance));

        return new ForestPrediction(classNameLookup.get(predictedClassValue));
    }
}
