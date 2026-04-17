package strategies;

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
    public boolean[][] crossover(boolean[] parent1, boolean[] parent2, RandomUtil random) {
        if (parent1 == null) throw new IllegalArgumentException("OnePointCrossover.crossover: parent1 is null");
        if (parent2 == null) throw new IllegalArgumentException("OnePointCrossover.crossover: parent2 is null");
        if (parent1.length != parent2.length) throw new IllegalArgumentException("OnePointCrossover.crossover: parents must have same length");
        
        int length = parent1.length;
        boolean[][] offspring = new boolean[2][length];
        
        // Copy parents to offspring
        System.arraycopy(parent1, 0, offspring[0], 0, length);
        System.arraycopy(parent2, 0, offspring[1], 0, length);
        
        // Perform crossover with probability
        if (random.nextBernoulli(crossoverRate)) {
            int swapIndex = random.nextInt(1, length);
            
            // Swap bits from swapIndex to end
            for (int i = swapIndex; i < length; i++) {
                boolean temp = offspring[0][i];
                offspring[0][i] = offspring[1][i];
                offspring[1][i] = temp;
            }
        }
        
        return offspring;
    }
}