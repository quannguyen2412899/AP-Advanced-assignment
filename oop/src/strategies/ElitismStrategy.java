package strategies;

import models.*;

public interface ElitismStrategy {
    Chromosome[] select(Population population);
}