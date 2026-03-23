package fitnesses;

import java.util.BitSet;

public class KnapsackFitnessEvaluator implements FitnessEvaluator {
    private int length;
    private double[] weights;
    private double[] values;
    private double capacity;

    public KnapsackFitnessEvaluator(int length, double[] weights, double[] values, double capacityOverTotal) {
        if(weights.length != values.length) throw new IllegalArgumentException("KnapsackFitnessEvaluator constructor: weights length " + weights.length + " != values length " + values.length);
        if(weights.length != length) throw new IllegalArgumentException("KnapsackFitnessEvaluator constructor: array length " + weights.length + " != expected chromosome length " + length);
        this.length = length;
        this.weights = weights.clone();
        this.values = values.clone();
        double totalWeight = 0;
        for (double w : weights) {
            totalWeight += w;
        }
        this.capacity = totalWeight * capacityOverTotal;
    }

    @Override
    public double evaluate(BitSet chromosome) {
        if(chromosome.length() > length) throw new IllegalArgumentException("KnapsackFitnessEvaluator::evaluate(): BitSet length " + chromosome.length() + " > expected length " + length);
        double totalWeight = 0;
        double totalValue = 0;
        for(int i = 0; i < length; i++) {
            totalWeight += (chromosome.get(i) ? weights[i] : 0.0);
            totalValue += (chromosome.get(i) ? values[i] : 0.0);
        }
        return (totalWeight > capacity) ? 0 : totalValue;
    }
}