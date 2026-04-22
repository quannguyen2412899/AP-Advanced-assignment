package fitnesses;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/fitnesses/*.java tests/fitnesses/TestKnapsack.java
 * java -ea -cp bin fitnesses.TestKnapsack
 */
public class TestKnapsack {
    /**
     * Test 01: Knapsack - all items excluded (fitness = 0, weight = 0)
     * Tests: No items selected, total value 0
     * Setup: items=[w:1,v:10], [w:2,v:20], [w:3,v:30] capacity=3.0 (100% of 6)
     * Evaluation: chromosome=[F,F,F] => weight=0, value=0, within capacity => fitness=0
     */
    public static void test_01() {
        double[] weights = {1.0, 2.0, 3.0};
        double[] values = {10.0, 20.0, 30.0};
        KnapsackFitnessEvaluator evaluator = new KnapsackFitnessEvaluator(3, weights, values, 1.0);
        
        boolean[] chromosome = new boolean[]{false, false, false};
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 0.0 : "All excluded items should have fitness 0.0";
    }
    
    /**
     * Test 02: Knapsack - all items included within capacity
     * Tests: All items selected, all within capacity, value = sum of all values
     * Setup: items=[w:1,v:10], [w:2,v:20], [w:3,v:30] capacity=6.0 (100% of 6)
     * Evaluation: chromosome=[T,T,T] => weight=6, value=60, within capacity => fitness=60
     */
    public static void test_02() {
        double[] weights = {1.0, 2.0, 3.0};
        double[] values = {10.0, 20.0, 30.0};
        KnapsackFitnessEvaluator evaluator = new KnapsackFitnessEvaluator(3, weights, values, 1.0);
        
        boolean[] chromosome = new boolean[]{true, true, true};
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 60.0 : "All items within capacity should sum to fitness 60.0";
    }
    
    /**
     * Test 03: Knapsack - selective items within capacity
     * Tests: Partial items selected, exactly at capacity limit
     * Setup: items=[w:2,v:20], [w:3,v:30], [w:4,v:40] capacity=5.0 (100% of 9)
     * Evaluation: chromosome=[T,T,F] => weight=5, value=50, at capacity => fitness=50
     */
    public static void test_03() {
        double[] weights = {2.0, 3.0, 4.0};
        double[] values = {20.0, 30.0, 40.0};
        KnapsackFitnessEvaluator evaluator = new KnapsackFitnessEvaluator(3, weights, values, 1.0);
        
        boolean[] chromosome = new boolean[]{true, true, false};
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 50.0 : "Selected items [w:2,w:3] = w:5, v:50, at capacity => fitness=50";
    }
    
    /**
     * Test 04: Knapsack - items exceed capacity (fitness = 0 penalty)
     * Tests: Weight exceeds capacity, returns 0 as penalty
     * Setup: items=[w:3,v:30], [w:4,v:40] capacity=5.0 (5/7 of 7)
     * Evaluation: chromosome=[T,T] => weight=7, value=70, exceeds capacity => fitness=0
     */
    public static void test_04() {
        double[] weights = {3.0, 4.0};
        double[] values = {30.0, 40.0};
        KnapsackFitnessEvaluator evaluator = new KnapsackFitnessEvaluator(2, weights, values, 5/7);
        
        boolean[] chromosome = new boolean[]{true, true};
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 0.0 : "Items exceeding capacity should have fitness 0.0 (penalty)";
    }
    
