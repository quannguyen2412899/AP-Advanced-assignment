package core;

import fitnesses.FitnessEvaluator;
import fitnesses.OneMaxFitnessEvaluator;
import fitnesses.KnapsackFitnessEvaluator;
import models.Chromosome;
import models.Population;
import strategies.*;
import utils.RandomUtil;

/**
 * javac -d bin src/models/*.java src/utils/*.java src/fitnesses/*.java src/strategies/*.java src/core/EvolutionStep.java tests/core/TestEvolutionStep.java
 * java -ea -cp bin core.TestEvolutionStep
 */
public class TestEvolutionStep {

    /**
     * Test 01: Basic evolution preserves or improves best fitness
     * Initialize population of 5 chromosomes, run evolve once,
     * assert best fitness after evolution >= best fitness before evolution
     */
    public static void test_01() {
        // Setup
        int populationSize = 5;
        int chromosomeLength = 8;
        FitnessEvaluator fitnessEvaluator = new OneMaxFitnessEvaluator(chromosomeLength);
        
        // Create initial population with known chromosomes
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        initialChromosomes[0] = new Chromosome(new boolean[]{false, true, false, false, false, false, false, false}, 2.0);
        initialChromosomes[1] = new Chromosome(new boolean[]{false, false, true, false, false, false, false, false}, 2.0);
        initialChromosomes[2] = new Chromosome(new boolean[]{false, false, false, true, false, false, false, false}, 2.0);
        initialChromosomes[3] = new Chromosome(new boolean[]{false, false, false, false, false, true, false, false}, 2.0);
        initialChromosomes[4] = new Chromosome(new boolean[]{false, true, false, false, false, false, false, false}, 3.0);
        
        Population initialPopulation = new Population(initialChromosomes, populationSize);
        double bestFitnessBefore = getBestFitness(initialPopulation);
        
        // Create strategies
        ElitismStrategy elitismStrategy = new SimpleElitism(1);
        SelectionStrategy selectionStrategy = new TournamentSelection(3);
        CrossoverStrategy crossoverStrategy = new OnePointCrossover(0.9);
        MutationStrategy mutationStrategy = new BitFlipMutation(0.125);
        RandomUtil randomness = new RandomUtil(42);
        
        // Execute evolution step
        EvolutionStep evolutionStep = new EvolutionStep(
            fitnessEvaluator,
            elitismStrategy,
            selectionStrategy,
            crossoverStrategy,
            mutationStrategy,
            randomness
        );
        Population evolvedPopulation = evolutionStep.evolve(initialPopulation);
        double bestFitnessAfter = getBestFitness(evolvedPopulation);
        
        // Assert best fitness is preserved or improved through elitism
        assert bestFitnessAfter >= bestFitnessBefore :
            "Evolution should preserve or improve best fitness. " +
            "Before: " + bestFitnessBefore + ", After: " + bestFitnessAfter;
        
        // Assert 1: Population size preserved
        assert evolvedPopulation.size() == populationSize :
            "Population size should remain " + populationSize + ", got " + evolvedPopulation.size();
        
        // Assert 2: All chromosomes are valid (correct length and non-negative fitness)
        for (int i = 0; i < evolvedPopulation.size(); i++) {
            Chromosome chromosome = evolvedPopulation.getIndividual(i);
            assert chromosome.length() == chromosomeLength :
                "Chromosome " + i + " has invalid length: " + chromosome.length() + ", expected " + chromosomeLength;
            assert chromosome.getFitness() >= 0 :
                "Chromosome " + i + " has negative fitness: " + chromosome.getFitness();
        }
        
    }

