package fitnesses;

public class OneMaxFitnessEvaluator implements FitnessEvaluator {
    private int length;

    public OneMaxFitnessEvaluator(int length) {
        this.length = length;
    }

    @Override
    public double evaluate(boolean[] chromosome) {
        if (chromosome == null) throw new IllegalArgumentException("OneMaxFitnessEvaluator.evaluate: chromosome is null");
        if (chromosome.length != length) throw new IllegalArgumentException("OneMaxFitnessEvaluator.evaluate: chromosome length " + chromosome.length + " > expected length " + length);
        
        int count = 0;
        for (boolean bit : chromosome) {
            if (bit) count++;
        }
        return count;
    }
}