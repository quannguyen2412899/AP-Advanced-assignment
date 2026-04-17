package strategies;

import utils.RandomUtil;

public interface  MutationStrategy {
    // BitSet mutate(BitSet chromosome, int length, RandomUtil random);
    boolean[] mutate(boolean[] chromosome, RandomUtil random);
}