    /**
     * Test 02: Multiple evolution steps with larger population using Knapsack
     * Initialize population of 20 chromosomes, run evolve 5 times,
     * assert best fitness is preserved or improved after multiple generations
     */
    public static void test_02() {
        // Setup
        int populationSize = 20;
        int chromosomeLength = 10;
        int generations = 5;
        
        // Create Knapsack items: weight, value pairs
        double[] weights = {2, 3, 4, 5, 6, 7, 8, 3, 4, 5};
        double[] values = {3, 4, 5, 6, 7, 8, 9, 5, 6, 7};
        double capacityRate = 0.4;
        FitnessEvaluator fitnessEvaluator = new KnapsackFitnessEvaluator(chromosomeLength, weights, values, capacityRate);
        
        // Create initial population with random-like chromosomes
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            boolean[] bits = new boolean[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                bits[j] = (i + j) % 3 == 0; // Pseudo-random pattern
            }
            double fitness = fitnessEvaluator.evaluate(bits);
            initialChromosomes[i] = new Chromosome(bits, fitness);
        }
        
        Population currentPopulation = new Population(initialChromosomes, populationSize);
        double bestFitnessBefore = getBestFitness(currentPopulation);
        
        // Create strategies
        ElitismStrategy elitismStrategy = new SimpleElitism(2);
        SelectionStrategy selectionStrategy = new TournamentSelection(4);
        CrossoverStrategy crossoverStrategy = new OnePointCrossover(0.8);
        MutationStrategy mutationStrategy = new BitFlipMutation(0.1);
        RandomUtil randomness = new RandomUtil(123);
        
        // Execute multiple evolution steps
        EvolutionStep evolutionStep = new EvolutionStep(
            fitnessEvaluator,
            elitismStrategy,
            selectionStrategy,
            crossoverStrategy,
            mutationStrategy,
            randomness
        );
        
        for (int gen = 0; gen < generations; gen++) {
            currentPopulation = evolutionStep.evolve(currentPopulation);
        }
        
        // Track best fitness after evolution
        double bestFitnessAfter = getBestFitness(currentPopulation);
        
        // Assert best fitness is preserved or improved through elitism
        assert bestFitnessAfter >= bestFitnessBefore :
            "Evolution should preserve or improve best fitness over " + generations + " generations. " +
            "Before: " + bestFitnessBefore + ", After: " + bestFitnessAfter;
        
        // Assert 1: Population size preserved
        assert currentPopulation.size() == populationSize :
            "Population size should remain " + populationSize + ", got " + currentPopulation.size();
        
