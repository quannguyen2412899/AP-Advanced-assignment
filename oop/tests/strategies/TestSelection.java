package strategies;

import models.*;
import utils.RandomUtil;
import utils.FakeRandomUtil;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/strategies/*.java tests/utils/FakeRandomUtil.java tests/strategies/TestSelection.java
 * java -ea -cp bin strategies.TestSelection
 */
public class TestSelection {
    /**
     * Test 01: Tournament selection with size 3 from population of 5
     * Tests: Selects the best chromosome from tournament (indices 0, 3, 4)
     */
    public static void test_01() {
        Chromosome[] chromosomes = new Chromosome[5];
        chromosomes[0] = new Chromosome(new boolean[10], 10.0);
        chromosomes[1] = new Chromosome(new boolean[10], 50.0);
        chromosomes[2] = new Chromosome(new boolean[10], 30.0);
        chromosomes[3] = new Chromosome(new boolean[10], 80.0);
        chromosomes[4] = new Chromosome(new boolean[10], 20.0);
        
        Population pop = new Population(chromosomes, 5);
        TournamentSelection tournament = new TournamentSelection(3);
        
        // FakeRandomUtil will return 0, 3, 4 in sequence
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{0, 3, 4}, new boolean[]{});
        
        Chromosome selected = tournament.select(pop, fakeRandom);
        