    /**
     * Test 05: Knapsack - fractional capacity (50% of total weight)
     * Tests: Capacity is half of total weight, test selective inclusion
     * Setup: items=[w:2,v:10], [w:2,v:15], [w:2,v:25] capacity=3.0 (50% of 6)
     * Evaluation: chromosome=[F,T,T] => weight=4, value=40, exceeds capacity(3) => fitness=0
     * Evaluation: chromosome=[T,F,F] => weight=2, value=10, within capacity(3) => fitness=10
     */
    public static void test_05() {
        double[] weights = {2.0, 2.0, 2.0};
        double[] values = {10.0, 15.0, 25.0};
        KnapsackFitnessEvaluator evaluator = new KnapsackFitnessEvaluator(3, weights, values, 0.5);
        
        // Test case 5a: exceeds capacity
        boolean[] chromosomeExceed = new boolean[]{false, true, true};
        double fitnessExceed = evaluator.evaluate(chromosomeExceed);
        assert fitnessExceed == 0.0 : "Weight 4 exceeds capacity 3 => fitness=0";
        
        // Test case 5b: within capacity
        boolean[] chromosomeWithin = new boolean[]{true, false, false};
        double fitnessWithin = evaluator.evaluate(chromosomeWithin);
        assert fitnessWithin == 10.0 : "Weight 2 within capacity 3, value=10 => fitness=10";
    }
    
    /**
     * Test 06: Knapsack - edge cases and boundaries
     * Tests: Length mismatch, single item
     */
    public static void test_06() {
        double[] weights = {1.0, 2.0, 3.0};
        double[] values = {10.0, 20.0, 30.0};
        KnapsackFitnessEvaluator evaluator = new KnapsackFitnessEvaluator(3, weights, values, 1.0);
        
        // Edge case 1: Undersized chromosome (length 2 < expected 3)
        boolean undersizeCaught = false;
        try {
            boolean[] undersize = new boolean[2];
            evaluator.evaluate(undersize);
        } catch (IllegalArgumentException e) {
            undersizeCaught = true;
            assert e.getMessage().contains("length") : "Exception should mention length";
        }
        assert undersizeCaught : "Should throw exception for undersized chromosome";
        
        // Edge case 2: Oversized chromosome (length 4 > expected 3)
        boolean oversizeCaught = false;
        try {
            boolean[] oversize = new boolean[4];
            evaluator.evaluate(oversize);
        } catch (IllegalArgumentException e) {
            oversizeCaught = true;
            assert e.getMessage().contains("length") : "Exception should mention length";
        }
        assert oversizeCaught : "Should throw exception for oversized chromosome";
        
        // Edge case 3: Single item within capacity
        KnapsackFitnessEvaluator singleEval = new KnapsackFitnessEvaluator(1, new double[]{5.0}, new double[]{50.0}, 1.0);
        boolean[] singleChrom = new boolean[]{true};
        double fitnessSingle = singleEval.evaluate(singleChrom);
        assert fitnessSingle == 50.0 : "Single item included should have fitness 50.0";
    }
    
    /**
     * Test 07: Knapsack - constructor validation
     * Tests: Mismatched weights/values arrays, mismatched array length with chromosome length
     */
    public static void test_07() {
        // Constructor edge case 1: weights and values length mismatch
        boolean mismatchCaught = false;
        try {
            new KnapsackFitnessEvaluator(3, new double[]{1.0, 2.0}, new double[]{10.0, 20.0, 30.0}, 1.0);
        } catch (IllegalArgumentException e) {
            mismatchCaught = true;
            assert e.getMessage().contains("weights") && e.getMessage().contains("values") : "Exception should mention weights/values";
        }
        assert mismatchCaught : "Should throw exception for weights/values length mismatch";
        
        // Constructor edge case 2: array length mismatch with chromosome length
        boolean arrayLenMismatchCaught = false;
        try {
            new KnapsackFitnessEvaluator(5, new double[]{1.0, 2.0, 3.0}, new double[]{10.0, 20.0, 30.0}, 1.0);
        } catch (IllegalArgumentException e) {
            arrayLenMismatchCaught = true;
            assert e.getMessage().contains("length") : "Exception should mention length";
        }
        assert arrayLenMismatchCaught : "Should throw exception for array length != chromosome length";
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        test_06();
        test_07();
        System.out.println("All Knapsack fitness tests passed!");
    }
}
