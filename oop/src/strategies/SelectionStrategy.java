package strategies;

import models.*;
import utils.RandomUtil;

public interface SelectionStrategy {
    Chromosome select(Population population, RandomUtil random);
}