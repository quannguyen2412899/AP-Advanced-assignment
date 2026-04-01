package core;

import fitnesses.FitnessEvaluator;
import models.*;
import reporter.*;
import strategies.*;
import utils.RandomUtil;

public class GeneticAlgorithm {
    private EvolutionStep oneStepEvolution;
    
    public GeneticAlgorithm(FitnessEvaluator fe,
                            ElitismStrategy es,
                            SelectionStrategy ss,
                            CrossoverStrategy cs,
                            MutationStrategy ms,
                            RandomUtil random) {
        this.oneStepEvolution = new EvolutionStep(fe, es, ss, cs, ms, random);
    }

    // Return the best Chromosome found after evolution
    // Currently only return the last generation best chromosome, not the best among generations
    public Chromosome run(Population initialPopulation, EvolutionReporter reporter, int maxGenerations) {
        if (maxGenerations <= 0) throw new IllegalArgumentException("maxGenerations must be > 0");
        
        Population currentPopulation = initialPopulation;
        int generation;
        for (generation = 0; generation < maxGenerations; generation++) {
            try {
                // --------- One step evolution ---------
                Population newPopulation = oneStepEvolution.evolve(currentPopulation);
                currentPopulation = newPopulation;
            } catch (Exception e) {
                throw new IllegalStateException("Error during generation " + generation + ": " + e.getMessage(), e);
            }
            // --------- Analysis ---------
            reporter.record(GenerationStatistics.statisticsOf(currentPopulation, generation));
        }

        ElitismStrategy mostEliteSelector = new SimpleElitism(1);
        Chromosome bestChromosome = mostEliteSelector.select(currentPopulation)[0];
        return bestChromosome;
    }
}