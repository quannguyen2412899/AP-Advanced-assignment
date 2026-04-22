package models;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/fitnesses/*.java tests/models/TestPopulation.java
 * java -ea -cp bin models.TestPopulation
 */
public class TestPopulation {
    
    /**
     * Test 01: Basic construction and size
     * Tests: Population correctly stores size and provides access to individuals
     */
    public static void test_01() {
        Chromosome[] chromosomes = new Chromosome[5];
        for (int i = 0; i < 5; i++) {
            boolean[] genes = new boolean[10];
            genes[i] = true;
            chromosomes[i] = new Chromosome(genes, (double) i);
        }
        
        Population pop = new Population(chromosomes, 5);
        
        assert pop.size() == 5 : "Population size should be 5";
        
        // Verify each individual is accessible and has correct fitness
        for (int i = 0; i < 5; i++) {
            assert pop.getIndividual(i).getFitness() == (double) i : 
                "Individual " + i + " should have fitness " + i;
        }
    }
    
    /**
     * Test 02: Input array independence - modifying source array doesn't affect Population
     * Tests: Population defensive copying of input array
     */
    public static void test_02() {
        Chromosome[] chromosomes = new Chromosome[3];
        boolean[] genes0 = new boolean[10];
        genes0[0] = true;
        boolean[] genes1 = new boolean[10];
        genes1[1] = true;
        boolean[] genes2 = new boolean[10];
        genes2[2] = true;
        
        chromosomes[0] = new Chromosome(genes0, 10.0);
        chromosomes[1] = new Chromosome(genes1, 20.0);
        chromosomes[2] = new Chromosome(genes2, 30.0);
        
        Population pop = new Population(chromosomes, 3);
        
        // Modify the source array
        boolean[] newGenes = new boolean[10];
        Chromosome newChromosome = new Chromosome(newGenes, 99.0);
        chromosomes[0] = newChromosome;
        chromosomes[1] = null;
        chromosomes[2] = null;
        
        // Population should be unaffected
        assert pop.getIndividual(0).getFitness() == 10.0 : "Individual 0 fitness should remain 10.0";
        assert pop.getIndividual(1).getFitness() == 20.0 : "Individual 1 fitness should remain 20.0";
        assert pop.getIndividual(2).getFitness() == 30.0 : "Individual 2 fitness should remain 30.0";
    }
    
    /**
     * Test 03: Bounds checking for getIndividual
     * Tests: IndexOutOfBoundsException for invalid indices
     */
    public static void test_03() {
        Chromosome[] chromosomes = new Chromosome[3];
        for (int i = 0; i < 3; i++) {
            chromosomes[i] = new Chromosome(new boolean[10], (double) i);
        }
        
        Population pop = new Population(chromosomes, 3);
        
        // Valid boundary indices
        assert pop.getIndividual(0).getFitness() == 0.0 : "Index 0 should be accessible";
        assert pop.getIndividual(2).getFitness() == 2.0 : "Index 2 should be accessible";
        
        // Invalid indices
        try {
            pop.getIndividual(-1);
            assert false : "Should throw IndexOutOfBoundsException for negative index";
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        
        try {
            pop.getIndividual(3);
            assert false : "Should throw IndexOutOfBoundsException for index >= size";
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
    
    /**
     * Test 04: Array size validation
     * Tests: Constructor validation that input array size >= required size
     */
    public static void test_04() {
        Chromosome[] chromosomes = new Chromosome[2];
        for (int i = 0; i < 2; i++) {
            chromosomes[i] = new Chromosome(new boolean[10], (double) i);
        }
        
        try {
            Population pop = new Population(chromosomes, 5); // Require size 5 but only have 2
            assert false : "Should throw IllegalArgumentException when array size < required size";
        } catch (IllegalArgumentException e) {
            assert e.getMessage().contains("array size") : "Error message should mention array size";
        }
    }
    
    /**
     * Test 05: Exact chromosome preservation
     * Tests: Each chromosome in the population is exactly what was initialized
     */
    public static void test_05() {
        Chromosome[] chromosomes = new Chromosome[6];
        for (int i = 0; i < 6; i++) {
            boolean[] genes = new boolean[10];
            // Set different patterns for each chromosome
            for (int j = 0; j <= i; j++) {
                genes[j] = true;
            }
            chromosomes[i] = new Chromosome(genes, (double) (i * 5));
        }
        
        Population pop = new Population(chromosomes, 6);
        
        // Verify each chromosome preserved exactly
        for (int i = 0; i < 6; i++) {
            Chromosome c = pop.getIndividual(i);
            assert c.getFitness() == (double) (i * 5) : "Chromosome " + i + " fitness mismatch";
            assert c.length() == 10 : "Chromosome " + i + " length mismatch";
            
            // Verify bit pattern
            for (int bit = 0; bit <= i; bit++) {
                assert c.getBit(bit) : "Chromosome " + i + " bit " + bit + " should be set";
            }
            for (int bit = i + 1; bit < 10; bit++) {
                assert !c.getBit(bit) : "Chromosome " + i + " bit " + bit + " should not be set";
            }
        }
    }
    
    /**
     * Test 06: Large population stress test - 100 random chromosomes
     * Tests: Population correctly stores and preserves many chromosomes
     */
    public static void test_06() {
        int populationSize = 100;
        Chromosome[] chromosomes = new Chromosome[populationSize];
        
        for (int i = 0; i < populationSize; i++) {
            boolean[] genes = new boolean[50];
            // Create unique pattern for each
            genes[i % 50] = true;
            chromosomes[i] = new Chromosome(genes, (double) i * 1.5);
        }
        
        Population pop = new Population(chromosomes, populationSize);
        
        // Verify all chromosomes preserved
        assert pop.size() == populationSize : "Population size should be " + populationSize;
        
        for (int i = 0; i < populationSize; i++) {
            assert pop.getIndividual(i).getFitness() == (double) i * 1.5 : 
                "Chromosome " + i + " fitness mismatch";
            assert pop.getIndividual(i).getBit(i % 50) : 
                "Chromosome " + i + " bit pattern mismatch";
        }
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        test_06();
        System.out.println("All tests passed!");
    }
}
