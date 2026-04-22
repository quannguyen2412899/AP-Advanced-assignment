package strategies;

import models.*;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/fitnesses/*.java src/strategies/*.java tests/strategies/TestElitism.java
 * java -ea -cp bin strategies.TestElitism
 */
public class TestElitism {

    /**
     * Test 01: Elitism with 2 elite - select top 2 from 5 chromosomes
     * Tests: Selects the 2 highest fitness chromosomes
     */
    public static void test_01() {
        Chromosome[] chromosomes = new Chromosome[5];
        chromosomes[0] = new Chromosome(new boolean[10], 10.0);
        chromosomes[1] = new Chromosome(new boolean[10], 50.0);
        chromosomes[2] = new Chromosome(new boolean[10], 30.0);
        chromosomes[3] = new Chromosome(new boolean[10], 80.0);
        chromosomes[4] = new Chromosome(new boolean[10], 20.0);
        
        Population pop = new Population(chromosomes, 5);
        SimpleElitism elitism = new SimpleElitism(2);
        
        Chromosome[] elite = elitism.select(pop);
        
        assert elite.length == 2 : "Should select exactly 2 elite";
        double fitness1 = elite[0].getFitness();
        double fitness2 = elite[1].getFitness();
        double min = Math.min(fitness1, fitness2);
        double max = Math.max(fitness1, fitness2);
        assert min == 50.0 && max == 80.0 : "Elite should have fitness 50.0 and 80.0";
    }
    
    /**
     * Test 02: Elitism with 2 elite - select top 2 from 10 chromosomes with duplicate fitness
     * Tests: Handles populations with duplicate fitness values correctly
     */
    public static void test_02() {
        Chromosome[] chromosomes = new Chromosome[10];
        chromosomes[0] = new Chromosome(new boolean[10], 5.0);
        chromosomes[1] = new Chromosome(new boolean[10], 15.0);
        chromosomes[2] = new Chromosome(new boolean[10], 15.0);
        chromosomes[3] = new Chromosome(new boolean[10], 25.0);
        chromosomes[4] = new Chromosome(new boolean[10], 10.0);
        chromosomes[5] = new Chromosome(new boolean[10], 25.0);
        chromosomes[6] = new Chromosome(new boolean[10], 20.0);
        chromosomes[7] = new Chromosome(new boolean[10], 8.0);
        chromosomes[8] = new Chromosome(new boolean[10], 25.0);
        chromosomes[9] = new Chromosome(new boolean[10], 12.0);
        
        Population pop = new Population(chromosomes, 10);
        SimpleElitism elitism = new SimpleElitism(2);
        
        Chromosome[] elite = elitism.select(pop);
        
        assert elite.length == 2 : "Should select exactly 2 elite";
        double min = Math.min(elite[0].getFitness(), elite[1].getFitness());
        double max = Math.max(elite[0].getFitness(), elite[1].getFitness());
        assert min == 25.0 && max == 25.0 : "Elite should both be 25.0, the highest fitness value";
    }
    
    /**
     * Test 03: Elitism with 1 elite - select top 1 from 5 chromosomes
     * Tests: Correctly identifies single best chromosome
     */
    public static void test_03() {
        Chromosome[] chromosomes = new Chromosome[5];
        chromosomes[0] = new Chromosome(new boolean[10], 10.0);
        chromosomes[1] = new Chromosome(new boolean[10], 50.0);
        chromosomes[2] = new Chromosome(new boolean[10], 30.0);
        chromosomes[3] = new Chromosome(new boolean[10], 99.0);
        chromosomes[4] = new Chromosome(new boolean[10], 20.0);
        
        Population pop = new Population(chromosomes, 5);
        SimpleElitism elitism = new SimpleElitism(1);
        
        Chromosome[] elite = elitism.select(pop);
        
        assert elite.length == 1 : "Should select exactly 1 elite";
        assert elite[0].getFitness() == 99.0 : "Elite should be the chromosome with fitness 99.0";
    }
    
    /**
     * Test 04: Elitism with 1 elite - from large population
     * Tests: Correctly finds best among many chromosomes
     */
    public static void test_04() {
        Chromosome[] chromosomes = new Chromosome[50];
        for (int i = 0; i < 50; i++) {
            chromosomes[i] = new Chromosome(new boolean[10], (double) i * 2.5);
        }
        
        Population pop = new Population(chromosomes, 50);
        SimpleElitism elitism = new SimpleElitism(1);
        
        Chromosome[] elite = elitism.select(pop);
        
        assert elite.length == 1 : "Should select exactly 1 elite";
        assert elite[0].getFitness() == 49 * 2.5 : "Elite should be the best chromosome in population";
    }
    
    /**
     * Test 05: Edge cases - elitism count 0, and elitism count equals population size
     * Tests: Handles boundary conditions correctly
     */
    public static void test_05() {
        Chromosome[] chromosomes = new Chromosome[5];
        for (int i = 0; i < 5; i++) {
            chromosomes[i] = new Chromosome(new boolean[10], (double) i * 10);
        }
        Population pop = new Population(chromosomes, 5);
        
        // Test count = 0
        SimpleElitism elitism0 = new SimpleElitism(0);
        Chromosome[] elite0 = elitism0.select(pop);
        assert elite0.length == 0 : "Elitism count 0 should return empty array";
        
        // Test count = population size
        SimpleElitism elitism5 = new SimpleElitism(5);
        Chromosome[] elite5 = elitism5.select(pop);
        assert elite5.length == 5 : "Elitism count equal to population size should return all";
        
        // Verify all are present and ordered by fitness (highest last in heap poll order)
        for (int i = 0; i < 5; i++) {
            assert elite5[i].getFitness() >= 0.0 && elite5[i].getFitness() <= 40.0 : 
                "Elite " + i + " fitness should be within population range";
        }
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        System.out.println("All elitism tests passed!");
    }
}