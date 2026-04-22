package utils;

import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

public class RandomUtil {
    private RandomGenerator randomGenerator;

    public RandomUtil(int seed) {
        this.randomGenerator = RandomGeneratorFactory.of("L32X64MixRandom").create(seed);
    }

    public boolean nextBernoulli(double p) {
        if (p < 0.0 || p > 1.0) throw new IllegalArgumentException("Probability must be in [0.0, 1.0], got " + p);
        return randomGenerator.nextDouble() < p;
    }

    public int nextInt(int range) {
        if (range <= 0) throw new IllegalArgumentException("Range must be > 0, got " + range);
        return randomGenerator.nextInt(range);
    }

    public int nextInt(int origin, int bound) {
        if (origin >= bound) throw new IllegalArgumentException("Origin must be less than bound");
        return randomGenerator.nextInt(origin, bound);
    }
}