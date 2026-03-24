package strategies;

import java.util.BitSet;
import utils.RandomUtil;

public interface CrossoverStrategy {
    BitSet[] crossover(BitSet parent1, BitSet parent2, int length, RandomUtil random);
}