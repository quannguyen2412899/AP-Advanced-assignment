package strategies;

import java.util.BitSet;

public class BitFlipMutation implements MutationStrategy {
    private double ratePerBit;

    public BitFlipMutation(double mutationRate) {
        this.ratePerBit = mutationRate;
    }

    public BitFlipMutation(BitFlipMutation other) {
        this.ratePerBit = other.ratePerBit;
    }

    @Override
    public void mutate(BitSet chromosome) {
        /* implementations */
    }

}