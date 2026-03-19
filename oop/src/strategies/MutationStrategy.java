package strategies;

import models.Chromosome;

public interface  MutationStrategy {
    Chromosome mutate(Chromosome chromosome);
}