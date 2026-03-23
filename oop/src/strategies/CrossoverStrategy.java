package strategies;

import models.Chromosome;
import utils.RandomUtil;

public interface CrossoverStrategy {
    Chromosome[] crossover(Chromosome p1, Chromosome p2, RandomUtil random);
}