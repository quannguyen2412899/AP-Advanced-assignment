import fitnesses.FitnessEvaluator;
import fitnesses.OneMaxFitnessEvaluator;
import fitnesses.KnapsackFitnessEvaluator;
import models.Chromosome;
import models.Population;
import strategies.*;
import utils.RandomUtil;
import core.GeneticAlgorithm;
import reporter.EvolutionReporter;
import reporter.GenerationStatistics;

/**
 * javac -d bin -cp "lib/*" src/models/*.java src/utils/*.java src/fitnesses/*.java src/strategies/*.java src/core/*.java src/reporter/*.java tests/core/TestGeneticAlgorithm.java
 * java -ea -cp "bin:lib/*" TestGeneticAlgorithm
 */
public class TestGeneticAlgorithm {
    // Configuration from config.json
    private static final SelectionStrategy selectionStrategy = new TournamentSelection(3);
    private static final CrossoverStrategy crossoverStrategy = new OnePointCrossover(0.9);
    private static final MutationStrategy mutationStrategy = new BitFlipMutation(0.01);
    private static final ElitismStrategy elitismStrategy = new SimpleElitism(2);
    private static final RandomUtil randomness = new RandomUtil(42);
    
    public static void test_01() {
        // Setup
        int populationSize = 10;
        int chromosomeLength = 10;
        int setBits = 3;
        int maxGenerations = 50;
        
        // Create initial population with 10 chromosomes of length 10, each with 3 set bits
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            boolean[] bits = new boolean[chromosomeLength];
            
            // Set exactly 3 bits
            int bitsSet = 0;
            for (int j = 0; j < chromosomeLength && bitsSet < setBits; j++) {
                if ((i + j) % (chromosomeLength / setBits) == 0) {
                    bits[j] = true;
                    bitsSet++;
                }
            }
            
            initialChromosomes[i] = new Chromosome(bits, setBits);
        }
        
        Population initialPopulation = new Population(initialChromosomes, populationSize);
        
        // Create fitness evaluator
        FitnessEvaluator fitnessEvaluator = new OneMaxFitnessEvaluator(chromosomeLength);
        
        // Initialize genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(
            fitnessEvaluator,
            elitismStrategy,
            selectionStrategy,
            crossoverStrategy,
            mutationStrategy,
            randomness
        );
        
        // Run GA with dummy reporter
        EvolutionReporter dummyReporter = new DummyReporter();
        Chromosome bestChromosome = ga.run(initialPopulation, dummyReporter, maxGenerations);
        
        // Assert the result fitness
        double bestFitness = bestChromosome.getFitness();
        assert bestFitness >= setBits :
            "Best fitness should be at least " + setBits + ", got " + bestFitness;
        assert bestFitness <= chromosomeLength :
            "Best fitness should not exceed chromosome length " + chromosomeLength + ", got " + bestFitness;
    }

    public static void test_02() {
        // Setup for Knapsack problem
        double[] weights = {1, 4, 3, 5, 6, 3, 3, 9, 0, 7};
        double[] values = {7, 6, 7, 4, 1, 1, 0, 2, 3, 4};
        double capacityRate = 0.4;
        
        int populationSize = 10;
        int chromosomeLength = weights.length;
        int maxGenerations = 750;
        
        // Create initial population
        Chromosome[] initialChromosomes = new Chromosome[populationSize];
        for (int i = 0; i < populationSize; i++) {
            boolean[] bits = new boolean[chromosomeLength];
            
            // Random bit initialization
            for (int j = 0; j < chromosomeLength; j++) {
                bits[j] = (i + j) % 2 == 0;
            }
            
            initialChromosomes[i] = new Chromosome(bits, 0);
        }
        
        Population initialPopulation = new Population(initialChromosomes, populationSize);
        
        // Create Knapsack fitness evaluator
        FitnessEvaluator fitnessEvaluator = new KnapsackFitnessEvaluator(chromosomeLength, weights, values, capacityRate);
        
        // Initialize genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(
            fitnessEvaluator,
            elitismStrategy,
            selectionStrategy,
            crossoverStrategy,
            mutationStrategy,
            randomness
        );
        
        // Run GA with dummy reporter
        EvolutionReporter dummyReporter = new DummyReporter();
        Chromosome bestChromosome = ga.run(initialPopulation, dummyReporter, maxGenerations);
        
        // Assert the result fitness
        double bestFitness = bestChromosome.getFitness();
        assert bestFitness == 28.0 :
            "Best fitness should be 28.0, got " + bestFitness;
    }
    
    public static void main(String[] args) {
        test_01();
        test_02();
        System.out.println("All GeneticAlgorithm tests passed!");
    }
}

class DummyReporter extends EvolutionReporter {
    @Override
    public void record(GenerationStatistics statistics) {
        // Do nothing - no recording for dummy reporter
    }
    
    @Override
    public void exportStatistics(String destination) {
        // Do nothing - no export for dummy reporter
    }
}