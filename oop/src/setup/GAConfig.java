package setup;

import fitnesses.*;
import models.*;
import strategies.*;
import utils.RandomUtil;

public class GAConfig {
    private int populationSize;
    private int chromosomeLength;
    private int maxGenerations;
    private int randomSeed;
    private FitnessEvaluator fitnessEvaluator;
    private ElitismStrategy elitismStrategy;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;
    /** Strategies are stateless so all class can use the same strategy object, the oppsite for RandomUtil */

    public GAConfig(int populationSize,
                    int chromosomeLength,
                    int maxGenerations,
                    int randomSeed,
                    FitnessEvaluator fitnessEvaluator,
                    ElitismStrategy elitismStrategy,
                    SelectionStrategy selectionStrategy,
                    CrossoverStrategy crossoverStrategy,
                    MutationStrategy mutationStrategy) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.maxGenerations = maxGenerations;
        this.randomSeed = randomSeed;
        this.fitnessEvaluator = fitnessEvaluator;
        this.elitismStrategy = elitismStrategy;
        this.selectionStrategy = selectionStrategy;
        this.crossoverStrategy = crossoverStrategy;
        this.mutationStrategy = mutationStrategy;
    }
    

    public FitnessEvaluator fitnessEvaluator() {
        return fitnessEvaluator;
    }
    public ElitismStrategy elitismStrategy() {
        return elitismStrategy;
    }
    public SelectionStrategy selectionStrategy() {
        return selectionStrategy;
    }
    public CrossoverStrategy crossoverStrategy() {
        return crossoverStrategy;
    }
    public MutationStrategy mutationStrategy() {
        return mutationStrategy;
    }
    public RandomUtil randomUtil() {
        return new RandomUtil(randomSeed);
    }
    public int populationSize() {
        return populationSize;
    }
    public int chromosomeLength() {
        return chromosomeLength;
    }
    public int maxGenerations() {
        return maxGenerations;
    }
    public int randomSeed() {
        return randomSeed;
    }
    public Population generateRandomPopulation() {
        FitnessEvaluator fitness = fitnessEvaluator();
        RandomUtil random = randomUtil();
        Chromosome[] _population = new Chromosome[populationSize];
        for (int i = 0; i < _population.length; i++) {
            boolean[] _chromosome = new boolean[chromosomeLength];
            for (int j = 0; j < chromosomeLength; j++) {
                _chromosome[j] = random.nextBernoulli(0.5);
            }
            _population[i] = new Chromosome(_chromosome, fitness.evaluate(_chromosome));
        }
        return new Population(_population, populationSize);
    }
}