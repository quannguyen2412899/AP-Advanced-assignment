package strategies;

import models.Chromosome;

public interface CrossoverStrategy {
    Chromosome[] crossover(Chromosome p1, Chromosome p2);
}