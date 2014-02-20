package femo.liblinear.logreg;

import de.bwaldvogel.liblinear.*;
import femo.exception.FemoValidationException;
import femo.exception.InvalidFeatureException;
import femo.exception.InvalidFeatureValueException;
import femo.feature.DoubleFeature;
import femo.feature.EnumFeature;
import femo.feature.Feature;
import femo.feature.StringFeature;
import femo.featureset.FeatureSetUtils;
import femo.liblinear.common.LibLinearUtils;
import femo.modeling.*;

import java.io.PrintStream;
import java.util.List;

public class LogRegBuilder<DataType>
        implements ModelBuilder<DataType, Double, LogRegPrediction, LogRegModel<DataType>> {

    protected SolverType solverType = SolverType.L2R_LR; // -s 0
    protected double costOfConstraintViolation = 1.0; // C
    protected double stoppingCriteria = 0.01; // eps
    protected PrintStream debugOutput = null;

    @Override
    public <ResponseDataType> LogRegModel<DataType> buildModel(
            TrainingSet<DataType, ResponseDataType, Double> trainingSet) throws Exception {

        validateTrainingSet(trainingSet);

        trainingSet = new TrainingSet<>(FeatureSetUtils.expandToDoubleOnly(trainingSet.getFeatureSet()), trainingSet.getDataObjectsIterator(),
                trainingSet.getResponseFeature(), trainingSet.getResponseIterator());

        List<TrainingExample<Double>> trainingExamples = trainingSet.generateAllExamples(ExampleDensity.Dense);

        Problem problem = new Problem();
        problem.l = trainingExamples.size(); // number of training examples
        problem.n = trainingSet.getFeatureSet().getPredictorFeatures().size(); // number of features
        problem.x = new de.bwaldvogel.liblinear.Feature[problem.l][problem.n];
        for(int i=0; i<trainingExamples.size(); i++){
            problem.x[i] = LibLinearUtils.createInstance(trainingExamples.get(i));
        }
        problem.y = new double[trainingExamples.size()];
        for(int i=0; i<trainingExamples.size(); i++){
            problem.y[i] = trainingExamples.get(i).responseFeatureValue.getValue();
        }

        Parameter parameter = new Parameter(solverType, costOfConstraintViolation, stoppingCriteria);
        Linear.setDebugOutput(debugOutput);
        de.bwaldvogel.liblinear.Model model = Linear.train(problem, parameter);

        LogRegModel femoModel = new LogRegModel(trainingSet.getFeatureSet(), model);

        return femoModel;
    }

    protected <DataType, ResponseDataType, Double> void validateTrainingSet(
            TrainingSet<DataType, ResponseDataType, Double> trainingSet) throws FemoValidationException {

        if(!(trainingSet.getResponseFeature() instanceof DoubleFeature)){
            throw new InvalidFeatureValueException("response feature for linear regression must return a double: "
                    +trainingSet.getResponseFeature().getName());
        }

        for(Feature feature : trainingSet.getFeatureSet().getPredictorFeatures()){
            if(!(feature instanceof DoubleFeature)
                    && !(feature instanceof StringFeature)
                    && !(feature instanceof EnumFeature))
                throw new InvalidFeatureException("feature must be a Double, String, or Enum feature");
        }
    }

    public LogRegBuilder<DataType> setSolverType(SolverType solverType) {
        this.solverType = solverType;
        return this;
    }

    public LogRegBuilder<DataType> setCostOfConstraintViolation(double costOfConstraintViolation) {
        this.costOfConstraintViolation = costOfConstraintViolation;
        return this;
    }

    public LogRegBuilder<DataType> setStoppingCriteria(double stoppingCriteria) {
        this.stoppingCriteria = stoppingCriteria;
        return this;
    }

    public void setDebugOutput(PrintStream debugOutput) {
        this.debugOutput = debugOutput;
    }
}
