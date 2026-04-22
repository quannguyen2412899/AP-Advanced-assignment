package strategies;

import utils.RandomUtil;
import utils.FakeRandomUtil;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/strategies/*.java tests/utils/FakeRandomUtil.java tests/strategies/TestMutation.java
 * java -ea -cp bin strategies.TestMutation
 */
public class TestMutation {
    /**
     * Test 01: BitFlipMutation - no mutation happens (mutation rate 0.0)
     * Tests: When mutation rate is 0.0, chromosome unchanged regardless of random values
     */
    public static void test_01() {
        boolean[] chromosome = new boolean[]{true, false, true, false, true, false, true, false, true, false};
        
        BitFlipMutation mutation = new BitFlipMutation(0.0); // Rate 0.0 = no mutations
        RandomUtil random = new RandomUtil(42); // Use real RandomUtil
        
        boolean[] mutated = mutation.mutate(chromosome, random);
        
        assert mutated.length == 10 : "Mutated chromosome should have same length";
        
        // Verify no bits were flipped
        for (int i = 0; i < 10; i++) {
            assert mutated[i] == chromosome[i] : "Bit " + i + " should remain unchanged";
        }
    }
    
    /**
     * Test 02: BitFlipMutation - all bits mutate (mutation rate 1.0)
     * Tests: When mutation rate is 1.0, all bits are always flipped
     */
    public static void test_02() {
        boolean[] chromosome = new boolean[]{true, false, true, false, true, false, true, false, true, false};
        
        BitFlipMutation mutation = new BitFlipMutation(1.0); // Rate 1.0 = all mutations
        RandomUtil random = new RandomUtil(42); // Use real RandomUtil
        
        boolean[] mutated = mutation.mutate(chromosome, random);
        
        assert mutated.length == 10 : "Mutated chromosome should have same length";
        
        // Verify all bits were flipped
        for (int i = 0; i < 10; i++) {
            assert mutated[i] == !chromosome[i] : "Bit " + i + " should be flipped";
        }
    }
    
    /**
     * Test 03: BitFlipMutation - selective mutations (determined bernoulli sequence)
     * Tests: Only specific bits mutate based on bernoulli sequence
     */
    public static void test_03() {
        boolean[] chromosome = new boolean[]{true, true, true, true, true, false, false, false, false, false};
        
        BitFlipMutation mutation = new BitFlipMutation(0.5);
        
        // FakeRandomUtil: bernoulli sequence with selective mutations
        // Mutate bits at indices: 1, 3, 5, 7, 9
        boolean[] bernoulliSeq = new boolean[]{false, true, false, true, false, true, false, true, false, true};
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{}, bernoulliSeq);
        
        boolean[] mutated = mutation.mutate(chromosome, fakeRandom);
        
        assert mutated.length == 10 : "Mutated chromosome should have same length";
        
        // Verify correct bits were flipped
        assert mutated[0] == true : "Bit 0 should not mutate";
        assert mutated[1] == false : "Bit 1 should be flipped";
        assert mutated[2] == true : "Bit 2 should not mutate";
        assert mutated[3] == false : "Bit 3 should be flipped";
        assert mutated[4] == true : "Bit 4 should not mutate";
        assert mutated[5] == true : "Bit 5 should be flipped";
        assert mutated[6] == false : "Bit 6 should not mutate";
        assert mutated[7] == true : "Bit 7 should be flipped";
        assert mutated[8] == false : "Bit 8 should not mutate";
        assert mutated[9] == true : "Bit 9 should be flipped";
    }
    
    /**
     * Test 04: BitFlipMutation - edge cases
     * Tests: Null input, empty chromosome, single-bit chromosome, large chromosome, mutation rate boundaries
     */
    public static void test_04() {
        BitFlipMutation mutation = new BitFlipMutation(0.5);
        
        // Edge case 1: Null chromosome
        boolean nullCaught = false;
        try {
            mutation.mutate(null, new FakeRandomUtil(new int[]{}, new boolean[]{false}));
        } catch (IllegalArgumentException e) {
            nullCaught = true;
            assert e.getMessage().contains("null") : "Exception should mention null";
        }
        assert nullCaught : "Should throw exception for null chromosome";
        
        // Edge case 2: Empty chromosome
        boolean[] emptyChromosome = new boolean[0];
        FakeRandomUtil fakeRandom2 = new FakeRandomUtil(new int[]{}, new boolean[]{});
        boolean[] mutatedEmpty = mutation.mutate(emptyChromosome, fakeRandom2);
        assert mutatedEmpty.length == 0 : "Empty chromosome should remain empty";
        
        // Edge case 3: Single-bit chromosome
        boolean[] singleBit = new boolean[]{true};
        boolean[] bernoulliSingle = new boolean[]{true};
        FakeRandomUtil fakeRandom3 = new FakeRandomUtil(new int[]{}, bernoulliSingle);
        boolean[] mutatedSingle = mutation.mutate(singleBit, fakeRandom3);
        assert mutatedSingle.length == 1 : "Single-bit chromosome should have length 1";
        assert mutatedSingle[0] == false : "Single bit should be flipped";
        
        // Edge case 4: Large chromosome (100 bits)
        boolean[] largeChromosome = new boolean[100];
        boolean[] largeBernoulli = new boolean[100];
        for (int i = 0; i < 100; i++) {
            largeChromosome[i] = (i % 2 == 0);  // alternating pattern
            largeBernoulli[i] = (i % 3 == 0);   // mutate every 3rd bit
        }
        FakeRandomUtil fakeRandom4 = new FakeRandomUtil(new int[]{}, largeBernoulli);
        boolean[] mutatedLarge = mutation.mutate(largeChromosome, fakeRandom4);
        assert mutatedLarge.length == 100 : "Large chromosome should have correct length";
        
        // Verify correct bits were flipped in large chromosome
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                assert mutatedLarge[i] == !largeChromosome[i] : "Bit " + i + " should be flipped";
            } else {
                assert mutatedLarge[i] == largeChromosome[i] : "Bit " + i + " should not be flipped";
            }
        }
    }
    
    /**
     * Test 05: BitFlipMutation - defensive copy and independence
     * Tests: Original chromosome unchanged, mutated result is independent copy
     */
    public static void test_05() {
        boolean[] chromosome = new boolean[]{true, false, true, false, true};
        boolean[] originalCopy = new boolean[]{true, false, true, false, true};
        
        BitFlipMutation mutation = new BitFlipMutation(1.0);
        RandomUtil random = new RandomUtil(42);
        
        boolean[] mutated = mutation.mutate(chromosome, random);
        
        // Verify original chromosome unchanged
        for (int i = 0; i < 5; i++) {
            assert chromosome[i] == originalCopy[i] : "Original chromosome at " + i + " should not be modified";
        }
        
        // Verify mutated is a different array
        assert mutated != chromosome : "Mutated should be a different array object";
        
        // Modify mutated array and verify original is unaffected
        mutated[0] = true;
        assert chromosome[0] == originalCopy[0] : "Original chromosome should be independent from mutated result";
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        System.out.println("All mutation tests passed!");
    }
}
