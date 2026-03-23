package strategies;

import models.Chromosome;
import utils.RandomUtil;

public interface  MutationStrategy {
    Chromosome mutate(Chromosome chromosome, RandomUtil random);
}