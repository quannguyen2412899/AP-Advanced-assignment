package fitnesses;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/fitnesses/*.java tests/fitnesses/TestOneMax.java
 * java -ea -cp bin fitnesses.TestOneMax
 */
public class TestOneMax {
    /**
     * Test 01: OneMax - all false bits (fitness = 0)
     * Tests: Count of true bits with all false input
     */
    public static void test_01() {
        OneMaxFitnessEvaluator evaluator = new OneMaxFitnessEvaluator(10);
        boolean[] chromosome = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 0.0 : "All false bits should have fitness 0.0";
    }
    
    /**
     * Test 02: OneMax - all true bits (fitness = length)
     * Tests: Count of true bits with all true input
     */
    public static void test_02() {
        OneMaxFitnessEvaluator evaluator = new OneMaxFitnessEvaluator(10);
        boolean[] chromosome = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 10.0 : "All true bits should have fitness 10.0";
    }
    
    /**
     * Test 03: OneMax - mixed bits (fitness = count of true)
     * Tests: Correct count with mixed true/false pattern
     */
    public static void test_03() {
        OneMaxFitnessEvaluator evaluator = new OneMaxFitnessEvaluator(10);
        boolean[] chromosome = new boolean[]{true, false, true, false, true, false, true, false, true, false};
        
        double fitness = evaluator.evaluate(chromosome);
        
        assert fitness == 5.0 : "5 true bits should have fitness 5.0";
    }
    
    /**
     * Test 04: OneMax - real edge cases and boundaries
     * Tests: Null input, length mismatches (undersize, oversize), exact match boundary
     */
    public static void test_04() {
        OneMaxFitnessEvaluator evaluator = new OneMaxFitnessEvaluator(10);
        
        // Edge case 1: Null chromosome
        boolean nullCaught = false;
        try {
            evaluator.evaluate(null);
        } catch (IllegalArgumentException e) {
            nullCaught = true;
            assert e.getMessage().contains("null") : "Exception should mention null";
        }
        assert nullCaught : "Should throw exception for null chromosome";
        
        // Edge case 2: Undersized chromosome (length 9 < expected 10)
        boolean undersizeCaught = false;
        try {
            boolean[] undersize = new boolean[9];
            evaluator.evaluate(undersize);
        } catch (IllegalArgumentException e) {
            undersizeCaught = true;
            assert e.getMessage().contains("length") : "Exception should mention length";
        }
        assert undersizeCaught : "Should throw exception for undersized chromosome";
        
        // Edge case 3: Oversized chromosome (length 11 > expected 10)
        boolean oversizeCaught = false;
        try {
            boolean[] oversize = new boolean[11];
            evaluator.evaluate(oversize);
        } catch (IllegalArgumentException e) {
            oversizeCaught = true;
            assert e.getMessage().contains("length") : "Exception should mention length";
        }
        assert oversizeCaught : "Should throw exception for oversized chromosome";
        
        // Edge case 4: Empty chromosome (length 0 < expected 10)
        boolean emptyCaught = false;
        try {
            boolean[] empty = new boolean[0];
            evaluator.evaluate(empty);
        } catch (IllegalArgumentException e) {
            emptyCaught = true;
            assert e.getMessage().contains("length") : "Exception should mention length";
        }
        assert emptyCaught : "Should throw exception for empty chromosome";
        
        // Edge case 5: Exact length match with all false (valid, fitness 0)
        OneMaxFitnessEvaluator exactEval = new OneMaxFitnessEvaluator(5);
        boolean[] exactChrom = new boolean[5]; // all false by default
        double fitnessExact = exactEval.evaluate(exactChrom);
        assert fitnessExact == 0.0 : "Exact length match with all false should have fitness 0.0";
    }
    
    /**
     * Test 05: OneMax - various evaluators with different lengths
     * Tests: Correct fitness calculation with different configured lengths
     */
    public static void test_05() {
        // Length 5
        OneMaxFitnessEvaluator evaluator5 = new OneMaxFitnessEvaluator(5);
        boolean[] chromosome5 = new boolean[]{true, true, false, true, false};
        double fitness5 = evaluator5.evaluate(chromosome5);
        assert fitness5 == 3.0 : "3 true bits out of 5 should have fitness 3.0";
        
        // Length 20
        OneMaxFitnessEvaluator evaluator20 = new OneMaxFitnessEvaluator(20);
        boolean[] chromosome20 = new boolean[20];
        for (int i = 0; i < 20; i++) {
            chromosome20[i] = (i % 2 == 0); // 10 true bits
        }
        double fitness20 = evaluator20.evaluate(chromosome20);
        assert fitness20 == 10.0 : "10 true bits out of 20 should have fitness 10.0";
        
        // Length 100
        OneMaxFitnessEvaluator evaluator100 = new OneMaxFitnessEvaluator(100);
        boolean[] chromosome100 = new boolean[100];
        for (int i = 0; i < 100; i++) {
            chromosome100[i] = (i < 42); // 42 true bits
        }
        double fitness100 = evaluator100.evaluate(chromosome100);
        assert fitness100 == 42.0 : "42 true bits out of 100 should have fitness 42.0";
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        System.out.println("All OneMax fitness tests passed!");
    }
}
