package femo.normalizer;

// EnumFeature is backed by StringFeature which is why InputType generic is String
public class EnumNomalizer extends Normalizer<String, Double> {

    private Double activeValue;
    private Double inactiveValue;
    private Enum active;

    public EnumNomalizer(Enum active, Double inactiveValue, Double activeValue){
        this.active = active;
        this.inactiveValue = inactiveValue;
        this.activeValue = activeValue;
    }

    @Override
    public Double normalize(String input) {
        if(input.equals(active.toString()))
            return activeValue;
        return inactiveValue;
    }
}
