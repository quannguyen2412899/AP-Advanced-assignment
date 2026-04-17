package strategies;

import utils.RandomUtil;

public class BitFlipMutation implements MutationStrategy {
    private double ratePerBit;

    public BitFlipMutation(double mutationRate) {
        if (mutationRate < 0.0 || mutationRate > 1.0) throw new IllegalArgumentException("BitFlipMutation: mutationRate must be in [0.0, 1.0], got " + mutationRate);
        this.ratePerBit = mutationRate;
    }

    public BitFlipMutation(BitFlipMutation other) {
        this.ratePerBit = other.ratePerBit;
    }

    @Override
    public boolean[] mutate(boolean[] chromosome, RandomUtil random) {
        if (chromosome == null) throw new IllegalArgumentException("BitFlipMutation.mutate: chromosome is null");
        
        int length = chromosome.length;
        boolean[] mutated = new boolean[length];
        for (int i = 0; i < length; i++) {
            boolean bit = chromosome[i];
            if (random.nextBernoulli(ratePerBit)) {
                mutated[i] = !bit;
            } else {
                mutated[i] = bit;
            }
        }
        return mutated;
    }
}