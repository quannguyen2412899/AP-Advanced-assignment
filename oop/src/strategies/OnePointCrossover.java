package strategies;

import java.util.BitSet;
import utils.RandomUtil;

public class OnePointCrossover implements CrossoverStrategy {
    private final double crossoverRate;

    public OnePointCrossover(double crossoverRate) {
        if (crossoverRate < 0.0 || crossoverRate > 1.0) throw new IllegalArgumentException("OnePointCrossover: crossoverRate must be in [0.0, 1.0], got " + crossoverRate);
        this.crossoverRate = crossoverRate;
    }

    public OnePointCrossover(OnePointCrossover other) {
        this.crossoverRate = other.crossoverRate;
    }

    @Override
    public BitSet[] crossover(BitSet parent1, BitSet parent2, int length, RandomUtil random) {
        if (parent1 == null) throw new IllegalArgumentException("OnePointCrossover.crossover: parent1 is null");
        if (parent2 == null) throw new IllegalArgumentException("OnePointCrossover.crossover: parent2 is null");
        if (length < parent1.size()) throw new IllegalArgumentException("OnePointCrossover.crossover: length " + length + " must be >= parent1 size " + parent1.size());
        if (length < parent2.size()) throw new IllegalArgumentException("OnePointCrossover.crossover: length " + length + " must be >= parent2 size " + parent2.size());
        
        BitSet[] offspring = new BitSet[2];
        offspring[0] = (BitSet) parent1.clone();
        offspring[1] = (BitSet) parent2.clone();
        
        if (random.nextBernoulli(crossoverRate)) {
            int swapIndex = random.nextInt(1, length);
            
            for (int i = swapIndex; i < length; i++) {
                boolean bit0 = offspring[0].get(i);
                boolean bit1 = offspring[1].get(i);
                offspring[0].set(i, bit1);
                offspring[1].set(i, bit0);
            }
        }
        
        return offspring;
    }
}