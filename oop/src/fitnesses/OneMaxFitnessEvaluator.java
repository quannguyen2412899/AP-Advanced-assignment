package fitnesses;

import java.util.BitSet;

public class OneMaxFitnessEvaluator implements FitnessEvaluator {
    private int length;

    public OneMaxFitnessEvaluator(int length) {
        this.length = length;
    }

    @Override
    public double evaluate(BitSet chromosome) {
        if(chromosome.length() > length) throw new IllegalArgumentException("OneMaxFitnessEvaluator::evaluate(): BitSet length " + chromosome.length() + " > expected length " + length);
        return chromosome.cardinality();
    }
}