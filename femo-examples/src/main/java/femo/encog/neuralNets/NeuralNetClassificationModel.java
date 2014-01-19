package femo.encog.neuralNets;

import femo.feature.FeatureSet;
import femo.modeling.Example;
import femo.modeling.ExampleDensity;
import femo.modeling.Model;
import org.encog.ml.data.MLData;
import org.encog.neural.networks.BasicNetwork;

import java.util.ArrayList;

public class NeuralNetClassificationModel<DataType> extends Model<DataType, String, NeuralNetClassificationPrediction> {
    protected BasicNetwork network;
    protected ArrayList<String> classNameLookup;

    public NeuralNetClassificationModel(FeatureSet<DataType> featureSet, BasicNetwork network, ArrayList<String> classNameLookup){
        super(featureSet);
        this.network = network;
        this.classNameLookup = classNameLookup;
    }

    @Override
    public NeuralNetClassificationPrediction getPrediction(DataType dataObject) throws Exception {
        Example example = featureSet.getExample(dataObject, ExampleDensity.Sparse);
        //TODO: confirm it grabs correct activation function
        MLData mlData = NeuralNetBuilder.createMLData(example, ((FemoActivationFunction)network.getActivation(network.getLayerCount()-1)).mean);

        int predictedClassValue = network.classify(mlData);

        return new NeuralNetClassificationPrediction(classNameLookup.get(predictedClassValue));
    }
}
