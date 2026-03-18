package strategies;

import models.*;

public interface SelectionStrategy {
    Chromosome select(Population population);
}