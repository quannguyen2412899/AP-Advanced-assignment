package fitnesses;

import models.Chromosome;

public interface FitnessEvaluator {
    public double evaluate(Chromosome chromosome);
}