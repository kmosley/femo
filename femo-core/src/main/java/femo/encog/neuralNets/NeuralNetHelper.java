package femo.encog.neuralNets;

import femo.feature.DoubleFeature;
import femo.feature.Feature;
import femo.normalizer.ClampedNormalizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class NeuralNetHelper {
    public static <DataType> List<Feature<DataType>> normalizeFeatures(List<DoubleFeature<DataType>> features, FemoActivationFunction activationFunction, Iterator<DataType> dataIterator) throws Exception {
        List<ClampedNormalizer> normalizers = new ArrayList<ClampedNormalizer>(features.size());
        double[] minValues = new double[features.size()];
        double[] maxValues = new double[features.size()];
        Arrays.fill(minValues, Double.MAX_VALUE);
        Arrays.fill(maxValues, Double.MIN_VALUE);
        while(dataIterator.hasNext()){
            DataType dataObject = dataIterator.next();
            for(int i=0; i<features.size(); i++){
                DoubleFeature<DataType> feature = features.get(i);
                Double d = feature.getFeatureValue(dataObject).getValue();
                if(d != null){
                    if(d < minValues[i])
                        minValues[i] = d;
                    if(d > maxValues[i])
                        maxValues[i] = d;
                }
            }
        }

        List<Feature<DataType>> normalizedFeatures = new ArrayList<Feature<DataType>>(features.size());
        for(int i=0; i<features.size(); i++){
            final DoubleFeature<DataType> feature = features.get(i);
            final ClampedNormalizer normalizer = new ClampedNormalizer(minValues[i], maxValues[i], activationFunction.min+.001, activationFunction.max-.001);
            normalizedFeatures.add(new DoubleFeature<DataType>(feature.getName()+"_normalized") {
                @Override
                public Double getDoubleValue(DataType inputData) throws Exception {
                    return normalizer.normalize(feature.getDoubleValue(inputData));
                }
            });
        }
        return normalizedFeatures;
    }
}
