package strategies;

import utils.FakeRandomUtil;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/strategies/*.java tests/utils/FakeRandomUtil.java tests/strategies/TestCrossover.java
 * java -ea -cp bin strategies.TestCrossover
 */
public class TestCrossover {
    /**
     * Test 01: OnePointCrossover - no crossover happens (crossover probability false)
     * Tests: When nextBernoulli returns false, offspring are identical to parents
     */
    public static void test_01() {
        boolean[] parent1 = new boolean[]{true, true, true, true, true, false, false, false, false, false};
        boolean[] parent2 = new boolean[]{false, false, false, false, false, true, true, true, true, true};
        
        OnePointCrossover crossover = new OnePointCrossover(0.5);
        
        // FakeRandomUtil: bernoulli sequence with false (no crossover), int sequence empty since nextInt not called
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{}, new boolean[]{false});
        
        boolean[][] offspring = crossover.crossover(parent1, parent2, fakeRandom);
        
        assert offspring.length == 2 : "Should return 2 offspring";
        assert offspring[0].length == 10 : "Offspring 1 should have length 10";
        assert offspring[1].length == 10 : "Offspring 2 should have length 10";
        
        // Verify offspring are identical to parents when no crossover happens
        for (int i = 0; i < 10; i++) {
            assert offspring[0][i] == parent1[i] : "Offspring 1 should be identical to parent 1 at index " + i;
            assert offspring[1][i] == parent2[i] : "Offspring 2 should be identical to parent 2 at index " + i;
        }
    }

    public static void test_02() {
        boolean[] parent1 = new boolean[]{true, true, true, true, true, false, false, false, false, false};
        boolean[] parent2 = new boolean[]{false, false, false, false, false, true, true, true, true, true};
        
        OnePointCrossover crossover = new OnePointCrossover(1.0); // Always crossover
        
        // FakeRandomUtil: bernoulli true (crossover happens), crossover at index 5
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{5}, new boolean[]{true});
        
        boolean[][] offspring = crossover.crossover(parent1, parent2, fakeRandom);
        
        assert offspring.length == 2 : "Should return 2 offspring";
        assert offspring[0].length == 10 : "Offspring 1 should have length 10";
        assert offspring[1].length == 10 : "Offspring 2 should have length 10";
        
        // Offspring 1: parent1[0:5] + parent2[5:10]
        for (int i = 0; i < 5; i++) {
            assert offspring[0][i] == true : "Offspring 1 should have parent1 bits [0:5]";
        }
        for (int i = 5; i < 10; i++) {
            assert offspring[0][i] == true : "Offspring 1 should have parent2 bits [5:10]";
        }
        
        // Offspring 2: parent2[0:5] + parent1[5:10]
        for (int i = 0; i < 5; i++) {
            assert offspring[1][i] == false : "Offspring 2 should have parent2 bits [0:5]";
        }
        for (int i = 5; i < 10; i++) {
            assert offspring[1][i] == false : "Offspring 2 should have parent1 bits [5:10]";
        }
    }
    
    /**
     * Test 03: OnePointCrossover - crossover at different indices
     * Tests: Verify correct bit swapping at different crossover points
     */
    public static void test_03() {
        boolean[] parent1 = new boolean[]{true, true, true, true, true, true, true, true, true, true};
        boolean[] parent2 = new boolean[]{false, false, false, false, false, false, false, false, false, false};
        
        OnePointCrossover crossover = new OnePointCrossover(1.0); // Always crossover
        
        // Test crossover at index 3
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{3}, new boolean[]{true});
        boolean[][] offspring = crossover.crossover(parent1, parent2, fakeRandom);
        
        // Offspring 1: parent1[0:3] (true) + parent2[3:10] (false)
        for (int i = 0; i < 3; i++) {
            assert offspring[0][i] == true : "Offspring 1 [0:3] should be from parent1";
        }
        for (int i = 3; i < 10; i++) {
            assert offspring[0][i] == false : "Offspring 1 [3:10] should be from parent2";
        }
        
        // Offspring 2: parent2[0:3] (false) + parent1[3:10] (true)
        for (int i = 0; i < 3; i++) {
            assert offspring[1][i] == false : "Offspring 2 [0:3] should be from parent2";
        }
        for (int i = 3; i < 10; i++) {
            assert offspring[1][i] == true : "Offspring 2 [3:10] should be from parent1";
        }
    }
    
    /**
     * Test 04: OnePointCrossover - edge cases
     * Tests: Crossover at boundaries (1 and length-1), mixed parent patterns
     */
    public static void test_04() {
        // Edge case 1: Crossover at index 1 (minimal swap)
        boolean[] parent1 = new boolean[]{true, true, true, true, true, false, false, false, false, false};
        boolean[] parent2 = new boolean[]{false, false, false, false, false, true, true, true, true, true};
        
        OnePointCrossover crossover = new OnePointCrossover(1.0);
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{1}, new boolean[]{true});
        boolean[][] offspring = crossover.crossover(parent1, parent2, fakeRandom);
        
        assert offspring[0][0] == true : "Offspring 1[0] from parent1";
        for (int i = 1; i < 10; i++) {
            assert offspring[0][i] == parent2[i] : "Offspring 1[1:10] from parent2";
        }
        
        // Edge case 2: Crossover at index 9 (swap only last bit)
        FakeRandomUtil fakeRandom2 = new FakeRandomUtil(new int[]{9}, new boolean[]{true});
        boolean[][] offspring2 = crossover.crossover(parent1, parent2, fakeRandom2);
        
        for (int i = 0; i < 9; i++) {
            assert offspring2[0][i] == parent1[i] : "Offspring 1[0:9] from parent1";
        }
        assert offspring2[0][9] == parent2[9] : "Offspring 1[9] from parent2";
        
        // Edge case 3: Large chromosomes with complex pattern
        boolean[] parent3 = new boolean[50];
        boolean[] parent4 = new boolean[50];
        for (int i = 0; i < 50; i++) {
            parent3[i] = (i % 2 == 0);  // Alternating true/false
            parent4[i] = (i % 2 == 1);  // Alternating false/true (opposite)
        }
        
        FakeRandomUtil fakeRandom3 = new FakeRandomUtil(new int[]{25}, new boolean[]{true});
        boolean[][] offspring3 = crossover.crossover(parent3, parent4, fakeRandom3);
        
        // Verify first half from parent3, second half from parent4
        for (int i = 0; i < 25; i++) {
            assert offspring3[0][i] == parent3[i] : "Offspring 1[0:25] from parent3";
        }
        for (int i = 25; i < 50; i++) {
            assert offspring3[0][i] == parent4[i] : "Offspring 1[25:50] from parent4";
        }
    }
    
    /**
     * Test 05: OnePointCrossover - with crossover probability control
     * Tests: Crossover happens based on probability, parents unchanged when no crossover
     */
    public static void test_05() {
        boolean[] parent1 = new boolean[]{true, false, true, false, true, false, true, false, true, false};
        boolean[] parent2 = new boolean[]{false, true, false, true, false, true, false, true, false, true};
        
        // Test with low crossover probability (crossover doesn't happen)
        OnePointCrossover crossover_low = new OnePointCrossover(0.0);
        FakeRandomUtil fakeRandom_low = new FakeRandomUtil(new int[]{5}, new boolean[]{false});
        boolean[][] offspring_no = crossover_low.crossover(parent1, parent2, fakeRandom_low);
        
        for (int i = 0; i < 10; i++) {
            assert offspring_no[0][i] == parent1[i] : "No crossover: offspring 1 identical to parent1";
            assert offspring_no[1][i] == parent2[i] : "No crossover: offspring 2 identical to parent2";
        }
        
        // Test with high crossover probability (crossover happens)
        OnePointCrossover crossover_high = new OnePointCrossover(1.0);
        FakeRandomUtil fakeRandom_high = new FakeRandomUtil(new int[]{5}, new boolean[]{true});
        boolean[][] offspring_yes = crossover_high.crossover(parent1, parent2, fakeRandom_high);
        
        // Verify crossover actually happened (bits after index 5 should be swapped)
        boolean swapped = false;
        for (int i = 5; i < 10; i++) {
            if (offspring_yes[0][i] == parent2[i] && offspring_yes[1][i] == parent1[i]) {
                swapped = true;
                break;
            }
        }
        assert swapped : "Crossover should have swapped bits from index 5 onwards";
    }
    
    /**
     * Test 06: OnePointCrossover - input validation and edge cases
     * Tests: Null inputs, empty arrays, single-bit arrays, mismatched lengths
     */
    public static void test_06() {
        boolean[] parent1 = new boolean[]{true, false, true, false};
        boolean[] parent2 = new boolean[]{false, true, false, true};
        OnePointCrossover crossover = new OnePointCrossover(1.0);
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{2}, new boolean[]{true});
        
        // Test null parent1
        boolean nullParent1Caught = false;
        try {
            crossover.crossover(null, parent2, fakeRandom);
        } catch (IllegalArgumentException e) {
            nullParent1Caught = true;
            assert e.getMessage().contains("parent1") : "Exception should mention parent1";
        }
        assert nullParent1Caught : "Should throw exception for null parent1";
        
        // Test null parent2
        boolean nullParent2Caught = false;
        try {
            crossover.crossover(parent1, null, fakeRandom);
        } catch (IllegalArgumentException e) {
            nullParent2Caught = true;
            assert e.getMessage().contains("parent2") : "Exception should mention parent2";
        }
        assert nullParent2Caught : "Should throw exception for null parent2";
        
        // Test empty parent arrays
        boolean emptyArrayCaught = false;
        try {
            crossover.crossover(new boolean[0], new boolean[0], fakeRandom);
        } catch (IllegalArgumentException e) {
            emptyArrayCaught = true;
            assert e.getMessage().contains("length 0") : "Exception should mention length 0";
        }
        assert emptyArrayCaught : "Should throw exception for empty arrays";
        
        // Test single-bit chromosome
        boolean singleBitCaught = false;
        try {
            crossover.crossover(new boolean[]{true}, new boolean[]{false}, fakeRandom);
        } catch (IllegalArgumentException e) {
            singleBitCaught = true;
            assert e.getMessage().contains("length must be > 1") : "Exception should mention length > 1 requirement";
        }
        assert singleBitCaught : "Should throw exception for single-bit chromosomes";
        
        // Test mismatched parent lengths
        boolean mismatchCaught = false;
        try {
            crossover.crossover(new boolean[]{true, false}, new boolean[]{true, false, true}, fakeRandom);
        } catch (IllegalArgumentException e) {
            mismatchCaught = true;
            assert e.getMessage().contains("same length") : "Exception should mention same length requirement";
        }
        assert mismatchCaught : "Should throw exception for mismatched parent lengths";
    }
    
    /**
     * Test 07: OnePointCrossover - two-bit chromosomes (minimum valid case)
     * Tests: Verify crossover works with minimal chromosome size (2 bits)
     */
    public static void test_07() {
        boolean[] parent1 = new boolean[]{true, true};
        boolean[] parent2 = new boolean[]{false, false};
        
        OnePointCrossover crossover = new OnePointCrossover(1.0);
        
        // Only valid crossover point for length 2 is index 1
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{1}, new boolean[]{true});
        boolean[][] offspring = crossover.crossover(parent1, parent2, fakeRandom);
        
        assert offspring.length == 2 : "Should return 2 offspring";
        assert offspring[0].length == 2 : "Offspring should have length 2";
        assert offspring[1].length == 2 : "Offspring should have length 2";
        
        // Offspring 1: parent1[0] + parent2[1]
        assert offspring[0][0] == true : "Offspring 1[0] from parent1";
        assert offspring[0][1] == false : "Offspring 1[1] from parent2";
        
        // Offspring 2: parent2[0] + parent1[1]
        assert offspring[1][0] == false : "Offspring 2[0] from parent2";
        assert offspring[1][1] == true : "Offspring 2[1] from parent1";
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        test_06();
        test_07();
        System.out.println("All crossover tests passed!");
    }
}
