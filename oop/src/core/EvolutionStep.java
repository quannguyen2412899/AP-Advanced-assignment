package core;

import fitnesses.FitnessEvaluator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import models.*;
import strategies.CrossoverStrategy;
import strategies.ElitismStrategy;
import strategies.MutationStrategy;
import strategies.SelectionStrategy;
import utils.RandomUtil;


/**
 * Define one iteration of GA
 */
public class EvolutionStep {
    private FitnessEvaluator fitnessEvaluator;
    private ElitismStrategy elitismStrategy;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;
    private RandomUtil randomness;

    public EvolutionStep(FitnessEvaluator fe,
                        ElitismStrategy es,
                        SelectionStrategy ss,
                        CrossoverStrategy cs,
                        MutationStrategy ms,
                        RandomUtil random) {
        this.fitnessEvaluator = fe;
        this.elitismStrategy = es;
        this.selectionStrategy = ss;
        this.crossoverStrategy = cs;
        this.mutationStrategy = ms;
        this.randomness = random;
    }

    /**
     * Generate a new population with the same size as the input population
     */
    public Population evolve(Population population) {
        int populationSize = population.size();
        int chromosomeLength = population.getIndividual(0).length();
        List<Chromosome> newPopulationChromosomes = new ArrayList<>();

        // --------- Elitism ---------
        Chromosome[] eliteGroup = elitismStrategy.select(population);
        newPopulationChromosomes.addAll(Arrays.asList(eliteGroup));

        // --------- REPRODUCTION ---------
        while(newPopulationChromosomes.size() < populationSize) {
            // --------- Selection ---------
            Chromosome parent1 = selectionStrategy.select(population, randomness);
            Chromosome parent2 = selectionStrategy.select(population, randomness);

            // --------- Crossover ---------
            BitSet parentGenes1 = parent1.getBitString();
            BitSet parentGenes2 = parent2.getBitString();
            BitSet[] childrenGenes = crossoverStrategy.crossover(parentGenes1, parentGenes2, chromosomeLength, randomness);
            BitSet childGenes1 = childrenGenes[0];
            BitSet childGenes2 = childrenGenes[1];

            // --------- Mutation ---------
            childGenes1 = mutationStrategy.mutate(childGenes1, chromosomeLength, randomness);
            childGenes2 = mutationStrategy.mutate(childGenes2, chromosomeLength, randomness);

            // --------- Fitness evaluation  ---------
            Chromosome child1 = new Chromosome(childGenes1, chromosomeLength, fitnessEvaluator.evaluate(childGenes1));
            Chromosome child2 = new Chromosome(childGenes2, chromosomeLength, fitnessEvaluator.evaluate(childGenes2));
            
            // --------- Insertion ---------
            if(newPopulationChromosomes.size() < populationSize) newPopulationChromosomes.add(child1);
            if(newPopulationChromosomes.size() < populationSize) newPopulationChromosomes.add(child2);
        }

        // --------- Exception handling ---------
        if (newPopulationChromosomes.size() != populationSize) {
            throw new IllegalStateException("EvolutionStep.evolve: population size mismatch after generation. " +
                                            "Created " + newPopulationChromosomes.size() + " != expected " + populationSize + ". " +
                                            "Elitism produced " + eliteGroup.length + " individuals, " +
                                            "generated " + (newPopulationChromosomes.size() - eliteGroup.length) + " offspring.");
        }
        Chromosome[] chromosomes = newPopulationChromosomes.toArray(Chromosome[]::new);
        return new Population(chromosomes, chromosomes.length);
    }
}