        // Assert 2: All chromosomes are valid (correct length and non-negative fitness)
        for (int i = 0; i < currentPopulation.size(); i++) {
            Chromosome chromosome = currentPopulation.getIndividual(i);
            assert chromosome.length() == chromosomeLength :
                "Chromosome " + i + " has invalid length: " + chromosome.length() + ", expected " + chromosomeLength;
            assert chromosome.getFitness() >= 0 :
                "Chromosome " + i + " has negative fitness: " + chromosome.getFitness();
        }
    }

    /**
     * Test 03: Edge case - minimum population size (2 chromosomes)
     * Initialize population of 2 chromosomes, run evolve once,
     * assert best fitness preserved and population size correct
     */
    public static void test_03() {
        int populationSize = 2;
        int chromosomeLength = 8;
        FitnessEvaluator fitnessEvaluator = new OneMaxFitnessEvaluator(chromosomeLength);
        
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        initialChromosomes[0] = new Chromosome(new boolean[]{true, true, true, false, false, false, false, false}, 3.0);
        initialChromosomes[1] = new Chromosome(new boolean[]{false, false, false, true, true, true, false, false}, 3.0);
        
        Population initialPopulation = new Population(initialChromosomes, populationSize);
        double bestFitnessBefore = getBestFitness(initialPopulation);
        
        EvolutionStep evolutionStep = new EvolutionStep(
            fitnessEvaluator,
            new SimpleElitism(1),
            new TournamentSelection(2),
            new OnePointCrossover(0.5),
            new BitFlipMutation(0.2),
            new RandomUtil(999)
        );
        Population evolvedPopulation = evolutionStep.evolve(initialPopulation);
        double bestFitnessAfter = getBestFitness(evolvedPopulation);
        
        assert bestFitnessAfter >= bestFitnessBefore :
            "Minimum population edge case failed: " + bestFitnessBefore + " -> " + bestFitnessAfter;
        assert evolvedPopulation.size() == populationSize;
    }

    /**
     * Test 04: Edge case - minimum bit chromosome (length 2)
     * Initialize population of 5 chromosomes with length 2, run evolve once,
     * assert population integrity maintained
     */
    public static void test_04() {
        int populationSize = 5;
        int chromosomeLength = 2;
        FitnessEvaluator fitnessEvaluator = new OneMaxFitnessEvaluator(chromosomeLength);
        
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        initialChromosomes[0] = new Chromosome(new boolean[]{true, true}, 2.0);
        initialChromosomes[1] = new Chromosome(new boolean[]{false, false}, 0.0);
        initialChromosomes[2] = new Chromosome(new boolean[]{true, false}, 1.0);
        initialChromosomes[3] = new Chromosome(new boolean[]{false, true}, 1.0);
        initialChromosomes[4] = new Chromosome(new boolean[]{true, true}, 2.0);
        
        Population initialPopulation = new Population(initialChromosomes, populationSize);
        double bestFitnessBefore = getBestFitness(initialPopulation);
        
        EvolutionStep evolutionStep = new EvolutionStep(
            fitnessEvaluator,
            new SimpleElitism(1),
            new TournamentSelection(2),
            new OnePointCrossover(0.5),
            new BitFlipMutation(0.5),
            new RandomUtil(777)
        );
        Population evolvedPopulation = evolutionStep.evolve(initialPopulation);
        double bestFitnessAfter = getBestFitness(evolvedPopulation);
        
        assert bestFitnessAfter >= bestFitnessBefore :
            "Single-bit chromosome edge case failed: " + bestFitnessBefore + " -> " + bestFitnessAfter;
        assert evolvedPopulation.size() == populationSize;
        for (int i = 0; i < evolvedPopulation.size(); i++) {
            assert evolvedPopulation.getIndividual(i).length() == chromosomeLength;
        }
    }

    /**
     * Test 05: Edge case - zero mutation rate (no mutations)
     * Initialize population of 10 chromosomes, run evolve with mutation rate 0,
     * assert genetic diversity changes only through crossover and selection
     */
    public static void test_05() {
        int populationSize = 10;
        int chromosomeLength = 8;
        FitnessEvaluator fitnessEvaluator = new OneMaxFitnessEvaluator(chromosomeLength);
        
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            boolean[] bits = new boolean[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                bits[j] = (i * 2 + j) % 2 == 0;
            }
            int fitness = 0;
            for (boolean b : bits) if (b) fitness++;
            initialChromosomes[i] = new Chromosome(bits, fitness);
        }
        
        Population initialPopulation = new Population(initialChromosomes, populationSize);
        double bestFitnessBefore = getBestFitness(initialPopulation);
        
        EvolutionStep evolutionStep = new EvolutionStep(
            fitnessEvaluator,
            new SimpleElitism(1),
            new TournamentSelection(3),
            new OnePointCrossover(0.7),
            new BitFlipMutation(0.0), // No mutations
            new RandomUtil(555)
        );
        Population evolvedPopulation = evolutionStep.evolve(initialPopulation);
        double bestFitnessAfter = getBestFitness(evolvedPopulation);
        
        assert bestFitnessAfter >= bestFitnessBefore :
            "Zero mutation rate edge case failed: " + bestFitnessBefore + " -> " + bestFitnessAfter;
        assert evolvedPopulation.size() == populationSize;
        for (int i = 0; i < evolvedPopulation.size(); i++) {
            Chromosome chromosome = evolvedPopulation.getIndividual(i);
            assert chromosome.length() == chromosomeLength;
            assert chromosome.getFitness() >= 0;
        }
    }
    
    private static double getBestFitness(Population population) {
        double best = 0;
        for (int i = 0; i < population.size(); i++) {
            double fitness = population.getIndividual(i).getFitness();
            if (fitness > best) best = fitness;
        }
        return best;
    }

    public static void main(String[] args) {
        test_01();
        test_02();
        test_03();
        test_04();
        test_05();
        System.out.println("All EvolutionStep tests passed!");
    }
}
