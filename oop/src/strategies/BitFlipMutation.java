package strategies;

import java.util.BitSet;
import utils.RandomUtil;

public class BitFlipMutation implements MutationStrategy {
    private double ratePerBit;

    public BitFlipMutation(double mutationRate) {
        this.ratePerBit = mutationRate;
    }

    public BitFlipMutation(BitFlipMutation other) {
        this.ratePerBit = other.ratePerBit;
    }

    @Override
    public BitSet mutate(BitSet chromosome, RandomUtil random) {
        /* implementations */
        return null;
    }

}