package fitnesses;

import java.util.BitSet;

public interface FitnessEvaluator {
    public double evaluate(BitSet chromosome);
}