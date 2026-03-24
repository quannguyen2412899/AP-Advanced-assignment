package strategies;

import java.util.BitSet;
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
    public BitSet mutate(BitSet chromosome, int length, RandomUtil random) {
        if (chromosome == null) throw new IllegalArgumentException("BitFlipMutation.mutate: chromosome is null");
        if (length < chromosome.size()) throw new IllegalArgumentException("BitFlipMutation.mutate: length " + length + " must be >= chromosome size " + chromosome.size());
        
        BitSet mutatedChromosome = (BitSet) chromosome.clone();
        for(int i = 0; i < length; i++) {
            if (random.nextBernoulli(ratePerBit)) {
                mutatedChromosome.flip(i);
            }
        }
        return mutatedChromosome;
    }
}