package femo.liblinear.common;

import de.bwaldvogel.liblinear.FeatureNode;
import femo.feature.Feature;
import femo.feature.FeatureValue;
import femo.featureset.FeatureSet;
import femo.modeling.Example;

import java.util.ArrayList;

public abstract class LibLinearUtils {
    public static de.bwaldvogel.liblinear.Feature[] createInstance(Example example){
        de.bwaldvogel.liblinear.Feature[] instance = new de.bwaldvogel.liblinear.Feature[example.getPredictorFeatureValues().size()];
        ArrayList<FeatureValue> featureValues = example.getPredictorFeatureValues();
        for(int i=0; i<featureValues.size(); i++){
            if(featureValues.get(i).getValue() != null){
                // cast to double is safe since we converted all features to return doubles
                instance[i] = new FeatureNode(i+1, (Double)featureValues.get(i).getValue());
            }
            else
                instance[i] = new FeatureNode(i+1, 0);
        }
        return instance;
    }
}
