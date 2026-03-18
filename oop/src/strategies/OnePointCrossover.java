package strategies;

import java.util.BitSet;
import models.Chromosome;

public class OnePointCrossover implements CrossoverStrategy {
    private double crossoverRate;

    public OnePointCrossover(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public OnePointCrossover(OnePointCrossover other) {
        this.crossoverRate = other.crossoverRate;
    }

    @Override
    public BitSet[] crossover(Chromosome p1, Chromosome p2) {

        /* implementations */
        
        return null;
    }
    
}