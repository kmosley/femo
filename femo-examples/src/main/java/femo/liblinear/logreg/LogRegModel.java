package femo.liblinear.logreg;

import de.bwaldvogel.liblinear.*;
import femo.exception.FemoValidationException;
import femo.featureset.FeatureSet;
import femo.featureset.FeatureSetUtils;
import femo.liblinear.common.LibLinearUtils;
import femo.modeling.*;
import femo.modeling.Model;

public class LogRegModel<DataType> extends Model<DataType, Double, LogRegPrediction> {

    de.bwaldvogel.liblinear.Model model;

    public LogRegModel(FeatureSet<DataType> featureSet, de.bwaldvogel.liblinear.Model model) throws FemoValidationException {
        super(FeatureSetUtils.expandToDoubleOnly(featureSet));
        this.model = model;
    }

    @Override
    public LogRegPrediction getPrediction(DataType dataObj) throws Exception {
        Example example = featureSet.getExample(dataObj, ExampleDensity.Dense);
        de.bwaldvogel.liblinear.Feature[] instance = LibLinearUtils.createInstance(example);
        double prediction = Linear.predict(model, instance);
        return new LogRegPrediction(prediction);
    }
}
