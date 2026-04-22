package models;

import java.util.Random;

/**
 * javac -d bin src/models/*.java src/utils/*.java tests/models/TestChromosome.java
 * java -ea -cp bin models.TestChromosome    
 */
public class TestChromosome {

    /**
     * Test 01: Verify Chromosome correctly stores genes and fitness from constructor
     * Tests: Basic construction, getter methods return correct values
     */
    public static void test_01() {
        boolean[] genes = new boolean[10];
        genes[0] = true;
        genes[5] = true;
        genes[9] = true;
        
        Chromosome c = new Chromosome(genes, 42.5);
        
        assert c.length() == 10 : "Length should be 10";
        assert c.getFitness() == 42.5 : "Fitness should be 42.5";
        assert c.getBit(0) : "Bit 0 should be set";
        assert c.getBit(5) : "Bit 5 should be set";
        assert c.getBit(9) : "Bit 9 should be set";
        assert !c.getBit(3) : "Bit 3 should not be set";
    }
    
    /**
     * Test 02: Verify defensive copying - modifying original array does not affect Chromosome
     * Tests: Chromosome independence from external modifications
     */
    public static void test_02() {
        boolean[] genes = new boolean[10];
        genes[2] = true;
        genes[7] = true;
        
        Chromosome c = new Chromosome(genes, 15.0);
        
        // Modify the original array
        genes[2] = false;
        genes[4] = true;
        
        // Chromosome should be unaffected
        assert c.getBit(2) : "Chromosome bit 2 should still be set";
        assert !c.getBit(4) : "Chromosome bit 4 should not be set";
    }
    
    /**
     * Test 03: Verify copy from boolean[] with different patterns
     * Tests: Chromosome stores exact bit patterns from boolean array
     */
    public static void test_03() {
        boolean[] genes = new boolean[10];
        genes[1] = true;
        genes[6] = true;
        
        Chromosome c = new Chromosome(genes, 25.5);
        
        // Verify bits match
        assert c.getFitness() == 25.5 : "Fitness should match";
        assert c.length() == 10 : "Length should match";
        assert c.getBit(1) : "Bit 1 should be set";
        assert c.getBit(6) : "Bit 6 should be set";
        
        // Verify independence - modify returned array
        boolean[] retrieved = c.getBitString();
        retrieved[1] = false;
        assert c.getBit(1) : "Original bit 1 should remain set after external modification";
    }
    
    /**
     * Test 04: Verify bounds checking for getBit
     * Tests: IndexOutOfBoundsException for invalid indices
     */
    public static void test_04() {
        boolean[] genes = new boolean[10];
        Chromosome c = new Chromosome(genes, 5.0);
        
        // Valid boundary indices
        assert !c.getBit(0) : "Bit 0 should be accessible";
        assert !c.getBit(9) : "Bit 9 should be accessible";
        
        // Invalid indices should throw exception
        try {
            c.getBit(-1);
            assert false : "Should throw IndexOutOfBoundsException for negative index";
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        
        try {
            c.getBit(10);
            assert false : "Should throw IndexOutOfBoundsException for index >= length";
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
    
    /**
     * Test 05: Verify IllegalArgumentException when chromosome array is null
     * Tests: Constructor validation of null input
     */
    public static void test_05() {
        try {
            Chromosome c = new Chromosome(null, 0.0);
            assert false : "Should throw IllegalArgumentException when chromosome is null";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("chromosome is null") : "Error message should mention null";
        }
    }
    
    /**
     * Test 06: Verify getBitString() returns independent defensive copy
     * Tests: Modifying returned array does not affect internal state
     */
    public static void test_06() {
        boolean[] genes = new boolean[10];
        genes[3] = true;
        genes[7] = true;
        
        Chromosome c = new Chromosome(genes, 50.0);
        
        // Get the bit string and modify it
        boolean[] retrieved = c.getBitString();
        retrieved[3] = false;
        retrieved[5] = true;
        
        // Chromosome internal state should be unaffected
        assert c.getBit(3) : "Internal bit 3 should still be set after external modification";
        assert !c.getBit(5) : "Internal bit 5 should still be unset after external modification";
        assert c.getBit(7) : "Internal bit 7 should remain set";
    }
    
    /**
     * Test 07: Verify Chromosome preserves ALL bits correctly
     * Tests: Complete gene preservation, not just sampled bits
     */
    public static void test_07() {
        boolean[] genes = new boolean[20];
        // Set alternating bits: 0, 2, 4, 6, 8, 10, 12, 14, 16, 18
        for (int i = 0; i < 20; i += 2) {
            genes[i] = true;
        }
        
        Chromosome c = new Chromosome(genes, 99.9);
        
        // Verify every bit matches exactly
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                assert c.getBit(i) : "Bit " + i + " should be set";
            } else {
                assert !c.getBit(i) : "Bit " + i + " should not be set";
            }
        }
        
        assert c.getFitness() == 99.9 : "Fitness should match exactly";
    }
    
    /**
     * Test 08: Random stress test - 200 iterations of random 100-bit chromosomes
     * Tests: Data fidelity - verify exact bit preservation across many random initializations
     */
    public static void test_08() {
        Random rand = new Random(12345);
        int iterations = 200;
        int chromosomeLength = 100;
        
        for (int iter = 0; iter < iterations; iter++) {
            // Generate random boolean array
            boolean[] genes = new boolean[chromosomeLength];
            for (int i = 0; i < chromosomeLength; i++) {
                genes[i] = rand.nextBoolean();
            }
            
            double fitness = rand.nextDouble() * 1000.0;
            Chromosome c = new Chromosome(genes, fitness);
            
            // Verify exact preservation: every bit matches
            for (int bit = 0; bit < chromosomeLength; bit++) {
                assert genes[bit] == c.getBit(bit) : 
                    "Iteration " + iter + ": bit " + bit + " mismatch via getBit()";
            }
            
            // Verify getBitString() returns exact same bits
            boolean[] retrievedBits = c.getBitString();
            for (int bit = 0; bit < chromosomeLength; bit++) {
                assert genes[bit] == retrievedBits[bit] : 
                    "Iteration " + iter + ": bit " + bit + " mismatch via getBitString()";
            }
            
            assert c.length() == chromosomeLength : "Length mismatch in iteration " + iter;
            assert c.getFitness() == fitness : "Fitness mismatch in iteration " + iter;
        }
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        test_06();
        test_07();
        test_08();
        System.out.println("All tests passed!");
    }
}