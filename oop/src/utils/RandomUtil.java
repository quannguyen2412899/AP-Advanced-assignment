package utils;

import java.util.random.RandomGenerator;

public class RandomUtil {
    static private RandomUtil randomUtil;
    private RandomGenerator randomGenerator;

    private RandomUtil() {
        /* singleton pattern */
    }

    public static RandomUtil getInstance(int randomSeed) {
        /* singleton pattern */
        return null;
    }

    public RandomGenerator getGenerator() {
        return null;
    }
}