package utils;

import java.util.random.RandomGenerator;

public class RandomUtil {
    static private RandomUtil randomUtil;
    private RandomGenerator randomGenerator;

    private RandomUtil() {
        /* singleton pattern */
    }

    public static void init(int seed) {
        
    }

    public static RandomUtil getInstance() {
        /* singleton pattern */
        return null;
    }

    public RandomGenerator getGenerator() {
        return null;
    }
}