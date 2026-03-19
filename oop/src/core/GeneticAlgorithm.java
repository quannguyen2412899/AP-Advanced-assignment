package core;

import fitnesses.FitnessEvaluator;
import models.Population;
import strategies.*;

public class GeneticAlgorithm {
    private EvolutionStep oneStepEvolution;
    private int maxGenerations;
    
    GeneticAlgorithm(GAConfig config) {
        /* implements */
    }

    public Population run(Population initialPopulation, EvolutionReporter analyzer) {
        /* implements */
        return null;
    }
}

class EvolutionStep {
    private FitnessEvaluator fitnessEvaluator;
    private SelectionStrategy selectionStrategy;
    private CrossoverStrategy crossoverStrategy;
    private MutationStrategy mutationStrategy;

    EvolutionStep(FitnessEvaluator fe,
                SelectionStrategy ss,
                CrossoverStrategy cs,
                MutationStrategy ms) {
    
    }
    public Population evolve(Population population) {
        return null;
    }
}