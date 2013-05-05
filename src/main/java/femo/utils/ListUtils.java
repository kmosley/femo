package femo.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public abstract class ListUtils {

    public static <T> ArrayList<ArrayList<T>> splitRandomly(ArrayList<T> arr, double[] ratios, int randomSeed){
        Random r = new Random(randomSeed);
        ArrayList<T> shuffled = new ArrayList<T>(arr);
        Collections.shuffle(shuffled, r);
        return splitInOrder(shuffled, ratios);
    }

    public static <T> ArrayList<ArrayList<T>> splitInOrder(ArrayList<T> arr, double[] ratios){
        double portionSum=0;
        for(double d : ratios)
            portionSum += d;
        int startInd = 0;
        float endInd = 0;
        ArrayList<ArrayList<T>> results = new ArrayList<ArrayList<T>>(ratios.length);
        for(int i=0; i<ratios.length; i++){
            endInd += (float)ratios[i]/portionSum * arr.size();
            int roundedEndInd = Math.round(endInd);
            results.add(new ArrayList<T>(arr.subList(startInd, roundedEndInd)));
            startInd = roundedEndInd;
        }

        return results;
    }
}