        assert selected.getFitness() == 80.0 : "Should select chromosome at index 3 with fitness 80.0 (max of 10.0, 80.0, 20.0)";
        assert selected == chromosomes[3] : "Should select the exact chromosome object from index 3";
    }
    
    /**
     * Test 02: Tournament selection - verify correct chromosome selection with different indices
     * Tests: Selects best chromosome from different tournament participants
     */
    public static void test_02() {
        Chromosome[] chromosomes = new Chromosome[6];
        chromosomes[0] = new Chromosome(new boolean[10], 25.0);
        chromosomes[1] = new Chromosome(new boolean[10], 15.0);
        chromosomes[2] = new Chromosome(new boolean[10], 90.0);
        chromosomes[3] = new Chromosome(new boolean[10], 40.0);
        chromosomes[4] = new Chromosome(new boolean[10], 60.0);
        chromosomes[5] = new Chromosome(new boolean[10], 35.0);
        
        Population pop = new Population(chromosomes, 6);
        TournamentSelection tournament = new TournamentSelection(4);
        
        // FakeRandomUtil will return 1, 2, 4, 5 in sequence
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{1, 2, 4, 5}, new boolean[]{});
        
        Chromosome selected = tournament.select(pop, fakeRandom);
        
        assert selected.getFitness() == 90.0 : "Should select chromosome at index 2 with fitness 90.0 (max of 15.0, 90.0, 60.0, 35.0)";
        assert selected == chromosomes[2] : "Should select the exact chromosome object from index 2";
    }
    
    /**
     * Test 03: Tournament selection - single participant tournament
     * Tests: Correctly returns that single participant
     */
    public static void test_03() {
        Chromosome[] chromosomes = new Chromosome[5];
        chromosomes[0] = new Chromosome(new boolean[10], 10.0);
        chromosomes[1] = new Chromosome(new boolean[10], 85.0);
        chromosomes[2] = new Chromosome(new boolean[10], 30.0);
        chromosomes[3] = new Chromosome(new boolean[10], 50.0);
        chromosomes[4] = new Chromosome(new boolean[10], 20.0);
        
        Population pop = new Population(chromosomes, 5);
        TournamentSelection tournament = new TournamentSelection(1);
        
        // FakeRandomUtil will return 2
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{2}, new boolean[]{});
        
        Chromosome selected = tournament.select(pop, fakeRandom);
        
        assert selected.getFitness() == 30.0 : "Should select chromosome at index 2 with fitness 30.0";
        assert selected == chromosomes[2] : "Should select the exact chromosome object from index 2";
    }
    
    /**
     * Test 04: Tournament selection - edge cases
     * Tests: Tournament size equals population size, same fitness values, all chromosomes valid
     */
    public static void test_04() {
        Chromosome[] chromosomes = new Chromosome[4];
        chromosomes[0] = new Chromosome(new boolean[10], 50.0);
        chromosomes[1] = new Chromosome(new boolean[10], 50.0);
        chromosomes[2] = new Chromosome(new boolean[10], 100.0);
        chromosomes[3] = new Chromosome(new boolean[10], 50.0);
        
        Population pop = new Population(chromosomes, 4);
        
        // Edge case 1: Tournament size equals population size
        TournamentSelection tournament4 = new TournamentSelection(4);
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{0, 1, 2, 3}, new boolean[]{});
        Chromosome selected = tournament4.select(pop, fakeRandom);
        assert selected.getFitness() == 100.0 : "Should select best from entire population (index 2 with 100.0)";
        assert selected == chromosomes[2] : "Should select the exact chromosome object from index 2";
        
        // Edge case 2: Tournament size 2 with duplicate fitnesses
        TournamentSelection tournament2 = new TournamentSelection(2);
        FakeRandomUtil fakeRandom2 = new FakeRandomUtil(new int[]{1, 3}, new boolean[]{});
        Chromosome selected2 = tournament2.select(pop, fakeRandom2);
        assert selected2.getFitness() == 50.0 : "Should select one of the chromosomes with 50.0 fitness";
        assert (selected2 == chromosomes[1] || selected2 == chromosomes[3]) : "Should be one of the selected indices";
        
        // Edge case 3: Large tournament size from large population
        Chromosome[] largeChromosomes = new Chromosome[50];
        for (int i = 0; i < 50; i++) {
            largeChromosomes[i] = new Chromosome(new boolean[10], (double) i * 2.0);
        }
        Population largePop = new Population(largeChromosomes, 50);
        TournamentSelection tournament10 = new TournamentSelection(10);
        int[] indices = {5, 15, 25, 35, 45, 8, 12, 22, 30, 40};
        FakeRandomUtil fakeRandom3 = new FakeRandomUtil(indices, new boolean[]{});
        Chromosome selected3 = tournament10.select(largePop, fakeRandom3);
        assert selected3.getFitness() == 45 * 2.0 : "Should select index 45 with fitness 90.0 (max of provided indices)";
        assert selected3 == largeChromosomes[45] : "Should select the exact chromosome object from index 45";
    }
    
    /**
     * Test 05: Tournament selection - boundary and invalid input cases
     * Tests: Tournament size > population size throws exception, all same fitness values
     */
    public static void test_05() {
        Chromosome[] chromosomes = new Chromosome[3];
        chromosomes[0] = new Chromosome(new boolean[10], 75.0);
        chromosomes[1] = new Chromosome(new boolean[10], 75.0);
        chromosomes[2] = new Chromosome(new boolean[10], 75.0);
        
        Population pop = new Population(chromosomes, 3);
        
        // Edge case 1: Tournament size > population size should throw
        TournamentSelection tournament5 = new TournamentSelection(5);
        FakeRandomUtil fakeRandom = new FakeRandomUtil(new int[]{0, 1, 2, 0, 1}, new boolean[]{});
        boolean exceptionThrown = false;
        try {
            tournament5.select(pop, fakeRandom);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
            assert e.getMessage().contains("tournamentSize") : "Exception message should mention tournamentSize";
        }
        assert exceptionThrown : "Should throw IllegalArgumentException when tournament size > population size";
        
        // Edge case 2: All chromosomes have same fitness - any selection is valid
        TournamentSelection tournament2 = new TournamentSelection(2);
        FakeRandomUtil fakeRandom2 = new FakeRandomUtil(new int[]{0, 2}, new boolean[]{});
        Chromosome selected = tournament2.select(pop, fakeRandom2);
        assert selected.getFitness() == 75.0 : "Should select a chromosome with fitness 75.0";
        assert (selected == chromosomes[0] || selected == chromosomes[2]) : "Should be one of the tournament participants";
        
        // Edge case 3: Very small population (size 2)
        Chromosome[] smallChromosomes = new Chromosome[2];
        smallChromosomes[0] = new Chromosome(new boolean[10], 10.0);
        smallChromosomes[1] = new Chromosome(new boolean[10], 99.0);
        Population smallPop = new Population(smallChromosomes, 2);
        TournamentSelection tournament2_small = new TournamentSelection(2);
        FakeRandomUtil fakeRandom3 = new FakeRandomUtil(new int[]{0, 1}, new boolean[]{});
        Chromosome selected2 = tournament2_small.select(smallPop, fakeRandom3);
        assert selected2.getFitness() == 99.0 : "Should select best from 2-chromosome population";
        assert selected2 == smallChromosomes[1] : "Should select chromosome at index 1";
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        System.out.println("All tournament selection tests passed!");
    }
}