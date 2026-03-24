package strategies;

import java.util.BitSet;
import utils.RandomUtil;

public interface  MutationStrategy {
    BitSet mutate(BitSet chromosome, RandomUtil random);
}