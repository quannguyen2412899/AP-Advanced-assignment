package strategies;

import java.util.BitSet;
import utils.RandomUtil;

public class OnePointCrossover implements CrossoverStrategy {
    private double crossoverRate;

    public OnePointCrossover(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }

    public OnePointCrossover(OnePointCrossover other) {
        this.crossoverRate = other.crossoverRate;
    }

    @Override
    public BitSet[] crossover(BitSet parent1, BitSet parent2, RandomUtil random) {

        /* implementations */
        
        return null;
    }
    
}