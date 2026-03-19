package strategies;

import models.Chromosome;

public class BitFlipMutation implements MutationStrategy {
    private double ratePerBit;

    public BitFlipMutation(double mutationRate) {
        this.ratePerBit = mutationRate;
    }

    public BitFlipMutation(BitFlipMutation other) {
        this.ratePerBit = other.ratePerBit;
    }

    @Override
    public Chromosome mutate(Chromosome chromosome) {
        /* implementations */
        return null;
    }

}