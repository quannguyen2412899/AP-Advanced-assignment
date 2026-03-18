package strategies;

import java.util.BitSet;
import models.Chromosome;

public interface CrossoverStrategy {
    BitSet[] crossover(Chromosome p1, Chromosome p2);
}